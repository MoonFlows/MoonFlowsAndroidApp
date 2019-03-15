package org.uniom.moonflows.di;

import org.uniom.moonflows.activities.AppInit;
import org.uniom.moonflows.activities.InitIPFS;
import org.uniom.moonflows.activities.Login;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(InitIPFS mainActivity);

    void inject(Login Login);

    void inject(AppInit AppInit);

}
