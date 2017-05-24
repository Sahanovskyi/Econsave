package com.gw.presentation.internal.di.module;

import android.content.Context;

import com.gw.data.cache.Cache;
import com.gw.data.cache.CacheImpl;
import com.gw.data.executor.JobExecutor;
import com.gw.data.repository.DataRepository;
import com.gw.domain.executor.PostExecutionThread;
import com.gw.domain.executor.ThreadExecutor;
import com.gw.domain.model.User;
import com.gw.domain.repository.Repository;
import com.gw.domain.interactor.forecast.PredictionManager;
import com.gw.presentation.AndroidApplication;
import com.gw.presentation.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {
    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }


    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    Cache provideUserCache(CacheImpl cache) {
        return cache;
    }

    @Provides
    @Singleton
    Repository provideUserRepository(DataRepository dataRepository) {
        return dataRepository;
    }
}
