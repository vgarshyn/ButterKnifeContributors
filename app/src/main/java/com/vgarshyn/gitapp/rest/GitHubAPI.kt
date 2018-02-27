package com.vgarshyn.gitapp.rest

import com.vgarshyn.gitapp.rest.model.Contributor

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API interface used by [retrofit2.Retrofit]
 * Should be used with GitHub REST Api
 * Created by v.garshyn on 11.02.18.
 */

interface GitHubAPI {

    /**
     * Return [Response] with list of [Contributor]'s
     * Since information about max page count available in response Headers
     * can't just return List<Contributor> because it doesn't contain valuable info about pages
     *
     * @param owner repository's owner name
     * @param repo repository name
     * @param page page number
     * @param count max items per page
     * @return
    </Contributor> */
    @Headers("Accept: application/vnd.github.v3.full+json", "User-Agent: ButterKnife-Contributor-App")
    @GET("/repos/{owner}/{repo}/contributors")
    fun getContributors(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Query("page") page: Int,
            @Query("per_page") count: Int
    ): Observable<Response<List<Contributor>>>

    companion object Factory {
        fun instance(): GitHubAPI {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(GitHubAPI::class.java)
        }
    }
}
