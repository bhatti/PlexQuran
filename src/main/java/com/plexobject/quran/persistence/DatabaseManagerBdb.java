package com.plexobject.quran.persistence;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.config.Configuration;
import com.plexobject.quran.listeners.ApplicationLifecycleListener;
import com.plexobject.quran.metrics.Metric;
import com.plexobject.quran.metrics.Timer;
import com.plexobject.quran.model.PersistentEntity;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Durability;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.evolve.IncompatibleClassException;

public class DatabaseManagerBdb implements DatabaseManager {
	private static final Logger LOGGER = Logger
			.getLogger(DatabaseManagerBdb.class);
	public static final String DB_DIR = Configuration.getInstance()
			.getProperty("bdb_docs.dir", getDefaultBdbDir());

	private final File databaseDir;
	private Environment env;
	private StoreConfig storeConfig;

	private Map<String, PersistenceStorage<? extends PersistentEntity>> storages = Collections
			.synchronizedMap(new HashMap<String, PersistenceStorage<? extends PersistentEntity>>());
	private Map<String, Database> databases = Collections
			.synchronizedMap(new HashMap<String, Database>());

	DatabaseManagerBdb() throws IOException {
		this(DB_DIR);
	}

	DatabaseManagerBdb(final String databaseDir) throws IOException {
		this(new File(databaseDir));
	}

