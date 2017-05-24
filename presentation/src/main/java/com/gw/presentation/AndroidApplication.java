package com.gw.presentation;

import android.app.Application;

import com.gw.presentation.internal.di.component.ApplicationComponent;
import com.gw.presentation.internal.di.component.DaggerApplicationComponent;
import com.gw.presentation.internal.di.module.ApplicationModule;

/**
 * Android Main Application
 */
public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
//        this.initializeLeakDetection();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

}