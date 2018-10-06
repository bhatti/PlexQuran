package com.plexobject.quran.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import com.plexobject.quran.metrics.Metric;
import com.plexobject.quran.metrics.Timer;
import com.plexobject.quran.model.PersistentEntity;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryIntegrityException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.evolve.IncompatibleClassException;

public class PersistenceStorageBdb<T extends PersistentEntity> implements
		PersistenceStorage<T> {
	private static final int MAX_MAX_LIMIT = 1024;
	private static final int DEFAULT_LIMIT = 256;

	protected static final Logger LOGGER = Logger
			.getLogger(PersistenceStorageBdb.class);
	private static DatabaseManagerBdb _databaseManager;
	protected final String databaseName;
	private EntityStore _store;
	private PrimaryIndex<Long, T> _index;
	private SecondaryIndex<Long, Long, T> _createdAtIndex;
	private SecondaryIndex<Integer, Long, T> _hashIndex;
	private SecondaryIndex<Boolean, Long, T> _syncToDBIndex;
	private Class<T> persistentClass;

	static synchronized DatabaseManagerBdb _getDatabaseManager()
			throws IOException {
		if (_databaseManager == null) {
			_databaseManager = new DatabaseManagerBdb();
		}
		return _databaseManager;
	}

	@SuppressWarnings("unchecked")
	public PersistenceStorageBdb(String dbname) {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("dbname is not specified");
		}

		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		this.databaseName = dbname;
	}

	@Override
	public DatabaseManager getDatabaseManager() throws IOException {
		return _getDatabaseManager();
	}

	@Override
	public T save(T entity) throws IOException {
		if (entity == null) {
			throw new NullPointerException("PersistentEntity not specified");
		}
		entity.setHashCode();
		if (entity.getId() == null) {
			Long id = null;
			Collection<T> oldCopies = getByHashCode(entity.getHashCode());
			if (oldCopies.size() > 0) {
				LOGGER.info("Found " + oldCopies.size()
						+ " copies matching hash code " + entity.getHashCode());
				for (T next : oldCopies) {
					if (next.equals(entity)) {
						id = next.getId();
						break;
					}
				}
			}
			if (id == null) {
				entity.setId(_getDatabaseManager().getNextKey(databaseName));
			} else {
				entity.setId(id);
			}
		}
		entity.setUpdatedDate(new Date());
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.savePersistentEntity");
		try {
			T old = getIndex().put(entity);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("saving old " + old + "\n new "
						+ entity.getHashCode() + ", " + entity);
			}
			_getDatabaseManager().sync();
			return entity;
		} catch (IllegalArgumentException e) {
			throw new IOException("Failed to save " + entity, e);
		} catch (SecondaryIntegrityException e) {
			close();
			_getDatabaseManager().deleteDatabase(databaseName);
			return save(entity);
		} catch (DatabaseException e) {
			throw new IOException("Failed to save " + entity, e);
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return save(entity);
		} finally {
			timer.stop();
		}
	}

	@Override
	public T get(Long id) throws IOException {
		return get(id, 0);
	}

	private T get(Long id, int retries) throws IOException {
		if (id == null) {
			throw new IllegalArgumentException("id name not specified");
		}
		final Timer timer = Metric.newTimer("PersistenceStorageBdb.get");
		try {
			T d = getIndex().get(id);
			if (d == null) {
				LOGGER.debug("#### Could not find entry for " + id + " in "
						+ databaseName);
				throw new FileNotFoundException("Failed to find " + id + " in "
						+ databaseName);
			}

			return d;
		} catch (NullPointerException e) {
			if (retries < 2) {
				_getDatabaseManager().open();
				return get(id, retries + 1);
			} else {
				throw e;
			}
		} catch (IllegalStateException e) {
			if (retries < 2) {
				_getDatabaseManager().open();
				return get(id, retries + 1);
			} else {
				throw e;
			}

		} catch (DatabaseException e) {
			_getDatabaseManager().flushMemory(false);
			if (retries < 2) {
				return get(id, retries + 1);
			} else {
				throw new IOException("Failed to get PersistentEntity with id "
						+ id, e);
			}
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return get(id, retries);
		} finally {
			timer.stop();
		}
	}

	@Override
	public boolean remove(Long id) throws IOException {
		if (GenericValidator.isBlankOrNull(databaseName)) {
			throw new IllegalArgumentException("database name not specified");
		}
		if (id == null) {
			throw new IllegalArgumentException("id name not specified");
		}

		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.deletePersistentEntity");
		try {
			LOGGER.debug("PersistenceStorageBdb.removet removing object for "
					+ databaseName + "/" + id);
			return getIndex().delete(id);
		} catch (SecondaryIntegrityException e) {
			close();
			_getDatabaseManager().deleteDatabase(databaseName);
			return false;
		} catch (DatabaseException e) {
			throw new IOException("Failed to remove " + id + " in "
					+ databaseName, e);
		} finally {
			timer.stop();
		}
	}

	@Override
	public Collection<T> getByHashCode(final int hashCode) throws IOException {
		if (GenericValidator.isBlankOrNull(databaseName)) {
			throw new IllegalArgumentException("database name not specified");
		}
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getByHashCode");
		List<T> all = new ArrayList<T>();
		EntityCursor<T> cursor = null;
		try {
			cursor = getHashIndex().entities(hashCode, true, hashCode, true);
			Iterator<T> it = cursor.iterator();
			while (it.hasNext()) {
				T next = it.next();
				all.add(next);
			}
			return all;
		} catch (DatabaseException e) {
			throw new IOException("Failed to find all in " + databaseName, e);
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return getByHashCode(hashCode);
		} finally {
			timer.stop();
			if (cursor != null) {
				try {
					cursor.close();
				} catch (DatabaseException e) {
					LOGGER.error("failed to close cursor", e);
				}
			}
		}
	}

	@Override
	public List<T> getAll(Long startKey, int limit,
			final Long createdDateGreaterThan) throws IOException {
		if (GenericValidator.isBlankOrNull(databaseName)) {
			throw new IllegalArgumentException("database name not specified");
		}
		if (limit <= 0) {
			limit = DEFAULT_LIMIT;
		}
		limit = Math.max(limit, MAX_MAX_LIMIT);
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getAllPersistentEntitys");

		EntityCursor<T> cursor = null;
		List<T> all = new ArrayList<T>();
		try {
			if (createdDateGreaterThan == null) {
				cursor = getIndex().entities(startKey, true, null, true);
				Iterator<T> it = cursor.iterator();
				for (int i = 0; it.hasNext() && i < limit; i++) {
					T next = it.next();
					all.add(next);
				}
			} else {
				cursor = getCreatedAtIndex().entities(createdDateGreaterThan,
						true, new Long(System.currentTimeMillis()), true);
				Iterator<T> it = cursor.iterator();
				for (int i = 0; it.hasNext() && i < startKey; i++) {
					it.next();
				}
				for (int i = 0; it.hasNext() && i < limit; i++) {
					T next = it.next();
					all.add(next);
				}
			}
			return all;
		} catch (IllegalStateException e) {
			close();
			_getDatabaseManager().open();
			return getAll(startKey, limit, createdDateGreaterThan);
		} catch (DatabaseException e) {
			throw new IOException("Failed to find all in " + databaseName, e);
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return getAll(startKey, limit, createdDateGreaterThan);
		} finally {
			timer.stop();
			if (cursor != null) {
				try {
					cursor.close();
				} catch (DatabaseException e) {
					LOGGER.error("failed to close cursor", e);
				}
			}
		}
	}

	@Override
	public PagedList<T> getAll(Long startKey, Long endKey, int max,
			final Long createdDateGreaterThan) throws IOException {
		return getAll(startKey, endKey, max, createdDateGreaterThan, 0);
	}

	private PagedList<T> getAll(Long startKey, Long endKey, int max,
			final Long createdDateGreaterThan, int retries) throws IOException {
		if (GenericValidator.isBlankOrNull(databaseName)) {
			throw new IllegalArgumentException("database name not specified");
		}
		final int limit = Math.min(max, MAX_MAX_LIMIT);

		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getAllPersistentEntitys");

		EntityCursor<T> cursor = null;
		List<T> all = new ArrayList<T>();
		Long lastKey = null;

		try {
			if (createdDateGreaterThan == null) {
				cursor = getIndex().entities(startKey, true, endKey, true);
				Iterator<T> it = cursor.iterator();
				for (int i = 0; it.hasNext() && i < limit; i++) {
					T next = it.next();
					all.add(next);
					lastKey = next.getId();
				}

			} else {
				cursor = getCreatedAtIndex().entities(createdDateGreaterThan,
						true, new Long(System.currentTimeMillis()), true);
				Iterator<T> it = cursor.iterator();

				for (int i = 0; it.hasNext() && i < limit;) {
					T next = it.next();
					if (startKey != null
							&& startKey.longValue() < next.getId().longValue()) {
						continue;
					}
					all.add(next);
					lastKey = next.getId();
					i++;
				}
			}

			return new PagedList<T>(all, startKey, lastKey, limit,
					all.size() == limit);
		} catch (IllegalStateException e) {
			if (retries < 2) {
				close();
				_getDatabaseManager().open();
				return getAll(startKey, endKey, max, createdDateGreaterThan,
						retries + 1);
			} else {
				throw e;
			}

		} catch (DatabaseException e) {
			throw new IOException("Failed to get PersistentEntitys from "
					+ databaseName + " with range " + startKey + "-" + endKey,
					e);
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return getAll(startKey, endKey, max, createdDateGreaterThan);
		} finally {
			timer.stop();
			if (cursor != null) {
				try {
					cursor.close();
				} catch (DatabaseException e) {
					LOGGER.error("failed to close cursor", e);
				}
			}
		}
	}

	@Override
	public Map<Long, T> get(Long... ids) throws IOException {
		return get(Arrays.asList(ids));
	}

	@Override
	public Map<Long, T> get(Collection<Long> ids) throws IOException {
		if (null == ids) {
			throw new IllegalArgumentException("ids not specified");
		}
		Map<Long, T> docsById = new TreeMap<Long, T>();
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getPersistentEntitys");
		try {
			PrimaryIndex<Long, T> primaryIndex = getIndex();
			for (Long id : ids) {
				T d = primaryIndex.get(id);
				if (d != null) {
					docsById.put(id, d);
				}
			}
			return docsById;
		} catch (DatabaseException e) {
			throw new IOException("Failed to get PersistentEntitys with ids "
					+ ids, e);
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return get(ids);
		} finally {
			timer.stop();
		}
	}

	@Override
	public void close() {
		try {
			EntityStore store = _getStore();
			if (store != null) {
				try {
					store.close();
				} catch (DatabaseException e) {
					LOGGER.error("Failed to close " + store, e);
				} catch (IllegalStateException e) {
				}
			}
		} catch (IOException e) {
			LOGGER.error("Failed to get store for closing", e);
		}
		_index = null;
		_createdAtIndex = null;
		_hashIndex = null;
		_syncToDBIndex = null;
		_store = null;
	}

	public long count(final String dbname) throws IOException {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("database name not specified");
		}
		try {
			return _getDatabaseManager().getDatabase(dbname).count();
		} catch (DatabaseException e) {
			throw new IOException("Failed to count " + dbname, e);
		}
	}

	//
	protected synchronized PrimaryIndex<Long, T> getIndex() throws IOException {
		if (GenericValidator.isBlankOrNull(databaseName)) {
			throw new IllegalArgumentException("dbname is not specified");
		}

		if (_index == null) {
			try {
				_index = _getStore().getPrimaryIndex(Long.class,
						persistentClass);
			} catch (IncompatibleClassException e) {
				throw new IOException(e);
			} catch (DatabaseException e) {
				throw new IOException(e);
			} catch (IllegalStateException e) {
				close();
				return getIndex();
			}
		}
		return _index;
	}

	//
	private synchronized SecondaryIndex<Long, Long, T> getCreatedAtIndex()
			throws IOException {
		if (_createdAtIndex == null) {
			try {
				_createdAtIndex = _getStore().getSecondaryIndex(getIndex(),
						Long.class, "createdAt");
			} catch (IncompatibleClassException e) {
				throw new IOException(e);
			} catch (DatabaseException e) {
				throw new IOException(e);
			} catch (IllegalStateException e) {
				close();
				return getCreatedAtIndex();
			}
		}
		return _createdAtIndex;
	}

	private synchronized SecondaryIndex<Boolean, Long, T> getSyncToDBIndex()
			throws IOException {
		if (_syncToDBIndex == null) {
			try {
				_syncToDBIndex = _getStore().getSecondaryIndex(getIndex(),
						Boolean.class, "syncToDB");
			} catch (IncompatibleClassException e) {
				throw new IOException(e);
			} catch (DatabaseException e) {
				throw new IOException(e);
			} catch (IllegalStateException e) {
				close();
				return getSyncToDBIndex();
			}
		}
		return _syncToDBIndex;
	}

	private synchronized SecondaryIndex<Integer, Long, T> getHashIndex()
			throws IOException {
		if (_hashIndex == null) {
			try {
				_hashIndex = _getStore().getSecondaryIndex(getIndex(),
						Integer.class, "hashCode");
			} catch (IncompatibleClassException e) {
				throw new IOException(e);
			} catch (DatabaseException e) {
				throw new IOException(e);
			} catch (IllegalStateException e) {
				close();
				return getHashIndex();
			}
		}
		return _hashIndex;
	}

	//
	protected EntityStore _getStore() throws IOException {
		if (_store == null) {
			_store = _getDatabaseManager().newEntityStore(databaseName);
		}
		return _store;
	}

	@Override
	public Collection<T> getAllUnsyncedToDB() throws IOException {
		if (GenericValidator.isBlankOrNull(databaseName)) {
			throw new IllegalArgumentException("database name not specified");
		}
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getAllUnsyncedToDB");
		List<T> all = new ArrayList<T>();
		EntityCursor<T> cursor = null;
		try {
			cursor = getSyncToDBIndex().entities(false, true, false, true);
			Iterator<T> it = cursor.iterator();
			while (it.hasNext()) {
				T next = it.next();
				all.add(next);
			}
			return all;
		} catch (DatabaseException e) {
			throw new IOException("Failed to find all in " + databaseName, e);
		} catch (OutOfMemoryError e) {
			_getDatabaseManager().flushMemory(false);
			return getAllUnsyncedToDB();
		} finally {
			timer.stop();
			if (cursor != null) {
				try {
					cursor.close();
				} catch (DatabaseException e) {
					LOGGER.error("failed to close cursor", e);
				}
			}
		}
	}

}
