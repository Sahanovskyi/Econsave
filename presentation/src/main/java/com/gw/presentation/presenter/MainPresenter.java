package com.gw.presentation.presenter;

import com.github.mikephil.charting.data.LineData;
import com.gw.domain.exception.DefaultErrorBundle;
import com.gw.domain.exception.ErrorBundle;
import com.gw.domain.interactor.DefaultObserver;
import com.gw.domain.interactor.GetPrivatBankTransactionList;
import com.gw.domain.interactor.GetSmsTransactionList;
import com.gw.domain.interactor.forecast.TransactionsManager;
import com.gw.domain.model.Interval;
import com.gw.domain.model.PrivatBank.PrivatBankClient;
import com.gw.domain.model.TransactionItem;
import com.gw.domain.model.User;
import com.gw.presentation.exception.ErrorMessageFactory;
import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.mapper.TransactionItemModelMapper;
import com.gw.presentation.presenter.helper.ChartDataManager;
import com.gw.presentation.view.OverviewView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;


@PerActivity
public class MainPresenter implements Presenter {

    private final static double MIN_SALARY = 1700;
    private final static double AVERAGE_SALARY = 6200;
    private final GetPrivatBankTransactionList mGetPrivatBankTransactionList;
    private final GetSmsTransactionList mGetSmsTransactionList;
    private final TransactionsManager mTransactionsManager;
    private ChartDataManager mChartDataManager;
    private TransactionItemModelMapper mTransactionItemModelMapper;
    private OverviewView mOverviewView;
    private User mUser;
    private Map<String, List<TransactionItem>> mTransactionMap;
    private Interval mInterval = Interval.LAST_QUARTER;

    private String mTransactionSource = "PrivatBank";

    @Inject
    MainPresenter(TransactionItemModelMapper mTransactionItemModelMapper, TransactionsManager mTransactionsManager, GetPrivatBankTransactionList mGetPrivatBankTransactionList, GetSmsTransactionList mGetSmsTransactionList, ChartDataManager mChartDataManager) {
        this.mGetPrivatBankTransactionList = mGetPrivatBankTransactionList;
        this.mGetSmsTransactionList = mGetSmsTransactionList;
        this.mChartDataManager = mChartDataManager;
        this.mTransactionsManager = mTransactionsManager;
        this.mTransactionItemModelMapper = mTransactionItemModelMapper;
    }

    public List<TransactionItem> getTransactionItemsList() {
        return mTransactionMap.get(mTransactionSource);
    }

    public void changeChartInterval(Interval interval) {
        this.mInterval = interval;

        updateUI();
    }

    private void updateUI(){
        if (mTransactionMap != null && mTransactionMap.size() != 0) {
            try {
                mOverviewView.setChartData(new LineData(
                        mChartDataManager.createBalanceLineDataSet(
                                mTransactionsManager.getBalanceMap(
                                        mTransactionMap.get(mTransactionSource), mInterval))));

                mOverviewView.setBalance(String.format(Locale.getDefault(), "%.2f", mTransactionsManager.getBalance(mTransactionMap.get(mTransactionSource), mInterval)));
                mOverviewView.setIncome(String.format(Locale.getDefault(), "%.2f", mTransactionsManager.getIncome(mTransactionMap.get(mTransactionSource), mInterval)));
                mOverviewView.setExpense(String.format(Locale.getDefault(), "%.2f", mTransactionsManager.getExpenses(mTransactionMap.get(mTransactionSource), mInterval)));

            } catch (NullPointerException ex) {
                mOverviewView.showError("Change chart mInterval");
            }
        } else {
            mOverviewView.showError("Haven't got transaction history yet");
        }
    }

    private void updateIndex(){
        double income = this.mTransactionsManager.getIncome(mTransactionMap.get(mTransactionSource), Interval.LAST_MONTH);
        double expenses = this.mTransactionsManager.getExpenses(mTransactionMap.get(mTransactionSource), Interval.LAST_MONTH);

    }

    public void setView(OverviewView view) {
        this.mOverviewView = view;
    }

    public void logOut() {
    }

    //Initialize presenter
    public void initialize() {
        mTransactionMap = new HashMap<>();

        //Initialize user
        //See registration merchant instruction here https://api.privatbank.ua/#p24/registration
        mUser = new User("Vadym", "Sahanovskyi", "vad.sagan@ukr.net",
                new PrivatBankClient("1111111111111111", 1111, "1111"));

        getTransactionHistory();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.mGetPrivatBankTransactionList.dispose();
        this.mGetSmsTransactionList.dispose();
        this.mOverviewView = null;
        this.mTransactionMap = null;
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.mOverviewView.context(),
                errorBundle.getException());
        this.mOverviewView.showError(errorMessage);
    }

    private void getTransactionHistory() {
        this.mGetPrivatBankTransactionList.execute(new TransactionListObserver(mTransactionSource), mUser.getPrivatBankClient());
        //     this.mGetSmsTransactionList.execute(new TransactionListObserver("Ukrsotsbank"), "Ukrsotsbank");
    }

    private final class TransactionListObserver extends DefaultObserver<List<TransactionItem>> {

        private String transactionSource;

        TransactionListObserver(String transactionSource) {
            this.transactionSource = transactionSource;
        }

        @Override
        public void onComplete() {
            MainPresenter.this.mOverviewView.showToastMessage("Got data!");
            updateUI();
        }

        @Override
        public void onError(Throwable e) {
            MainPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(List<TransactionItem> transactionItems) {
            MainPresenter.this.mTransactionMap.put(transactionSource, transactionItems);
        }
    }

}
