package com.gw.presentation.internal.di.component;

import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.internal.di.module.ActivityModule;
import com.gw.presentation.internal.di.module.DecisionModule;
import com.gw.presentation.internal.di.module.ForecastModule;
import com.gw.presentation.view.activity.ForecastActivity;

import dagger.Component;

/**
 * Created by vadym on 12.05.17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ForecastModule.class})

public interface ForecastComponent {
    void inject(ForecastActivity forecastActivity);
}
