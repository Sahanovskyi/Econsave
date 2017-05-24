package com.gw.presentation.presenter;

import com.github.mikephil.charting.data.LineData;
import com.gw.domain.exception.DefaultErrorBundle;
import com.gw.domain.exception.ErrorBundle;
import com.gw.domain.interactor.DefaultObserver;
import com.gw.domain.interactor.GetPredictionList;
import com.gw.domain.interactor.forecast.TransactionsManager;
import com.gw.domain.model.Interval;
import com.gw.domain.model.TransactionItem;
import com.gw.domain.model.decision.BuyAsset;
import com.gw.domain.model.decision.Decision;
import com.gw.domain.model.decision.SellAsset;
import com.gw.presentation.exception.ErrorMessageFactory;
import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.presenter.helper.ChartDataManager;
import com.gw.presentation.view.DecisionView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import javax.inject.Inject;

/**
 * Created by vadym on 20.05.17.
 */

@PerActivity
public class DecisionReviewPresenter implements Presenter {
    private DecisionView mDecisionView;
    private ArrayList<TransactionItem> mTransactionsList;
    private TreeMap<Date, Double> mPredictionMap;
    private TreeMap<Date, Double> mBalanceMap;
    private Decision mDecision;
    private TransactionsManager mTransactionsManager;
    private ChartDataManager mChartDataManager;
    private GetPredictionList mGetPredictionList;


    @Inject
    public DecisionReviewPresenter(ChartDataManager chartDataManager, TransactionsManager transactionsManager, GetPredictionList getPredictionList) {
        this.mTransactionsManager = transactionsManager;
        this.mGetPredictionList = getPredictionList;
        this.mChartDataManager = chartDataManager;
    }

    public void setView(DecisionView view){
        this.mDecisionView = view;
    }

    public void setTransactionsList(ArrayList<TransactionItem> list){
        this.mTransactionsList = list;
    }

    public void setDecision(Decision decision){
        this.mDecision = decision;
    }

    public void initialize(){
        getPredictionList();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.mDecisionView = null;
        this.mDecision = null;
        this.mTransactionsList = null;
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.mDecisionView.context(),
                errorBundle.getException());
        this.mDecisionView.showError(errorMessage);
    }

    private void margeMaps(){
        TreeMap<Date, Double> map = this.mTransactionsManager.margeMaps(mPredictionMap, mDecision.getPaymentMap());

        LineData dataBalance = new LineData(
                mChartDataManager.createPredictionLineDataSet(map),
                mChartDataManager.createBalanceLineDataSet(mBalanceMap));

        mDecisionView.updateChart(dataBalance);
    }

    private void getPredictionList(){
        Interval interval;
        Calendar calendar = Calendar.getInstance();
        Date end;
        if(mDecision instanceof BuyAsset || mDecision instanceof SellAsset) {
            interval = Interval.LAST_QUARTER;
            calendar.add(Calendar.MONTH, 4);
            end = calendar.getTime();
        }
        else {
            interval = Interval.LAST_HALF_YEAR;
            end = mDecision.getPaymentMap().lastEntry().getKey();
        }

        Date start = new Date();
        this.mBalanceMap = this.mTransactionsManager.getBalanceMap(mTransactionsList, interval);
        this.mGetPredictionList.execute(
                new DecisionReviewPresenter.PredictionListObserver(),
                GetPredictionList.Params.predictParams(mBalanceMap, start, end));
    }

    private final class PredictionListObserver extends DefaultObserver<TreeMap<Date, Double>> {

        @Override
        public void onComplete() {
            margeMaps();
        }

        @Override
        public void onError(Throwable e) {
            DecisionReviewPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            DecisionReviewPresenter.this.mDecisionView.hideProgressDialog();
        }

        @Override
        public void onNext(TreeMap<Date, Double> predictionItems) {
            mPredictionMap = predictionItems;
        }
    }
}
