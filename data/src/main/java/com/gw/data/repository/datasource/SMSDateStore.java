package com.gw.data.repository.datasource;

import android.content.Context;

import com.gw.data.cache.Cache;
import com.gw.data.entity.TransactionItemEntity;
import com.gw.data.net.SMSReader;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


class SMSDateStore implements DataStore {

    public static final String CACHE_FILE_NAME = "SMS";

    private final Cache cache;
    private final SMSReader smsReader;
    private final String number;


    /**
     * Construct a {@link DataStore} based file system data store.
     *
     * @param cache A {@link Cache} to cache data retrieved from the api.
     */
    public SMSDateStore(SMSReader smsReader, String number, Cache cache) {
        this.cache = cache;
        this.smsReader = smsReader;
        this.number = number;
    }

    @Override
    public Observable<List<TransactionItemEntity>> transactionItemEntityList() {

        return smsReader.transactionItemEntityList().doOnNext((list) -> SMSDateStore.this.cache.put(list, (CACHE_FILE_NAME + number)));
    }
}
