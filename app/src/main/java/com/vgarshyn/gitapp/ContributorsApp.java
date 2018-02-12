package com.vgarshyn.gitapp;

import android.app.Application;
import android.content.Context;

import com.vgarshyn.gitapp.rest.ApiManager;

/**
 * Created by v.garshyn on 11.02.18.
 */

public class ContributorsApp extends Application {

    ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = new ApiManager();
    }

    public ApiManager getApiManager() {
        return apiManager;
    }

    public static ContributorsApp from(Context context) {
        return (ContributorsApp)context.getApplicationContext();
    }
}
