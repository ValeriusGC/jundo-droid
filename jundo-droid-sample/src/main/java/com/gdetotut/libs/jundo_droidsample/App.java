package com.gdetotut.libs.jundo_droidsample;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.gdetotut.libs.jundo_droidsample.di.ApiModule;
import com.gdetotut.libs.jundo_droidsample.di.AppComponent;
import com.gdetotut.libs.jundo_droidsample.di.ContextModule;
import com.gdetotut.libs.jundo_droidsample.di.DaggerAppComponent;

public class App extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        sAppComponent = appComponent;
    }

}
