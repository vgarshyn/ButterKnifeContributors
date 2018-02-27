package com.vgarshyn.gitapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.vgarshyn.gitapp.ContributorsApp
import com.vgarshyn.gitapp.rest.model.Contributor

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


/**
 * This ViewModel class cares about storing list of contributors separate from Activity lifecycle.
 * Used [LiveData] to easy handling changes.
 * If error occurred Activity will be notified through [PublishSubject] if subscribed correctly
 *
 * Created by v.garshyn on 12.02.18.
 */

class ContributorsViewModel : ViewModel(), Consumer<List<Contributor>> {

    private val contributorsData = MutableLiveData<List<Contributor>>()
    private val exceptionWatcher = PublishSubject.create<Throwable>()

    /**
     * Check is any contributors data is already stored
     * @return
     */
    private val isEmptyData: Boolean get() = contributorsData.value.orEmpty().isEmpty()


    override fun accept(contributors: List<Contributor>) {
        contributorsData.postValue(contributors)
    }

    /**
     * Returns LiveData for contributors list.
     * If contributors list is empty perform loading request
     * @return
     */
    fun getContributorsData(): LiveData<List<Contributor>> {
        if (isEmptyData) {
            loadContributors()
        }
        return contributorsData
    }

    /**
     * Subscribe observer listen for errors.
     * Returns [Disposable]
     * @param consumer
     * @return
     */
    fun subscribeErrorHandler(@NonNull consumer: Consumer<Throwable>): Disposable {
        return exceptionWatcher.subscribe(consumer)
    }

    /**
     * Get all contributors and handle
     */
    fun loadContributors() {
        ContributorsApp.instance
                .apiManager
                .fetchAllContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this@ContributorsViewModel,
                        Consumer { e -> exceptionWatcher.onNext(e) })
    }
}
