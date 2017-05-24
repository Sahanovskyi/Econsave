package com.gw.data.repository.datasource;


import android.content.Context;
import android.support.annotation.NonNull;

import com.gw.data.cache.Cache;
import com.gw.data.entity.mapper.TransactionItemEntitySMSMapper;
import com.gw.data.entity.mapper.TransactionItemEntityXMLMapper;
import com.gw.data.net.SMSReader;
import com.gw.data.net.ServerApi;
import com.gw.data.net.ServerApiImpl;
import com.gw.domain.model.PrivatBank.PrivatBankClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Factory that creates different implementations of {@link DataStore}.
 */
@Singleton
public class DataStoreFactory {

    private final Context context;
    private final Cache cache;

    @Inject
    DataStoreFactory(@NonNull Context context, @NonNull Cache cache) {
        this.context = context.getApplicationContext();
        this.cache = cache;
    }

    /**
     * Create {@link DataStore} to retrieve data.
     */
    public DataStore create(PrivatBankClient client) {
        DataStore dataStore;

        if (!this.cache.isExpired(CloudDataStore.CACHE_FILE_NAME) && this.cache.isCached(CloudDataStore.CACHE_FILE_NAME)) {
            dataStore = createDiskDataStore();
        } else {
            dataStore = createCloudDataStore(client);
        }
        return dataStore;

    }


    /**
     * Create {@link DataStore} to retrieve data from the cache.
     */
    public DataStore createDiskDataStore() {
        return new DiskDataStore(this.cache, CloudDataStore.CACHE_FILE_NAME);
    }

    /**
     * Create {@link DataStore} to retrieve data from the Cloud.
     */
    public DataStore createCloudDataStore(PrivatBankClient client) {

        if (this.cache.isExpired(CloudDataStore.CACHE_FILE_NAME) && !this.cache.isCached(CloudDataStore.CACHE_FILE_NAME)) {

            final TransactionItemEntityXMLMapper entityXMLMapper = new TransactionItemEntityXMLMapper();
            final ServerApi serverApi = new ServerApiImpl(this.context, entityXMLMapper, client);

            return new CloudDataStore(serverApi, this.cache);
        } else {
            return new DiskDataStore(this.cache, CloudDataStore.CACHE_FILE_NAME);
        }
    }

    public DataStore createSMSDataStore(String number) {
        if (this.cache.isExpired(SMSDateStore.CACHE_FILE_NAME + number) && !this.cache.isCached(SMSDateStore.CACHE_FILE_NAME + number)) {

            final TransactionItemEntitySMSMapper entitySMSMapper = new TransactionItemEntitySMSMapper();
            final SMSReader smsReader = new SMSReader(this.context, entitySMSMapper, number);
            return new SMSDateStore(smsReader, number, this.cache);
        }else {
            return new DiskDataStore(this.cache, SMSDateStore.CACHE_FILE_NAME + number);
        }
    }
}
