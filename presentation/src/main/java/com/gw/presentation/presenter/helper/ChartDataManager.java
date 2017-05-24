package com.gw.presentation.presenter.helper;

import android.content.Context;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gw.domain.model.Interval;
import com.gw.presentation.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChartDataManager {

    private Context context;

    @Inject
    public ChartDataManager(Context context) {
        this.context = context;
    }

    public LineDataSet createBalanceLineDataSet(TreeMap<Date, Double> transactions) {

        return setupLineDataSet(
                transactions,
                context.getString(R.string.balance_label),
                context.getResources().getColor(R.color.blueTextColor)
        );
    }

    public LineDataSet createPredictionLineDataSet(TreeMap<Date, Double> predictedItems) {

  //      LineDataSet current = setupLineDataSet(currentItems, "Current balance", context.getResources().getColor(R.color.blueTextColor));
        LineDataSet predicted = setupLineDataSet(predictedItems, "Predicted", context.getResources().getColor(R.color.gray));

        return predicted;

    }


    public LineDataSet createIncomeLineDataSet(TreeMap<Date, Double> transactions) {

        return setupLineDataSet(
                transactions,
                context.getString(R.string.income_label),
                context.getResources().getColor(R.color.greenTextColor));
    }


    public LineDataSet createExpenseLineData(TreeMap<Date, Double> transactions) {

        return setupLineDataSet(
                transactions,
                context.getString(R.string.expenses_label),
                context.getResources().getColor(R.color.redTextColor));
    }


    private LineDataSet setupLineDataSet(Map<Date, Double> map, String label, int color) {

        ArrayList<Entry> values = new ArrayList<>();

        for (Map.Entry<Date, Double> pair : map.entrySet()) {
            values.add(new Entry(pair.getKey().getTime(), pair.getValue().floatValue()));
        }

        LineDataSet set = new LineDataSet(values, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setLineWidth(1.7f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setFillAlpha(50);
        set.setFillColor(color);
        set.setDrawCircleHole(false);
        set.setDrawFilled(true);

        return set;
    }

}
