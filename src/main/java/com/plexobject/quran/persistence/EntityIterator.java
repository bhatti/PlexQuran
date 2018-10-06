package com.plexobject.quran.persistence;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.plexobject.quran.model.PersistentEntity;

public class EntityIterator<T extends PersistentEntity> implements
        Iterator<List<T>> {
    private final PersistenceStorage<T> persistenceStorage;
    private final int limit;
    private final Long createdAt;
    private Long startKey;
    private PagedList<T> docs;
    private int total;
    private int requests;
    private boolean consumedFirstBatch;

    public EntityIterator(PersistenceStorage<T> persistenceStorage,
            final int limit, final Long createdAt) {
        this(persistenceStorage, new Long(-1), limit, createdAt);
    }

    public EntityIterator(PersistenceStorage<T> persistenceStorage,
            final Long startKey, final int limit, final Long createdAt) {
        this.persistenceStorage = persistenceStorage;
        this.startKey = startKey;
        this.limit = limit;
        this.createdAt = createdAt;
        fetchNext();
    }

    private void fetchNext() {
        final Long endKey = null;
        try {
            docs = persistenceStorage.getAll(startKey, endKey, limit + 1,
                    createdAt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (docs == null) {
            throw new IllegalStateException("null documents");
        }
        startKey = docs.hasMore() ? docs.remove(limit).getId() : null;
        total += docs.size();
        requests++;
    }

    @Override
    public boolean hasNext() {
        return consumedFirstBatch ? docs.hasMore() : docs.size() > 0;
    }

    @Override
    public List<T> next() {
        final List<T> docsToReturn = docs;
        consumedFirstBatch = true;
        if (docs.hasMore()) {
            fetchNext();
        }
        return docsToReturn;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int getTotal() {
        return total;
    }

    public int getNumberOfRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return "EntityIterator " + total + " - startKey " + startKey
                + ", requests " + requests;
    }

}
