package com.vgarshyn.gitapp.rest;

import com.vgarshyn.gitapp.rest.model.Contributor;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by v.garshyn on 11.02.18.
 */

public interface GitHubAPI {

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<Response<List<Contributor>>> getContributors(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page,
            @Query("per_page") int count
    );

}
