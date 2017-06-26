package com.gw.presentation.view.adapters;


import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

    private Runnable mRunnable;
    private Activity mActivity;

    public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.mActivity = activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        mActivity.invalidateOptionsMenu();
    }
    @Override
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        mActivity.invalidateOptionsMenu();
    }
    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
        if (mRunnable != null && newState == DrawerLayout.STATE_IDLE) {
            mRunnable.run();
            mRunnable = null;
        }
    }

    public void runWhenIdle(Runnable runnable) {
        this.mRunnable = runnable;
    }
}