	DatabaseManagerBdb(final File databaseDir) throws IOException {
		if (databaseDir == null) {
			throw new NullPointerException("databaseDir is not specified");
		}
		this.databaseDir = databaseDir;
		open();
		ApplicationDelegate.getInstance().addApplicationLifecycleListener(
				new ApplicationLifecycleListener() {

					@Override
					public void applicationStarted() {
					}

					@Override
					public void applicationStopped() {
						try {
							close();
						} catch (Exception e) {
							LOGGER.info("Failed to close database", e);
						}
					}
				});
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					close();
				} catch (Exception e) {
					LOGGER.info("Failed to close database", e);
				}
			}
		});
	}

	@Override
	public String[] getAllDatabases() throws IOException {
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getAllDatabases");
		try {
			List<String> dbList = null;
			synchronized (env) {
				dbList = env.getDatabaseNames();
			}
			String[] dbNames = new String[dbList.size()];
			for (int i = 0; i < dbList.size(); i++) {
				dbNames[i] = dbList.get(i);
			}
			return dbNames;
		} catch (EnvironmentLockedException e) {
			throw new IOException(e);
		} catch (DatabaseException e) {
			throw new IOException(e);
		} finally {
			timer.stop();
		}
	}

	@Override
	public boolean deleteDatabase(String dbname) throws IOException {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("database name not specified");
		}
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.deleteDatabase");
		if (!existsDatabase(dbname)) {
			return false;
		}
		// final Database database = getDatabase(dbname);
		// if (database != null) {
		// try {
		// database.close();
		// } catch (DatabaseException e) {
		// throw new IOException(e);
		// }
		// }
		removeDatabase(dbname);
		timer.stop();
		return true;
	}

	@Override
	public boolean closeDatabase(String dbname) throws IOException {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("database name not specified");
		}
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.closeeDatabase");
		if (!existsDatabase(dbname)) {
			return false;
		}

		_closeDatabase(dbname);
		timer.stop();
		return true;
	}

	@Override
	public Map<String, String> getInfo(String database) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		info.put("db_name", database);
		info.put("count", String.valueOf(count(database)));
		return info;
	}

	boolean open() throws IOException {
		return _open(0);
	}

	synchronized boolean _open(int retries) throws IOException {
		if (env != null) {
			return false;
		}
		@SuppressWarnings("unused")
		Durability defaultDurability = new Durability(
				Durability.SyncPolicy.SYNC, null, // unused by non-HA
				// applications.
				null); // unused by non-HA applications.

		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(false);
		envConfig.setSharedCache(true);
		// envConfig.setDurability(defaultDurability);
		envConfig.setCacheSize(1000000);

		// envConfig.setReadOnly(true);
		// envConfig.setTxnTimeout(1000000);
		if (!databaseDir.exists()) {
			databaseDir.mkdirs();
		}

		final DatabaseConfig dbConfig = new DatabaseConfig();
		envConfig.setTransactional(false);
		dbConfig.setAllowCreate(true);
		dbConfig.setReadOnly(false);
		// dbConfig.setInitializeLocking(true);
		// dbConfig.setType(DatabaseType.BTREE);

		// envConfig.setInitializeCache(true);
		// envConfig.setInitializeLocking(true);
		// envConfig.setInitializeLogging(true);
		// envConfig.setThreaded(true);

		try {
			this.env = new Environment(databaseDir, envConfig);
		} catch (EnvironmentLockedException e) {
			if (retries == 0) {
				File lockDir = new File(DB_DIR);
				File[] locks = lockDir.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".lck");
					}
				});
				for (File lock : locks) {
					lock.delete();
					LOGGER.fatal("Deleting lock file " + lock);
				}
				return _open(retries + 1);
			}
			// ApplicationDelegate
			// .getInstance()
			// .fireException(
			// "Another instance of application is running or was unexpectedly crashed, please remove the lock file je.lck in "
			// + DB_DIR, e);
			LOGGER.fatal("Another instance of the application is running", e);
			// return false;
			throw new Error(
					"Another instance of application is running or was unexpectedly crashed, please remove the lock file je.lck in "
							+ DB_DIR + " or kill other instance (java)");
		} catch (DatabaseException e) {
			throw new IOException(e);
		}
		storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setDeferredWrite(false);

		// writer.optimize();
		// javaCatalog = new StoredClassCatalog(database);
		return true;
	}

	synchronized void flushMemory(boolean force) throws IOException {
		try {
			env.evictMemory();
		} catch (DatabaseException e) {
		}
		if (force) {
			close();
			open();
		}
	}

	synchronized void sync() {
		env.sync();
	}

	public synchronized boolean close() {
		if (env == null) {
			LOGGER.warn("already closed");
			return false;
		}
		try {
			env.sync();
		} catch (IllegalStateException e) {
		} catch (DatabaseException e) {
			LOGGER.error("Failed to sync BDB environment", e);
		}
		for (Database database : databases.values()) {
			try {
				database.close();
			} catch (DatabaseException e) {
				LOGGER.error("Failed to close " + database, e);
			} catch (IllegalStateException e) {
			}
		}
		for (PersistenceStorage<? extends PersistentEntity> storage : storages
				.values()) {
			try {
				storage.close();
			} catch (IOException e) {
				LOGGER.error("Failed to close " + storage, e);
			}
		}

		try {
			env.close();
			env = null;
			storages.clear();
			databases.clear();
			LOGGER.info("closed databases");
			return true;
		} catch (IllegalStateException e) {
			return true;
		} catch (DatabaseException e) {
			LOGGER.error("Failed to close BDB environment", e);
			return false;
		}
	}

	private synchronized void _closeDatabase(final String dbname)
			throws IOException {
		synchronized (dbname.intern()) {
			Database database = databases.remove(dbname);
			if (database != null) {
				try {
					database.close();
					LOGGER.info("#### closed database " + dbname);
				} catch (IllegalStateException e) {
					// already closed
				} catch (DatabaseException e) {
					LOGGER.warn("Failed to close store " + dbname + ": " + e);
				}
			}
			PersistenceStorage<? extends PersistentEntity> storage = storages
					.remove(dbname);
			if (storage != null) {
				try {
					storage.close();
				} catch (IOException e) {
					LOGGER.error("Failed to close " + storage, e);
				}
			}
		}
	}

	private synchronized void removeDatabase(final String dbname)
			throws IOException {
		synchronized (dbname.intern()) {
			_closeDatabase(dbname);
			try {
				env.removeDatabase(null, dbname);
			} catch (DatabaseException e) {
				throw new IOException("Failed to remove " + dbname, e);
			}
		}
	}

	private boolean existsDatabase(String dbName) throws IOException {
		for (String nextDB : getAllDatabases()) {
			if (nextDB.equals(dbName)) {
				return true;
			}
		}
		return false;
	}

	public long count(final String dbname) throws IOException {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("database name not specified");
		}
		try {
			return getDatabase(dbname).count();
		} catch (DatabaseException e) {
			throw new IOException("Failed to count " + dbname, e);
		}
	}

	EntityStore newEntityStore(String dbname) throws IOException {
		return _newEntityStore(dbname, 0);
	}

	synchronized EntityStore _newEntityStore(String dbname, int retries)
			throws IOException {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("database name not specified");
		}
		if (env == null) {
			open();
		}

		getDatabase(dbname);
		try {
			return new EntityStore(env, dbname, storeConfig);
		} catch (IncompatibleClassException e) {
			try {
				close();
			} catch (Exception ee) {
			}
			LOGGER.fatal("Found incompatible database, delete " + DB_DIR
					+ " and restart it");
			if (retries == 0) {
				File dir = new File(DB_DIR);
				FileUtils.deleteDirectory(dir);
				open();
				return _newEntityStore(dbname, retries + 1);
			}
			throw new Error("Found incompatible database, please delete "
					+ DB_DIR + " folder and restart Plexquran application.");
		} catch (IllegalStateException e) {
			try {
				close();
			} catch (Exception ee) {
			}
			LOGGER.fatal("Found incompatible database, delete " + DB_DIR
					+ " and restart it");
			if (retries == 0) {
				open();
				return _newEntityStore(dbname, retries + 1);
			}
			throw e;
		}
	}

	Database getDatabase(final String dbname) throws IOException {
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.getDatabase");
		synchronized (dbname.intern()) {
			Database database = databases.get(dbname);
			if (database != null) {
				return database;
			}

			try {
				DatabaseConfig dbconfig = new DatabaseConfig();
				dbconfig.setAllowCreate(true);
				dbconfig.setSortedDuplicates(false);
				dbconfig.setExclusiveCreate(false);
				dbconfig.setReadOnly(false);
				dbconfig.setTransactional(false);
				dbconfig.setDeferredWrite(false);
				database = env.openDatabase(null, dbname, dbconfig);
				databases.put(dbname, database);
				return database;
			} catch (DatabaseException e) {
				throw new IOException("Failed to open " + dbname, e);
			} finally {
				timer.stop();
			}
		}
	}

	/**
	 * Return the key of the last record entered in the database
	 * 
	 * @return String Numeric value of next key
	 */

	Long getNextKey(final String dbname) throws IOException {
		return System.currentTimeMillis();
		// synchronized (dbname.intern()) {
		// try {
		// long lastKey = 0;
		// Cursor cursor = null;
		// cursor = getDatabase(dbname).openCursor(null, null);
		// DatabaseEntry key = new DatabaseEntry();
		// DatabaseEntry data = new DatabaseEntry();
		// if (cursor.getLast(key, data, LockMode.DEFAULT) ==
		// OperationStatus.SUCCESS) {
		// lastKey = Long.parseLong(new String(key.getData()));
		// }
		// cursor.close();
		//
		// return lastKey + 1;
		// } catch (DatabaseException e) {
		// throw new IOException("Failed to get last key for " + dbname, e);
		// }
		// }
	}

	SerialBinding<PersistentEntity> getPersistentEntityBinding(Database database)
			throws IllegalArgumentException, DatabaseException {
		return new SerialBinding<PersistentEntity>(new StoredClassCatalog(
				database), PersistentEntity.class);
	}

	SerialBinding<String> getKeyBinding(Database database)
			throws IllegalArgumentException, DatabaseException {
		return new SerialBinding<String>(new StoredClassCatalog(database),
				String.class);
	}

	synchronized boolean createDatabase(String dbname) throws IOException {
		if (GenericValidator.isBlankOrNull(dbname)) {
			throw new IllegalArgumentException("database name not specified");
		}
		final Timer timer = Metric
				.newTimer("PersistenceStorageBdb.createDatabase");
		try {
			return getDatabase(dbname) != null;
		} finally {
			timer.stop();
		}
	}

	public static boolean removeLockDirectory() {
		File lock = new File(new File(getDefaultBdbDir()), "je.lck");
		return lock.delete();
	}

	static String getDefaultBdbDir() {
		String bdbDir = Configuration.getInstance().getProperty("bdb_dir");
		if (bdbDir == null) {
			bdbDir = System.getProperty("user.home");
		}
		return new File(bdbDir, ".plexquran").getAbsolutePath();
	}
}
