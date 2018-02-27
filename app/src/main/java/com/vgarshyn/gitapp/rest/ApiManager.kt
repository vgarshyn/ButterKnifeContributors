package com.vgarshyn.gitapp.rest

import com.vgarshyn.gitapp.rest.model.Contributor
import com.vgarshyn.gitapp.utils.HeaderLinkParser

import java.io.IOException

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main utility class to get data via network through GitHub API
 *
 * Created by v.garshyn on 11.02.18.
 */

const val COUNT_PER_PAGE = 30
const val REPO_OWNER = "JakeWharton"
const val REPO_NAME = "butterknife"
const val HEADER_LINK = "Link"

class ApiManager {

    private val api = GitHubAPI.instance()

    /**
     * Get RxJava Observale with contributors list and don't care about response headers
     * Items count controls by [COUNT_PER_PAGE]
     * @param page number of required page
     * @return
     */
    fun getContributors(page: Int): Observable<List<Contributor>> {
        return api.getContributors(REPO_OWNER, REPO_NAME, page, COUNT_PER_PAGE)
                .map { response -> response.body() }
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
    </Contributor> */
    fun fetchAllContributors(): Observable<List<Contributor>> {
        val startPage = 1
        return api.getContributors(REPO_OWNER, REPO_NAME, startPage, COUNT_PER_PAGE)
                .flatMap { response ->

                    if (response.body() == null && response.errorBody() != null) {
                        throw IOException(response.errorBody()!!.string())
                    }

                    val maxPage = getMaxPage(response)

                    val nextPagesObservable: Observable<List<Contributor>> = fetchPages(startPage.inc(), maxPage)

                    val zipper: BiFunction<List<Contributor>?, List<Contributor>?, List<Contributor>> = BiFunction {
                        list1, list2 ->
                        val result = list1.toMutableList()
                        result.addAll(list2)
                        result
                    }

                    Observable.just(response.body())
                            .zipWith(nextPagesObservable, zipper)
                }
    }

    /**
     * Return Observable which contains contributors List from pages range
     * @param start
     * @param end
     * @return
     */
    private fun fetchPages(start: Int, end: Int): Observable<List<Contributor>> {
        return Observable.range(start, end)
                .concatMap { page -> getContributors(page) }
                .concatMapIterable { list -> list }
                .toList()
                .toObservable()
    }

    /**
     * Try to find max page number in [Response] header
     * @param response
     * @return
     */
    private fun getMaxPage(response: Response<*>): Int {
        val headerLinks = response.headers().get(HEADER_LINK)
        val parser = HeaderLinkParser(headerLinks)
        return parser.maxPagesCount
    }

}
