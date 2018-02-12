package com.vgarshyn.gitapp;

import android.app.Application;

import com.vgarshyn.gitapp.rest.ApiManager;

/**
 * Created by v.garshyn on 11.02.18.
 */

public class ContributorsApp extends Application {

    private static ContributorsApp app;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        apiManager = new ApiManager();
    }

    public ApiManager getApiManager() {
        return apiManager;
    }

    public static ContributorsApp getInstance() {
        return app;
    }
}
