package com.gw.presentation.presenter;

import com.github.mikephil.charting.data.LineData;
import com.gw.domain.exception.DefaultErrorBundle;
import com.gw.domain.exception.ErrorBundle;
import com.gw.domain.interactor.DefaultObserver;
import com.gw.domain.interactor.GetPredictionList;
import com.gw.domain.interactor.forecast.TransactionsManager;
import com.gw.domain.model.Interval;
import com.gw.domain.model.Period;
import com.gw.domain.model.TransactionItem;
import com.gw.presentation.R;
import com.gw.presentation.exception.ErrorMessageFactory;
import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.presenter.helper.ChartDataManager;
import com.gw.presentation.view.ForecastView;
import com.gw.presentation.view.activity.ForecastActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import javax.inject.Inject;

@PerActivity
public class ForecastPresenter implements Presenter {

    private final GetPredictionList mGetPredictionList;
    private final ChartDataManager mChartDataManager;
    private final TransactionsManager mTransactionsManager;

    private ForecastView mForecastView;

    private ArrayList<TransactionItem> mTransactionItems;

    private TreeMap<Date, Double> mBalancePredictionMap;
    private TreeMap<Date, Double> mIncomePredictionMap;
    private TreeMap<Date, Double> mExpensesPredictionMap;
    private TreeMap<Date, Double> mIndexPredictionMap;
    private Interval mInterval;
    private Period mPeriod;
    private int mGotCount = 0;

    @Inject
    public ForecastPresenter(TransactionsManager transactionsManager,
                             GetPredictionList getPredictionList,
                             ChartDataManager chartDataManager) {

        this.mGetPredictionList = getPredictionList;
        this.mChartDataManager = chartDataManager;
        this.mTransactionsManager = transactionsManager;
        this.mInterval = Interval.LAST_HALF_YEAR;
        this.mPeriod = Period.WEEK;
    }

    public void changePredictionInterval(Interval interval){
        mInterval = interval;
        getPredictionLists();
    }

    public void changeGroupingPeriod(Period period){
        this.mPeriod = period;
        getPredictionLists();
    }

    public void setView(ForecastView view) {
        this.mForecastView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.mGetPredictionList.dispose();
        this.mForecastView = null;
        this.mBalancePredictionMap = null;
        this.mIncomePredictionMap = null;
        this.mExpensesPredictionMap = null;
        this.mIndexPredictionMap = null;
        this.mTransactionItems = null;
    }

    public void initialize() {
        if (mTransactionItems == null)
            mTransactionItems = new ArrayList<>();
        mBalancePredictionMap = new TreeMap<>();
        mIncomePredictionMap = new TreeMap<>();
        mExpensesPredictionMap = new TreeMap<>();
        mIndexPredictionMap = new TreeMap<>();
        getPredictionLists();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.mForecastView.context(),
                errorBundle.getException());
        this.mForecastView.showError(errorMessage);
    }

    private void updateUI() {
        TreeMap<Date, Double> balanceMap = mTransactionsManager.getBalanceMap(mTransactionItems, mInterval);
        TreeMap<Date, Double> incomeMap = mTransactionsManager.groupIncome(mTransactionsManager.getIncomeMap(mTransactionItems, mInterval), mPeriod);
        TreeMap<Date, Double> expensesMap = mTransactionsManager.groupExpenses(mTransactionsManager.getExpensesMap(mTransactionItems, mInterval), mPeriod);

        LineData dataBalance = new LineData(
                mChartDataManager.createPredictionLineDataSet(mBalancePredictionMap),
                mChartDataManager.createBalanceLineDataSet(balanceMap));

        LineData dataIncome = new LineData(
                mChartDataManager.createPredictionLineDataSet(mIncomePredictionMap),
                mChartDataManager.createIncomeLineDataSet(
                        mTransactionsManager.groupIncome(incomeMap, mPeriod)));

        LineData dataExpenses = new LineData(
                mChartDataManager.createPredictionLineDataSet(mExpensesPredictionMap),
                mChartDataManager.createExpenseLineData(
                        mTransactionsManager.groupExpenses(expensesMap, mPeriod)));

        mForecastView.updateTabChart(dataBalance, ForecastActivity.TabName.BALANCE);
        mForecastView.updateTabChart(dataIncome, ForecastActivity.TabName.INCOME);
        mForecastView.updateTabChart(dataExpenses, ForecastActivity.TabName.EXPENSES);

        double incomeCurrent = mTransactionsManager.getAverageIncome(incomeMap);
        double incomeExpected = mTransactionsManager.getAverageIncome(mIncomePredictionMap);

        double balanceCurrent = balanceMap.lastEntry().getValue();
        double balanceExpected = mBalancePredictionMap.lastEntry().getValue();

        double expensesCurrent = mTransactionsManager.getAverageExpenses(expensesMap);
        double expensesExpected = mTransactionsManager.getAverageExpenses(mExpensesPredictionMap);


        double income_trend = (mIncomePredictionMap.lastEntry().getValue() - mIncomePredictionMap.firstEntry().getValue()) * 100 / mIncomePredictionMap.firstEntry().getValue();
        double balance_trend = (mBalancePredictionMap.lastEntry().getValue() - mBalancePredictionMap.firstEntry().getValue()) * 100 / mBalancePredictionMap.firstEntry().getValue();
        double expenses_trend = (mExpensesPredictionMap.lastEntry().getValue() - mExpensesPredictionMap.firstEntry().getValue()) * 100 / mExpensesPredictionMap.firstEntry().getValue();


        mForecastView.updateTabData(incomeCurrent, incomeExpected, income_trend, mForecastView.context().getString(R.string.tip_entertainment), ForecastActivity.TabName.INCOME);
        mForecastView.updateTabData(balanceCurrent, balanceExpected, balance_trend, mForecastView.context().getString(R.string.tip_entertainment), ForecastActivity.TabName.BALANCE);
        mForecastView.updateTabData(expensesCurrent, expensesExpected, expenses_trend, mForecastView.context().getString(R.string.tip_entertainment), ForecastActivity.TabName.EXPENSES);
   //     mForecastView.updateTabData(,,, ForecastActivity.TabName.INDEX);
    }

    public void setmTransactionItems(ArrayList<TransactionItem> mTransactionItems) {
        this.mTransactionItems = mTransactionItems;
    }
