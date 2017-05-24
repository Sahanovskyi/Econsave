package com.gw.data.net;

import com.gw.data.entity.TransactionItemEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * ServerApi for communication with the server.
 */

public interface ServerApi {
    String API_BASE_URL =
            "https://graduation-work-33.appspot.com/";

    /**
     * Retrieves an {@link Observable} which will emit a List of {@link TransactionItemEntity}.
     */
    Observable<List<TransactionItemEntity>> transactionItemEntityList();

}
