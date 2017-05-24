package com.gw.presentation.internal.di.component;

import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.internal.di.module.ActivityModule;
import com.gw.presentation.internal.di.module.AuthModule;
import com.gw.presentation.view.activity.LoginActivity;
import com.gw.presentation.view.activity.SignUpActivity;
import com.gw.presentation.view.activity.StartActivity;

import dagger.Component;

/**
 * Created by vadym on 12.05.17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, AuthModule.class})
public interface AuthComponent {
    void inject(StartActivity startActivity);
    void inject(LoginActivity loginActivity);
    void inject(SignUpActivity signUpActivity);

}
