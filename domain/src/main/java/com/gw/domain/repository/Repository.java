package com.gw.domain.repository;

import com.gw.domain.model.DecisionItem;
import com.gw.domain.model.PrivatBank.PrivatBankClient;
import com.gw.domain.model.TransactionItem;


import java.util.List;

import io.reactivex.Observable;


/**
 * Interface that represents a Repository for getting {@link TransactionItem} related data.
 */
public interface Repository {
    /**
     * Get an {@link Observable} which will emit a List of {@link TransactionItem}.
     *
     * @param client provide PrivatBank client information.
     */
    Observable<List<TransactionItem>> getPrivatBankTransactionItems(PrivatBankClient client);

    Observable<List<TransactionItem>> getSmsTransactionItems(String number);

    Observable<List<DecisionItem>> getDecisions();

}