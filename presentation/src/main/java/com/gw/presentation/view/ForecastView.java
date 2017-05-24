package com.gw.presentation.view;

import com.github.mikephil.charting.data.LineData;
import com.gw.presentation.view.activity.ForecastActivity;

/**
 * Created by vadym on 10.05.17.
 */

public interface ForecastView extends View {
    void updateTabChart(LineData data, ForecastActivity.TabName tabName);
    void updateTabData(double current, double expected, double trend, String tip, ForecastActivity.TabName tabName);
}
