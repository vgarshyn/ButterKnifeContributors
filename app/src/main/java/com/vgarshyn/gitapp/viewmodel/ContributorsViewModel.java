package com.vgarshyn.gitapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.vgarshyn.gitapp.ContributorsApp;
import com.vgarshyn.gitapp.rest.model.Contributor;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by v.garshyn on 12.02.18.
 */

public class ContributorsViewModel extends ViewModel implements Consumer<List<Contributor>> {

    private MutableLiveData<List<Contributor>> contributorsData = new MutableLiveData<>();
    private PublishSubject<Throwable> exceptionWatcher = PublishSubject.create();

    @Override
    public void accept(List<Contributor> contributors) {
        contributorsData.postValue(contributors);
    }

    public LiveData<List<Contributor>> getContributorsData() {
        if (isEmptyData()) {
            loadContributors();
        }
        return contributorsData;
    }

    public Disposable subscribeErrorHandler(@NonNull Consumer<Throwable> consumer) {
        return exceptionWatcher.subscribe(consumer);
    }

    public void loadContributors() {
        ContributorsApp.getInstance()
                .getApiManager()
                .fetchAllContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this, e -> exceptionWatcher.onNext(e));
    }

    private boolean isEmptyData() {
        List<?> collection = contributorsData.getValue();
        return collection == null || collection.isEmpty();
    }
}
