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

    private final GetPredictionList getPredictionList;
    private final ChartDataManager chartDataManager;
    private final TransactionsManager transactionsManager;

    private ForecastView forecastView;

    private ArrayList<TransactionItem> transactionItems;

    private TreeMap<Date, Double> balancePredictionMap;
    private TreeMap<Date, Double> incomePredictionMap;
    private TreeMap<Date, Double> expensesPredictionMap;
    private TreeMap<Date, Double> indexPredictionMap;
    private Interval mInterval;
    private Period mPeriod;
    private int gotCount = 0;

    @Inject
    public ForecastPresenter(TransactionsManager transactionsManager,
                             GetPredictionList getPredictionList,
                             ChartDataManager chartDataManager) {

        this.getPredictionList = getPredictionList;
        this.chartDataManager = chartDataManager;
        this.transactionsManager = transactionsManager;
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
        this.forecastView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.getPredictionList.dispose();
        this.forecastView = null;
        this.balancePredictionMap = null;
        this.incomePredictionMap = null;
        this.expensesPredictionMap = null;
        this.indexPredictionMap = null;
        this.transactionItems = null;
    }

    public void initialize() {
        if (transactionItems == null)
            transactionItems = new ArrayList<>();
        balancePredictionMap = new TreeMap<>();
        incomePredictionMap = new TreeMap<>();
        expensesPredictionMap = new TreeMap<>();
        indexPredictionMap = new TreeMap<>();
        getPredictionLists();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.forecastView.context(),
                errorBundle.getException());
        this.forecastView.showError(errorMessage);
    }

    private void updateUI() {
        TreeMap<Date, Double> balanceMap = transactionsManager.getBalanceMap(transactionItems, mInterval);
        TreeMap<Date, Double> incomeMap = transactionsManager.groupIncome(transactionsManager.getIncomeMap(transactionItems, mInterval), mPeriod);
        TreeMap<Date, Double> expensesMap = transactionsManager.groupExpenses(transactionsManager.getExpensesMap(transactionItems, mInterval), mPeriod);

        LineData dataBalance = new LineData(
                chartDataManager.createPredictionLineDataSet(balancePredictionMap),
                chartDataManager.createBalanceLineDataSet(balanceMap));

        LineData dataIncome = new LineData(
                chartDataManager.createPredictionLineDataSet(incomePredictionMap),
                chartDataManager.createIncomeLineDataSet(
                        transactionsManager.groupIncome(incomeMap, mPeriod)));

        LineData dataExpenses = new LineData(
                chartDataManager.createPredictionLineDataSet(expensesPredictionMap),
                chartDataManager.createExpenseLineData(
                        transactionsManager.groupExpenses(expensesMap, mPeriod)));

        forecastView.updateTabChart(dataBalance, ForecastActivity.TabName.BALANCE);
        forecastView.updateTabChart(dataIncome, ForecastActivity.TabName.INCOME);
        forecastView.updateTabChart(dataExpenses, ForecastActivity.TabName.EXPENSES);

        double incomeCurrent = transactionsManager.getAverageIncome(incomeMap);
        double incomeExpected = transactionsManager.getAverageIncome(incomePredictionMap);

        double balanceCurrent = balanceMap.lastEntry().getValue();
        double balanceExpected = balancePredictionMap.lastEntry().getValue();

        double expensesCurrent = transactionsManager.getAverageExpenses(expensesMap);
        double expensesExpected = transactionsManager.getAverageExpenses(expensesPredictionMap);


        double income_trend = (incomePredictionMap.lastEntry().getValue() - incomePredictionMap.firstEntry().getValue()) * 100 / incomePredictionMap.firstEntry().getValue();
        double balance_trend = (balancePredictionMap.lastEntry().getValue() - balancePredictionMap.firstEntry().getValue()) * 100 / balancePredictionMap.firstEntry().getValue();
        double expenses_trend = (expensesPredictionMap.lastEntry().getValue() - expensesPredictionMap.firstEntry().getValue()) * 100 / expensesPredictionMap.firstEntry().getValue();


        forecastView.updateTabData(incomeCurrent, incomeExpected, income_trend, forecastView.context().getString(R.string.tip_entertainment), ForecastActivity.TabName.INCOME);
        forecastView.updateTabData(balanceCurrent, balanceExpected, balance_trend, forecastView.context().getString(R.string.tip_entertainment), ForecastActivity.TabName.BALANCE);
        forecastView.updateTabData(expensesCurrent, expensesExpected, expenses_trend, forecastView.context().getString(R.string.tip_entertainment), ForecastActivity.TabName.EXPENSES);
   //     forecastView.updateTabData(,,, ForecastActivity.TabName.INDEX);
    }

    public void setTransactionItems(ArrayList<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }
//
//    private void getTransactionHistory() {
//
//        forecastView.showProgressDialog();
//        User user = new User("Olya", "Sahanovska", "vad.sagan@ukr.net",
//                new PrivatBankClient("4149497820710905", 127246, "fBE6F19MGT2KgxqPQT5fMvhd3pO3yqNH"));
//
//   //     this.forecastView.showToastMessage("Getting data...");
//   //     this.getPrivatBankTransactionList.execute(new ForecastPresenter.TransactionListObserver("PrivatBank"), user.getPrivatBankClient());
//     //   this.getSmsTransactionList.execute(new TransactionListObserver("Ukrsotsbank"), "Ukrsotsbank");
//    }

    private void getPredictionLists() {
        if (transactionItems != null) {
      //      this.forecastView.showProgressDialog();
            this.gotCount = 0;
            Date start = new Date();
            Date end = TransactionsManager.getEndDate(mInterval);


            this.getPredictionList.execute(
                    new ForecastPresenter.PredictionListObserver(balancePredictionMap),
                    GetPredictionList.Params.predictParams(
                            this.transactionsManager.getBalanceMap(transactionItems, mInterval), start, end));

            this.getPredictionList.execute(
                    new ForecastPresenter.PredictionListObserver(incomePredictionMap),
                    GetPredictionList.Params.predictParams(
                            this.transactionsManager.groupIncome(
                            this.transactionsManager.getIncomeMap(transactionItems, mInterval), mPeriod),  start, end));

            this.getPredictionList.execute(
                    new ForecastPresenter.PredictionListObserver(expensesPredictionMap),
                    GetPredictionList.Params.predictParams(
                            this.transactionsManager.groupExpenses(
                            this.transactionsManager.getExpensesMap(transactionItems, mInterval), mPeriod),  start, end));
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
            //       ForecastPresenter.this.forecastView.showToastMessage("Got prediction history!");
            if (++gotCount == 3) {
                updateUI();
                gotCount = 0;
                ForecastPresenter.this.forecastView.hideProgressDialog();
            }
        }

        @Override
        public void onError(Throwable e) {
            ForecastPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            ForecastPresenter.this.forecastView.hideProgressDialog();
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
//            ForecastPresenter.this.forecastView.showToastMessage("Got transaction history!");
//            getPredictionLists();
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            ForecastPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
//            ForecastPresenter.this.forecastView.hideProgressDialog();
//        }
//
//        @Override
//        public void onNext(List<TransactionItem> transactionItems) {
//            ForecastPresenter.this.transactionItems.clear();
//            ForecastPresenter.this.transactionItems.addAll(transactionItems);
//
//        }
//    }


}
