package com.vgarshyn.gitapp

import android.app.Application

import com.vgarshyn.gitapp.rest.ApiManager

/**
 * Application is a robust Singletone in Android.
 * In this case used to store and provide instance of [ApiManager]
 *
 * Created by v.garshyn on 11.02.18.
 */

class ContributorsApp : Application() {
    /**
     * Get instance of [ApiManager]
     * @return
     */
    lateinit var apiManager: ApiManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        apiManager = ApiManager()
    }

    companion object {

        /**
         * Get instance of [ContributorsApp]
         * @return
         */
        lateinit var instance: ContributorsApp
    }
}
