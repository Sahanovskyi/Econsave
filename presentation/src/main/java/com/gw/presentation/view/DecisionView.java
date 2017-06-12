package com.gw.presentation.view;

import com.github.mikephil.charting.data.LineData;
import com.gw.presentation.view.View;
import com.gw.presentation.view.activity.ForecastActivity;

/**
 * Created by vadym on 20.05.17.
 */

public interface DecisionView extends View {
    void updateChart(LineData data);
    void setTip(String tip);
    void setCurrentBalance(double amount);
    void setExpectedBalance(double amount);
    void setDecisionEffectBalance(double amount);
    void setTrend(double trend);
}
