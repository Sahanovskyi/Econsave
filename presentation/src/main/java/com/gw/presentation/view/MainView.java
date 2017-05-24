package com.gw.presentation.view;

import android.graphics.Bitmap;

import com.github.mikephil.charting.data.LineData;

public interface MainView extends View {

    void setChartData(LineData data);

    void updateDrawer(Bitmap image, String name, String email);


    void setIncome(String income);

    void setExpense(String expense);

    void setBalance(String balance);

    void setIndex(String index);

    void showError(String message);

}
