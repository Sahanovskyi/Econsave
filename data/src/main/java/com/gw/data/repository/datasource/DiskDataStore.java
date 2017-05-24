package com.gw.data.repository.datasource;

import com.gw.data.cache.Cache;
import com.gw.data.entity.TransactionItemEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link DataStore} implementation based on file system data store.
 */
class DiskDataStore implements DataStore {

    private final Cache cache;

    private String cacheFileName;
    /**
     * Construct a {@link DataStore} based file system data store.
     *
     * @param cache A {@link Cache} to cache data retrieved from the api.
     */
    DiskDataStore(Cache cache, String cacheFileName) {
        this.cache = cache;
        this.cacheFileName = cacheFileName;
    }

    @Override public Observable<List<TransactionItemEntity>> transactionItemEntityList() {
        return this.cache.get(cacheFileName);
    }
}
