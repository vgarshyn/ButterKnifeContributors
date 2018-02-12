package com.vgarshyn.gitapp;

import android.app.Application;

import com.vgarshyn.gitapp.rest.ApiManager;

/**
 * Application is a robust Singletone in Android.
 * In this case used to store and provide instance of {@link ApiManager}
 *
 * Created by v.garshyn on 11.02.18.
 */

public class ContributorsApp extends Application {

    private static ContributorsApp contributorsApp;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        contributorsApp = this;
        apiManager = new ApiManager();
    }

    /**
     * Get instance of {@link ApiManager}
     * @return
     */
    public ApiManager getApiManager() {
        return apiManager;
    }

    /**
     * Get instance of {@link ContributorsApp}
     * @return
     */
    public static ContributorsApp getInstance() {
        return contributorsApp;
    }
}
