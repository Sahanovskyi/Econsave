package com.gw.presentation.view.activity;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gw.presentation.AndroidApplication;
import com.gw.presentation.R;
import com.gw.presentation.internal.di.component.ActivityComponent;
import com.gw.presentation.internal.di.component.ApplicationComponent;
import com.gw.presentation.internal.di.component.DaggerActivityComponent;
import com.gw.presentation.internal.di.module.ActivityModule;
import com.gw.presentation.navigation.Navigator;

import javax.inject.Inject;


public abstract class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    @Inject
    Navigator navigator;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, boolean isAddToBackStack) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.appearing, R.animator.disappearing);
        fragmentTransaction.replace(containerViewId, fragment);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        //   fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_in_left);
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link com.gw.presentation.internal.di.component.ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        Application app = getApplication();
        AndroidApplication androidApplication = (AndroidApplication) app;
        return androidApplication.getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link com.gw.presentation.internal.di.module.ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    private void initializeInjector() {
        this.activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

}
