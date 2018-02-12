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
 * API interface used by {@link retrofit2.Retrofit}
 * Should be used with GitHub REST Api
 * Created by v.garshyn on 11.02.18.
 */

public interface GitHubAPI {

    /**
     * Return {@link Response} with list of {@link Contributor}'s
     * Since information about max page count available in response Headers
     * can't just return List<Contributor> because it doesn't contain valuable info about pages
     *
     * @param owner repository's owner name
     * @param repo repository name
     * @param page page number
     * @param count max items per page
     * @return
     */
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: ButterKnife-Contributor-App"
    })
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<Response<List<Contributor>>> getContributors(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page,
            @Query("per_page") int count
    );

}
