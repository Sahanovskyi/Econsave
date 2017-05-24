package com.gw.presentation.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.gw.presentation.R;
import com.gw.presentation.view.activity.ForecastActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastPlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAB_SECTION_NAME = "section_number";
    private static final String ARG_CURRENT = "current value";
    private static final String ARG_EXPECTED = "expected value";
    private static final String ARG_TREND = "trend value";
    private static final String ARG_CURRENCY = "currency";
    private static final String ARG_TIP = "tip text";
    @BindView(R.id.forecast_chart)
    LineChart chart;
    @BindView(R.id.forecast_tab_name)
    TextView tvTabName;

    @BindView(R.id.forecast_label_current)
    TextView tvLabelCurrent;
    @BindView(R.id.forecast_tv_current_value)
    TextView tvCurrent;

    @BindView(R.id.forecast_label_expected)
    TextView tvLabelExpected;
    @BindView(R.id.forecast_tv_expected_value)
    TextView tvExpected;

    @BindView(R.id.forecast_trend_arrow)
    ImageView imTrendArrow;
    @BindView(R.id.forecast_trend_tv)
    TextView tvTrend;

    @BindView(R.id.tv_currency)
    TextView tvCurrency;

    @BindView(R.id.forecast_tv_tip)
    TextView tvTip;

    private LineData mLineData;
    private Bundle mSavedState = null;
    private ForecastActivity.TabName mTabName;


    public ForecastPlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ForecastPlaceholderFragment newInstance(ForecastActivity.TabName tabName) {
        ForecastPlaceholderFragment fragment = new ForecastPlaceholderFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAB_SECTION_NAME, tabName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mTabName == null)
            mTabName = ForecastActivity.TabName.BALANCE;
        if(mSavedState == null)
            mSavedState = new Bundle();
        if (mLineData == null)
            mLineData = new LineData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);

        if(mSavedState != null) {
            restoreData(mSavedState);
        }


        initChart();
        if (mLineData != null) {
            chart.setData(mLineData);
            chart.animateY(700);
            chart.invalidate();
        }

        ForecastActivity.TabName tabName = (ForecastActivity.TabName) getArguments().getSerializable(TAB_SECTION_NAME);
        mTabName = tabName;
        String strTabName;
        String strLabelCurrent;
        String strLabelExpected;
        int color;
        switch (tabName) {
            case BALANCE:
                strTabName = getString(R.string.balance_label);
                strLabelCurrent = getString(R.string.current_balance);
                strLabelExpected = getString(R.string.expected_balance);
                color = getResources().getColor(R.color.blueTextColor);
                break;
            case INCOME:
                strTabName = getString(R.string.income_label);
                strLabelCurrent = getString(R.string.current_average_income);
                strLabelExpected = getString(R.string.expected_average_income);
                color = getResources().getColor(R.color.greenTextColor);
                break;
            case EXPENSES:
                strTabName = getString(R.string.expenses_label);
                strLabelCurrent = getString(R.string.current_average_expenses);
                strLabelExpected = getString(R.string.expected_average_expenses);
                color = getResources().getColor(R.color.redTextColor);

                break;
            case INDEX:
                strTabName = getString(R.string.index_label);
                strLabelCurrent = getString(R.string.current_balance);
                strLabelExpected = getString(R.string.expected_balance);
                color = getResources().getColor(R.color.gray);

                break;
            default:
                strTabName = getString(R.string.balance_label);
                strLabelCurrent = getString(R.string.current_balance);
                strLabelExpected = getString(R.string.expected_balance);
                color = getResources().getColor(R.color.blueTextColor);
        }
        tvTabName.setText(strTabName);
        tvTabName.setTextColor(color);
        tvLabelCurrent.setText(strLabelCurrent);
        tvLabelExpected.setText(strLabelExpected);

        return rootView;
    }

    public void saveState() {
        try {
            mSavedState.putDouble(ARG_CURRENT, Double.parseDouble(tvCurrent.getText().toString()));
            mSavedState.putDouble(ARG_EXPECTED, Double.parseDouble(tvExpected.getText().toString()));
            String trend = tvTrend.getText().toString();
            trend = trend.replaceAll("%", "");
            trend = trend.replace(',', '.');
            mSavedState.putDouble(ARG_TREND, Double.parseDouble(trend));
            mSavedState.putString(ARG_TIP, tvTip.getText().toString());
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
        }
    }

    @Override
    public void onDestroyView() {
        saveState();
        super.onDestroyView();
    }



    private void restoreData(Bundle savedBundle) {
        double current = savedBundle.getDouble(ARG_CURRENT);
        double expected = savedBundle.getDouble(ARG_EXPECTED);
        double trend = savedBundle.getDouble(ARG_TREND);
  //      String currency = savedBundle.getString(ARG_CURRENCY);
        String tip = savedBundle.getString(ARG_TIP);

        setCurrentValue(current);
        setExpectedValue(expected);
        setTrend(trend);
        setTipText(tip);
   //     setCurrency(currency);
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

    public void setChartData(LineData data) {
        mLineData = data;
        if (chart != null) {
            chart.setData(data);
            chart.animateY(700);
            chart.invalidate();
        }
    }

    public void setCurrentValue(double value) {
        tvCurrent.setText(String.format(Locale.getDefault(), "%.0f", value));
    }

    public void setExpectedValue(double value) {
        tvExpected.setText(String.format(Locale.getDefault(), "%.0f", value));
    }

    public void setTipText(String tipText) {
        tvTip.setText(tipText);
    }

    public void setCurrency(String currency) {
        if (currency != null)
            tvCurrent.setText(currency);
    }

    public void setTrend(double trend){
        tvTrend.setText(String.format(Locale.getDefault(), "%+2.2f%%", trend));
        int green = getActivity().getResources().getColor(R.color.green_arrow);
        int red = getActivity().getResources().getColor(R.color.red_arrow);
        Drawable upGreen = getActivity().getResources().getDrawable(R.drawable.ic_arrow_up_green);
        Drawable downRed = getActivity().getResources().getDrawable(R.drawable.ic_arrow_down_red);

        Drawable upRed = getActivity().getResources().getDrawable(R.drawable.ic_arrow_up_red);
        Drawable downGreen = getActivity().getResources().getDrawable(R.drawable.ic_arrow_down_green);

        if(trend >= 0) {
            if(mTabName == ForecastActivity.TabName.EXPENSES) {
                tvTrend.setTextColor(red);
                imTrendArrow.setImageDrawable(upRed);
                setTipText(getString(R.string.negative));

            } else {
                tvTrend.setTextColor(green);
                imTrendArrow.setImageDrawable(upGreen);
                setTipText(getString(R.string.positive));

            }
        } else {
            if(mTabName == ForecastActivity.TabName.EXPENSES){
                tvTrend.setTextColor(green);
                imTrendArrow.setImageDrawable(downGreen);
                setTipText(getString(R.string.positive));

            } else {
                tvTrend.setTextColor(red);
                imTrendArrow.setImageDrawable(downRed);
                setTipText(getString(R.string.negative));

            }
        }
    }

}
