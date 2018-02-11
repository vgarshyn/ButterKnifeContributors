package com.vgarshyn.gitapp.rest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by v.garshyn on 11.02.18.
 */

public class ApiManager {

    private static final String URL_API = "https://api.github.com";
    private static final String REPO_OWNER = "JakeWharton";
    private static final String REPO_NAME = "butterknife";
    private static final String HEADER_LINK = "Link";
    private static final int COUNT_PER_PAGE = 30;

    private GitHubAPI api;

    public ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(GitHubAPI.class);
    }

}
