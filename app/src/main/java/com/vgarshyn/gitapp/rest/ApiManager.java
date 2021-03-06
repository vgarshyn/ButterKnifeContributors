package com.vgarshyn.gitapp.rest;

import com.vgarshyn.gitapp.rest.model.Contributor;
import com.vgarshyn.gitapp.utils.HeaderLinkParser;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main utility class to get data via network through GitHub API
 *
 * Created by v.garshyn on 11.02.18.
 */

public class ApiManager {

    private static final int COUNT_PER_PAGE = 30;
    private static final String URL_API = "https://api.github.com";
    private static final String REPO_OWNER = "JakeWharton";
    private static final String REPO_NAME = "butterknife";
    private static final String HEADER_LINK = "Link";

    private GitHubAPI api;

    public ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(GitHubAPI.class);
    }

    /**
     * Get RxJava Observale with contributors list and don't care about response headers
     * Items count controls by {@link ApiManager#COUNT_PER_PAGE}
     * @param page number of required page
     * @return
     */
    public Observable<List<Contributor>> getContributors(int page) {
        return api.getContributors(REPO_OWNER, REPO_NAME, page, COUNT_PER_PAGE)
                .map(response -> response.body());
    }

    /**
     * Get full list of contributors
     * Logic is next:
     * 1) Get First page(number 1), parse headers to figure out max page number
     * 2) Convert received items to Observable and merge with another Observable
     * 3) Create observable sequence which get data from remaining pages (2...max) and merge with first one
     * 4) Present data as one Observable with item List<Contributor>
     *
     * @return
     */
    public Observable<List<Contributor>> fetchAllContributors() {
        final int startPage = 1;
        return api.getContributors(REPO_OWNER, REPO_NAME, startPage, COUNT_PER_PAGE)
                .flatMap(response -> {

                    if (response.body() == null && response.errorBody() != null) {
                        throw new IOException(response.errorBody().string());
                    }

                    int maxPage = getMaxPage(response);

                    return Observable.just(response.body())
                            .zipWith(fetchPages(startPage + 1, maxPage), (list1, list2) -> {
                                list1.addAll(list2);
                                return list1;
                            });
                });
    }

    /**
     * Return Observable which contains contributors List from pages range
     * @param start
     * @param end
     * @return
     */
    private Observable<List<Contributor>> fetchPages(int start, int end) {
        return Observable.range(start, end)
                .concatMap(page -> getContributors(page))
                .concatMapIterable(list -> list)
                .toList()
                .toObservable();
    }

    /**
     * Try to find max page number in {@link Response} header
     * @param response
     * @return
     */
    private int getMaxPage(Response<?> response) {
        String headerLinks = response.headers().get(HEADER_LINK);
        HeaderLinkParser parser = new HeaderLinkParser(headerLinks);
        return parser.getMaxPagesCount();
    }

}
