package org.uniom.moonflows;

import android.app.Application;
import android.content.Intent;

import org.uniom.moonflows.di.AppComponent;
import org.uniom.moonflows.di.AppModule;
import org.uniom.moonflows.di.DaggerAppComponent;
import org.uniom.moonflows.ipfs.IPFSDaemonService;

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
