package com.gw.presentation.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.gw.domain.model.Interval;
import com.gw.domain.model.TransactionItem;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.component.MainComponent;
import com.gw.presentation.navigation.Navigator;
import com.gw.presentation.presenter.MainPresenter;
import com.gw.presentation.view.OverviewView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends BaseFragment implements OverviewView{

    public static final String ARG_TRANSACTION_ITEMS_LIST = "TransactionItemList";
    private static final String ARG_ITEM_POSITION = "Item position";
    private static final String LOG_TAG = "Overview.TAG";

    @Inject
    MainPresenter mainPresenter;

    @Inject
    Navigator navigator;

    //Bind main screen elements
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_expence)
    TextView tvExpense;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_index)
    TextView tvIndex;

    private int mItemSelected = 2;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(MainComponent.class).inject(this);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);
        setupPresenter();

        setupChart();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.main_date_range:
                showDateRangeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupPresenter(){
        mainPresenter.setView(this);
        mainPresenter.initialize();
    }

    private void setupChart() {
        chart.getDescription().setEnabled(false);
//        Description desc = new Description();
//        desc.setText(getString(R.string.chart_name));
//        desc.setTextSize(10f);
//        chart.setDescription(desc);

        // enable touch gestures
        chart.setTouchEnabled(true);

        //   chart.setDragDecelerationFrictionCoef(10f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        //  chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        chart.animateY(700);
        chart.getLegend().setEnabled(false);


        //    chart.setBackgroundColor(Color.TRANSPARENT);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(10f);
        yAxis.setAxisLineWidth(1f);
        chart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(getResources().getColor(R.color.dark_blue_gray));
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(5, true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yy");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });

    }

    private void showDateRangeDialog(){
        CharSequence[] items = getResources().getStringArray(R.array.main_spinner_items);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.choose_period_title));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }});

        builder.setSingleChoiceItems(items, mItemSelected, mOnDialogClickListener);

        builder.show();
    }

    private DialogInterface.OnClickListener mOnDialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            mItemSelected = which;
            Interval interval = Interval.fromInteger(which);

            mainPresenter.changeChartInterval(interval);
            dialog.cancel();
        }
    };

    @OnClick(R.id.btn_decision)
    public void onDecisionClick() {
        try {
            ArrayList<TransactionItem> list = new ArrayList<>(mainPresenter.getTransactionItemsList());
            navigator.navigateToDecisionActivity(getActivity(), list);
        }catch (NullPointerException ex) {
            showError("List is empty!");
        }
    }

    @OnClick(R.id.btn_forecast)
    public void onForecastClick() {
        try {
            ArrayList<TransactionItem> list = new ArrayList<>(mainPresenter.getTransactionItemsList());
            navigator.navigateToForecastActivity(getActivity(), list);
        }catch (NullPointerException ex) {
            showError("List is empty!");
        }
    }

    @Override
    public void setChartData(LineData data) {
        chart.setData(data);
        chart.animateY(700);
        chart.invalidate();
    }

    @Override
    public void setIncome(String income) {
        tvIncome.setText(income + " UAH");
    }

    @Override
    public void setExpense(String expense) {
        tvExpense.setText(expense + " UAH");
    }

    @Override
    public void setBalance(String balance) {
        tvBalance.setText(balance + " UAH");
    }

    @Override
    public void setIndex(String index) {
        tvIndex.setText(index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /*outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);
*/
        //     outState.putString("sweet2_txt", editText1.getText().toString());
        outState.putInt(ARG_ITEM_POSITION, mItemSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mItemSelected = savedInstanceState.getInt(ARG_ITEM_POSITION);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mainPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mainPresenter.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mainPresenter.resume();
        super.onResume();
    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }



}
