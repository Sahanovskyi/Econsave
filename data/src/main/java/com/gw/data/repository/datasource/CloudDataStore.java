package com.gw.data.repository.datasource;

import com.gw.data.cache.Cache;
import com.gw.data.entity.TransactionItemEntity;
import com.gw.data.net.ServerApi;
import com.gw.domain.model.PrivatBank.PrivatBankClient;

import java.util.List;

import io.reactivex.Observable;

/**
 * {@link DataStore} implementation based on connections to the api (Cloud).
 */
class CloudDataStore implements DataStore {

    public static final String CACHE_FILE_NAME = "PrivatBank";
    private final ServerApi serverApi;
    private final Cache cache;

    /**
     * Construct a {@link DataStore} based on connections to the api (Cloud).
     *
     * @param serverApi The {@link ServerApi} implementation to use.
     * @param cache A {@link Cache} to cache data retrieved from the api.
     */
    CloudDataStore(ServerApi serverApi, Cache cache) {
        this.serverApi = serverApi;
        this.cache = cache;
    }

    @Override public Observable<List<TransactionItemEntity>> transactionItemEntityList() {
        return this.serverApi.transactionItemEntityList().doOnNext((list) -> CloudDataStore.this.cache.put(list, CACHE_FILE_NAME));
    }

}