//
//    private void getTransactionHistory() {
//
//        mForecastView.showProgressDialog();
//        User user = new User("Olya", "Sahanovska", "vad.sagan@ukr.net",
//                new PrivatBankClient("4149497820710905", 127246, "fBE6F19MGT2KgxqPQT5fMvhd3pO3yqNH"));
//
//   //     this.mForecastView.showToastMessage("Getting data...");
//   //     this.getPrivatBankTransactionList.execute(new ForecastPresenter.TransactionListObserver("PrivatBank"), user.getPrivatBankClient());
//     //   this.getSmsTransactionList.execute(new TransactionListObserver("Ukrsotsbank"), "Ukrsotsbank");
//    }

    private void getPredictionLists() {
        if (mTransactionItems != null) {
      //      this.mForecastView.showProgressDialog();
            this.mGotCount = 0;
            Date start = new Date();
            Date end = TransactionsManager.getEndDate(mInterval);


            this.mGetPredictionList.execute(
                    new ForecastPresenter.PredictionListObserver(mBalancePredictionMap),
                    GetPredictionList.Params.predictParams(
                            this.mTransactionsManager.getBalanceMap(mTransactionItems, mInterval), start, end));

            this.mGetPredictionList.execute(
                    new ForecastPresenter.PredictionListObserver(mIncomePredictionMap),
                    GetPredictionList.Params.predictParams(
                            this.mTransactionsManager.groupIncome(
                            this.mTransactionsManager.getIncomeMap(mTransactionItems, mInterval), mPeriod),  start, end));

            this.mGetPredictionList.execute(
                    new ForecastPresenter.PredictionListObserver(mExpensesPredictionMap),
                    GetPredictionList.Params.predictParams(
                            this.mTransactionsManager.groupExpenses(
                            this.mTransactionsManager.getExpensesMap(mTransactionItems, mInterval), mPeriod),  start, end));
//
//        this.getPredictionLists.execute(
//                new ForecastPresenter.PredictionListObserver(),
//                GetPredictionList.Params.predictParams(
//                        list, Interval.LAST_QUARTER, GetPredictionList.Params.PredictionSource.BALANCE));
        }
    }

    private final class PredictionListObserver extends DefaultObserver<TreeMap<Date, Double>> {
        private TreeMap<Date, Double> transactionSource;

        PredictionListObserver(TreeMap<Date, Double> transactionSource) {
            this.transactionSource = transactionSource;
        }

        @Override
        public void onComplete() {
            //       ForecastPresenter.this.mForecastView.showToastMessage("Got prediction history!");
            if (++mGotCount == 3) {
                updateUI();
                mGotCount = 0;
                ForecastPresenter.this.mForecastView.hideProgressDialog();
            }
        }

        @Override
        public void onError(Throwable e) {
            ForecastPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            ForecastPresenter.this.mForecastView.hideProgressDialog();
        }

        @Override
        public void onNext(TreeMap<Date, Double> predictionItems) {
            transactionSource.clear();
            transactionSource.putAll(predictionItems);
        }
    }
//
//
//    private final class TransactionListObserver extends DefaultObserver<List<TransactionItem>> {
//        private String transactionSource;
//        public TransactionListObserver(String transactionSource) {
//            this.transactionSource = transactionSource;
//        }
//
//        @Override
//        public void onComplete() {
//            ForecastPresenter.this.mForecastView.showToastMessage("Got transaction history!");
//            getPredictionLists();
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            ForecastPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
//            ForecastPresenter.this.mForecastView.hideProgressDialog();
//        }
//
//        @Override
//        public void onNext(List<TransactionItem> mTransactionItems) {
//            ForecastPresenter.this.mTransactionItems.clear();
//            ForecastPresenter.this.mTransactionItems.addAll(mTransactionItems);
//
//        }
//    }


}
