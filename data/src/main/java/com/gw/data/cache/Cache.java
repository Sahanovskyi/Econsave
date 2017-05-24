package com.gw.data.cache;


import com.gw.data.entity.TransactionItemEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * An interface representing a user Cache.
 */
public interface Cache {
    /**
     * Gets an {@link Observable} which will emit a {@link List<TransactionItemEntity>}.
     *
     */
    Observable<List<TransactionItemEntity>> get(String fileName);

    /**
     * Puts an element into the cache.
     *
     * @param list Element to insert in the cache.
     */
    void put(List<TransactionItemEntity> list, String fileName);

    /**
     * Checks if an element exists in the cache.
     *
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(String fileName);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired(String fileName);

    /**
     * Evict all elements of the cache.
     */
    void evictAll();
}

