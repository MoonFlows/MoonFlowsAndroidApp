package org.uniom.moonflows;

import android.app.Application;

import org.uniom.moonflows.di.AppComponent;
import org.uniom.moonflows.di.AppModule;
import org.uniom.moonflows.di.DaggerAppComponent;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().appModule(new AppModule())
                                      .build();
    }

    public static AppComponent component() {
        return component;
    }
}
