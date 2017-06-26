package com.gw.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.gw.domain.model.TransactionItem;
import com.gw.domain.model.decision.BuyAsset;
import com.gw.domain.model.decision.Decision;
import com.gw.domain.model.decision.SellAsset;
import com.gw.domain.model.decision.TakeCredit;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.component.DecisionComponent;
import com.gw.presentation.presenter.DecisionReviewPresenter;
import com.gw.presentation.view.DecisionView;
import com.gw.presentation.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DecisionReviewFragment extends BaseFragment implements DecisionView{

    private static final String ARG_DECISION = "decision tag";

    @Inject
    DecisionReviewPresenter presenter;

    @BindView(R.id.decision_chart) LineChart chart;
    @BindView(R.id.decision_head_label) TextView tvLabel;
    @BindView(R.id.decision_tv_current_value) TextView tvCurrent;
    @BindView(R.id.decision_tv_expected_value) TextView tvExpected;
    @BindView(R.id.decision_tv_effect_value) TextView tvEffect;
    @BindView(R.id.decision_trend_arrow) ImageView trendArrow;
    @BindView(R.id.decision_trend_tv) TextView tvTrend;
    @BindView(R.id.decision_tv_tip) TextView tvTip;


    public DecisionReviewFragment() {
        // Required empty public constructor
    }

    public static DecisionReviewFragment newInstance(ArrayList<TransactionItem> list, Decision decision) {
        DecisionReviewFragment fragment = new DecisionReviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.ARG_TRANSACTION_ITEMS_LIST, list);
        args.putSerializable(ARG_DECISION, decision);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DecisionComponent.class).inject(this);

        if (getArguments() != null) {
            this.presenter.setTransactionsList((ArrayList<TransactionItem>) getArguments().getSerializable(MainActivity.ARG_TRANSACTION_ITEMS_LIST));
            this.presenter.setDecision((Decision)getArguments().getSerializable(ARG_DECISION));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_decision_review, container, false);
        ButterKnife.bind(this, view);
        Decision decision = (Decision) getArguments().getSerializable(ARG_DECISION);
        String title;
        if(decision instanceof BuyAsset)
            title = getString(R.string.buy_asset);
        else if(decision instanceof SellAsset)
            title = getString(R.string.sell_asset);
        else if (decision instanceof TakeCredit)
            title = getString(R.string.take_credit);
        else
            title = getString(R.string.open_deposit);
        tvLabel.setText(title);

        initChart();

        return view;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.presenter.setView(this);
        this.presenter.initialize();
    }

    private void initChart() {
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        //  chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        chart.animateY(700);
        chart.getLegend().setEnabled(false);

        //    chart.setBackgroundColor(Color.TRANSPARENT);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(12f);
        yAxis.setAxisLineWidth(1f);
        chart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
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



    @Override public Context context() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public void updateChart(LineData data) {
        if (chart != null) {
            chart.setData(data);
            chart.animateY(700);
            chart.invalidate();
        }
    }

    @Override
    public void setTip(String tip) {
        this.tvTip.setText(tip);
    }

    @Override
    public void setCurrentBalance(double amount) {
        tvCurrent.setText(String.format(Locale.getDefault(), "%.0f", amount));
    }

    @Override
    public void setExpectedBalance(double amount) {
        tvExpected.setText(String.format(Locale.getDefault(), "%.0f", amount));
    }

    @Override
    public void setDecisionEffectBalance(double amount) {
        tvEffect.setText(String.format(Locale.getDefault(), "%.0f", amount));
    }

    @Override
    public void setTrend(double trend) {
        tvTrend.setText(String.format(Locale.getDefault(), "%+2.2f%%", trend));

        if(trend >= 0) {
                tvTrend.setTextColor(getActivity().getResources().getColor(R.color.green_arrow));
                trendArrow.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_arrow_up_green));
                setTip(getString(R.string.positive_prediction));
        } else {
                tvTrend.setTextColor(getActivity().getResources().getColor(R.color.red_arrow));
                trendArrow.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_arrow_down_red));
                setTip(getString(R.string.negative_prediction));
        }
    }

    @Override public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.presenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.presenter.destroy();
    }

}
