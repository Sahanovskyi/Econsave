package com.gw.domain.interactor;

import com.gw.domain.executor.PostExecutionThread;
import com.gw.domain.executor.ThreadExecutor;
import com.gw.domain.interactor.forecast.PredictionManager;
import com.gw.domain.model.Interval;
import com.gw.domain.model.TransactionItem;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by vadym on 10.05.17.
 */

public class GetPredictionList extends UseCase<TreeMap<Date, Double>, GetPredictionList.Params> {

    private PredictionManager predictionManager;

    @Inject
    GetPredictionList(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread, PredictionManager predictionManager) {
        super(threadExecutor, postExecutionThread);
        this.predictionManager = predictionManager;
    }

    @Override
    Observable<TreeMap<Date, Double>> buildUseCaseObservable(final Params params) {
        Observable<TreeMap<Date, Double>> myObservable = Observable.create(
                new ObservableOnSubscribe<TreeMap<Date, Double>>() {
                    @Override
                    public void subscribe(ObservableEmitter<TreeMap<Date, Double>> e) throws Exception {
                        if (params.incomeMap != null) {
                            e.onNext(predictionManager.predict(params.incomeMap, params.start, params.end));
                            e.onComplete();
                        } else
                            e.onError(new Exception("From GetPredictionList.buildUseCase Exception!"));
                    }
                });

        return myObservable;
    }

    public static final class Params {

        private final TreeMap<Date, Double> incomeMap;
        private final Date start;
        private final Date end;

        private Params(TreeMap<Date, Double> incomeMap, Date start, Date end) {
            this.incomeMap = incomeMap;
            this.start = start;
            this.end = end;
        }

        public static Params predictParams(TreeMap<Date, Double> incomeList, Date start, Date end) {
            return new Params(incomeList, start, end);
        }

    }
}
