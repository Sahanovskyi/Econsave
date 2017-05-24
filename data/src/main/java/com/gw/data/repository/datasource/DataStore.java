package com.gw.data.repository.datasource;

import com.gw.data.entity.TransactionItemEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface DataStore {
    /**
     * Get an {@link Observable} which will emit a List of {@link TransactionItemEntity}.
     */
    Observable<List<TransactionItemEntity>> transactionItemEntityList();

//    /**
//     * Get an {@link Observable} which will emit a {@link TransactionItemEntity} by its id.
//     *
//     * @param userId The id to retrieve user data.
//     */
//    Observable<TransactionItemEntity> userEntityDetails(final int userId);
}

