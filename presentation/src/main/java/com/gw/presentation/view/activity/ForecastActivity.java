package com.gw.presentation.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.data.LineData;
import com.gw.domain.model.Interval;
import com.gw.domain.model.Period;
import com.gw.domain.model.TransactionItem;
import com.gw.presentation.R;
import com.gw.presentation.presenter.ForecastPresenter;
import com.gw.presentation.view.ForecastView;
import com.gw.presentation.view.adapters.SectionsPagerAdapter;
import com.gw.presentation.view.fragment.ForecastPlaceholderFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gw.presentation.view.activity.MainActivity.ARG_TRANSACTION_ITEMS_LIST;

public class ForecastActivity extends BaseActivity implements ForecastView {

    private static final String TAG = "ForecastActivity";
    private static final String ARG_DATE_RANGE_ITEM_POSITION = "Item position";
    private static final String ARG_GROUP_ITEM_POSITION = "Item position";

    @Inject
    ForecastPresenter presenter;
    @BindView(R.id.forecast_tab_container)
    ViewPager mViewPager;
    @BindView(R.id.forecast_toolbar)
    Toolbar toolbar;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mDateRangeItemSelected = 2;
    private int mGroupItemSelected = 2;

//    private AdapterView.OnItemSelectedListener mOnSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view,
//                                   int position, long id) {
//
//            Interval interval = Interval.fromInteger((int) (id + 1));
//
//            presenter.changePredictionInterval(interval);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//        }
//    };

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ForecastActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.fade_out, R.animator.fade_in);
        setContentView(R.layout.activity_forecast);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        this.getActivityComponent().inject(this);

        setupToolbar();

        setupPresenter();

        setupViewPager(mViewPager);
    }

    private void showDateRangeDialog(){
        CharSequence[] items = getResources().getStringArray(R.array.forecast_spinner_items);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.choose_period_title));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }});
        builder.setSingleChoiceItems(items, mDateRangeItemSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDateRangeItemSelected = which;
                Interval interval = Interval.fromInteger(which + 1);

                presenter.changePredictionInterval(interval);
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showGroupingDialog(){
        CharSequence[] items = getResources().getStringArray(R.array.forecast_group_items);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.choose_grouping_title));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }});
        builder.setSingleChoiceItems(items, mGroupItemSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGroupItemSelected = which;
                Period period = Period.fromInteger(which);

                presenter.changeGroupingPeriod(period);
                dialog.cancel();
            }
        });

        builder.show();
    }



    private void setupToolbar() {
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ex) {
            showError(ex.getMessage());
        }
    }

    private void setupPresenter() {
        presenter.setView(this);
        try {
            presenter.setTransactionItems((ArrayList<TransactionItem>) getIntent().getSerializableExtra(ARG_TRANSACTION_ITEMS_LIST));
            presenter.initialize();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(ForecastPlaceholderFragment.newInstance(TabName.INCOME), getString(R.string.income_label));
        mSectionsPagerAdapter.addFragment(ForecastPlaceholderFragment.newInstance(TabName.BALANCE), getString(R.string.balance_label));
        mSectionsPagerAdapter.addFragment(ForecastPlaceholderFragment.newInstance(TabName.EXPENSES), getString(R.string.expenses_label));
        mSectionsPagerAdapter.addFragment(ForecastPlaceholderFragment.newInstance(TabName.INDEX), getString(R.string.index_label));

        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                super.finish();
                overridePendingTransition(R.animator.fade_out, R.animator.fade_in);
                return true;
            case R.id.forecast_date_range:
                showDateRangeDialog();
                return true;
            case R.id.forecast_group_by:
                showGroupingDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.fade_out, R.animator.fade_in);
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_DATE_RANGE_ITEM_POSITION, mDateRangeItemSelected);
        outState.putInt(ARG_GROUP_ITEM_POSITION, mGroupItemSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mDateRangeItemSelected = savedInstanceState.getInt(ARG_DATE_RANGE_ITEM_POSITION);
            mGroupItemSelected = savedInstanceState.getInt(ARG_GROUP_ITEM_POSITION);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        presenter.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presenter.resume();
        super.onResume();
    }

    @Override
    public void updateTabChart(LineData data, TabName tabName) {
        int tabNumber = tabName.ordinal();
        ForecastPlaceholderFragment fragment = (ForecastPlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, tabNumber);
        if (fragment != null && data != null)
            fragment.setChartData(data);
    }

    @Override
    public void updateTabData(double current, double expected, double trend, String tip, ForecastActivity.TabName tabName) {
        int tabNumber = tabName.ordinal();
        ForecastPlaceholderFragment fragment = (ForecastPlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, tabNumber);
        if (fragment != null) {
            fragment.setCurrentValue(current);
            fragment.setExpectedValue(expected);
            fragment.setTrend(trend);

        }
    }

    public enum TabName {
        INCOME,
        BALANCE,
        EXPENSES,
        INDEX
    }

}
