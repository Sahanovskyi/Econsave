package com.gw.presentation.internal.di.component;

import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.internal.di.module.ActivityModule;
import com.gw.presentation.internal.di.module.DecisionModule;
import com.gw.presentation.presenter.DecisionReviewPresenter;
import com.gw.presentation.view.fragment.DecisionListFragment;
import com.gw.presentation.view.fragment.DecisionReviewFragment;
import com.gw.presentation.view.fragment.DecisionWizardFragment;

import dagger.Component;

/**
 * Created by vadym on 09.05.17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, DecisionModule.class})

public interface DecisionComponent {
    void inject(DecisionWizardFragment decisionWizardFragment);
    void inject(DecisionListFragment decisionListFragment);
    void inject(DecisionReviewFragment decisionReviewFragment);
}
