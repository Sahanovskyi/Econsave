package com.gw.presentation.internal.di.component;


import android.content.Context;
import android.gesture.Prediction;

import com.gw.domain.executor.PostExecutionThread;
import com.gw.domain.executor.ThreadExecutor;
import com.gw.domain.interactor.forecast.PredictionManager;
import com.gw.domain.interactor.forecast.TransactionsManager;
import com.gw.domain.model.User;
import com.gw.domain.repository.Repository;
import com.gw.presentation.internal.di.module.ApplicationModule;
import com.gw.presentation.navigation.Navigator;
import com.gw.presentation.presenter.helper.ChartDataManager;
import com.gw.presentation.view.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //Exposed to sub-graphs.
    Navigator navigator();

    PredictionManager predictionManager();

    TransactionsManager transactionManager();

    ChartDataManager chartDataManager();

    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    Repository repository();
}
