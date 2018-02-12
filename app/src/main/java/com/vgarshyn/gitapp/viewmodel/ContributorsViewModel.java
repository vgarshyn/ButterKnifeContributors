package com.vgarshyn.gitapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.vgarshyn.gitapp.rest.model.Contributor;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by v.garshyn on 12.02.18.
 */

public class ContributorsViewModel extends ViewModel implements Consumer<List<Contributor>> {
    public MutableLiveData<List<Contributor>> contributorsData = new MutableLiveData<>();

    @Override
    public void accept(List<Contributor> contributors) {
        contributorsData.postValue(contributors);
    }

}
