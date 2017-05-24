package com.gw.data.repository;

import com.gw.data.entity.DecisionEntity;
import com.gw.data.entity.mapper.DecisionEntityMapper;
import com.gw.data.entity.mapper.TransactionItemEntityDataMapper;
import com.gw.data.exception.CacheReadException;
import com.gw.data.repository.datasource.DataStore;
import com.gw.data.repository.datasource.DataStoreFactory;
import com.gw.domain.model.DecisionItem;
import com.gw.domain.model.PrivatBank.PrivatBankClient;
import com.gw.domain.model.TransactionItem;
import com.gw.domain.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link Repository} for retrieving user data.
 */
@Singleton
public class DataRepository implements Repository {
    private final DataStoreFactory dataStoreFactory;
    private final TransactionItemEntityDataMapper transactionItemEntityDataMapper;
    private final DecisionEntityMapper decisionEntityMapper;

    /**
     * Constructs a {@link Repository}.
     *
     * @param dataStoreFactory A factory to construct different data source implementations.
     * @param transactionItemEntityDataMapper {@link TransactionItemEntityDataMapper}.
     */
    @Inject
    public DataRepository(DataStoreFactory dataStoreFactory, TransactionItemEntityDataMapper transactionItemEntityDataMapper, DecisionEntityMapper decisionEntityMapper) {
        this.dataStoreFactory = dataStoreFactory;
        this.transactionItemEntityDataMapper = transactionItemEntityDataMapper;
        this.decisionEntityMapper = decisionEntityMapper;
    }

    @Override
    public Observable<List<TransactionItem>> getPrivatBankTransactionItems(PrivatBankClient client) {
        final DataStore dataStore = this.dataStoreFactory.createCloudDataStore(client);
        return dataStore.transactionItemEntityList().map(this.transactionItemEntityDataMapper::transform);

    }

    @Override
    public Observable<List<TransactionItem>> getSmsTransactionItems(String number) {
        final DataStore dataStore = this.dataStoreFactory.createSMSDataStore(number);
        return dataStore.transactionItemEntityList().map(this.transactionItemEntityDataMapper::transform);

    }

    @Override
    public Observable<List<DecisionItem>> getDecisions() {

        return Observable.create(emitter -> {
            final List<DecisionEntity> decisionEntityList = new ArrayList<>();
            decisionEntityList.add(new DecisionEntity(1, "Купити квартиру", new String[]{"покупка", "нерухомість", "житло"}));
            decisionEntityList.add(new DecisionEntity(2, "Купити машину", new String[]{"покупка", "транспорт", "автомобіль"}));
            decisionEntityList.add(new DecisionEntity(3, "Купити цінні папери", new String[]{"покупка", "інвестиції", "документи", "цінні_папери"}));
            decisionEntityList.add(new DecisionEntity(4, "Продати будинок", new String[]{"продаж", "нерухомість", "житло"}));
            decisionEntityList.add(new DecisionEntity(5, "Взяти кредит", new String[]{"валюта", "кредит"}));
            decisionEntityList.add(new DecisionEntity(6, "Відкрити депозит", new String[]{"валюта", "депозит"}));
            decisionEntityList.add(new DecisionEntity(7, "Купити будинок", new String[]{"покупка", "нерухомість", "житло", "будинок"}));
            decisionEntityList.add(new DecisionEntity(8, "Продати машину", new String[]{"продаж", "транспорт", "автомобіль"}));
            decisionEntityList.add(new DecisionEntity(9, "Продати велосипед", new String[]{"покупка", "транспорт", "велосипед"}));
            decisionEntityList.add(new DecisionEntity(9, "Купити побутову техніку", new String[]{"покупка", "техніка", "побут"}));
            decisionEntityList.add(new DecisionEntity(10, "Купити телефон", new String[]{"покупка", "електроніка", "телефон"}));
            decisionEntityList.add(new DecisionEntity(11, "Прдати оутбук", new String[]{"продаж", "електроніка", "ноутбук"}));



            if (decisionEntityList != null) {
                emitter.onNext(decisionEntityMapper.transform(decisionEntityList));
                emitter.onComplete();
            } else {
                emitter.onError(new CacheReadException());
            }
        });
    }
}
