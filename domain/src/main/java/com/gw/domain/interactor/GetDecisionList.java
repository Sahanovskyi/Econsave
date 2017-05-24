package com.gw.domain.interactor;

import com.gw.domain.executor.PostExecutionThread;
import com.gw.domain.executor.ThreadExecutor;
import com.gw.domain.model.DecisionItem;
import com.gw.domain.repository.Repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by vadym on 10.05.17.
 */

public class GetDecisionList extends UseCase<List<DecisionItem>, Void> {


    private final Repository userRepository;

    @Inject
    GetDecisionList(Repository userRepository, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    @Override
    Observable<List<DecisionItem>> buildUseCaseObservable(Void unused) {
        return this.userRepository.getDecisions();
    }
}
