package com.gw.domain.interactor;

import com.fernandocejas.arrow.checks.Preconditions;
import com.gw.domain.executor.PostExecutionThread;
import com.gw.domain.executor.ThreadExecutor;
import com.gw.domain.model.TransactionItem;
import com.gw.domain.repository.Repository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


public class GetSmsTransactionList extends UseCase<List<TransactionItem>, String> {
    private final Repository userRepository;

    @Inject
    GetSmsTransactionList(Repository userRepository, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    @Override
    Observable<List<TransactionItem>> buildUseCaseObservable(String number) {
        Preconditions.checkNotNull(number);
        return this.userRepository.getSmsTransactionItems(number);
    }
}
