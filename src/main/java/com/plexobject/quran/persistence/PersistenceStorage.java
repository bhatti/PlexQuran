package com.plexobject.quran.persistence;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.plexobject.quran.model.PersistentEntity;

public interface PersistenceStorage<T extends PersistentEntity> {
	public T save(T entity) throws IOException;

	public T get(Long key) throws IOException;

	public boolean remove(Long key) throws IOException;

	public Collection<T> getAll(final Long startKey, final int limit,
			final Long createdDateGreaterThan) throws IOException;

	public Collection<T> getAllUnsyncedToDB() throws IOException;

	public Collection<T> getByHashCode(final int hashCode) throws IOException;

	public PagedList<T> getAll(final Long startKey, final Long endKey,
			final int limit, final Long createdDateGreaterThan)
			throws IOException;

	public Map<Long, T> get(final Long... ids) throws IOException;

	public Map<Long, T> get(Collection<Long> ids) throws IOException;

	public void close() throws IOException;

	public DatabaseManager getDatabaseManager() throws IOException;
}
