package com.gw.presentation.internal.di.component;


import android.app.Activity;

import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.internal.di.module.ActivityModule;
import com.gw.presentation.view.activity.ForecastActivity;
import com.gw.presentation.view.activity.MainActivity;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link com.gw.presentation.internal.di.PerActivity}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(ForecastActivity forecastActivity);
    void inject(MainActivity mainActivity);

    //   void inject(DecisionActivity decisionActivity);

    //Exposed to sub-graphs.
    Activity activity();
}
