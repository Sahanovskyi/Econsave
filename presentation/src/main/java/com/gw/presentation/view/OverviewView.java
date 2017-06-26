package com.gw.presentation.view;

import com.github.mikephil.charting.data.LineData;

public interface OverviewView extends View {

    void setChartData(LineData data);

    void setIncome(String income);

    void setExpense(String expense);

    void setBalance(String balance);

    void setIndex(String index);

    void showError(String message);

}
