package com.gw.presentation.internal.di.component;

import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.internal.di.module.ActivityModule;
import com.gw.presentation.internal.di.module.MainModule;
import com.gw.presentation.view.fragment.OverviewFragment;

import dagger.Component;

/**
 * Created by vadym on 12.06.17.ForecastModule
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MainModule.class})
public interface MainComponent {
    void inject(OverviewFragment overviewFragment);
}
