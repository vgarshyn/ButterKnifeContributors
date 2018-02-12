package com.vgarshyn.gitapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.vgarshyn.gitapp.ContributorsApp;
import com.vgarshyn.gitapp.R;
import com.vgarshyn.gitapp.viewmodel.ContributorsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_LIST_STATE = "state.list_position";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.contributorsList) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private ContributorsListAdapter contributorsAdapter;
    private ContributorsViewModel contributorsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contributorsViewModel = ViewModelProviders.of(this).get(ContributorsViewModel.class);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        contributorsAdapter = new ContributorsListAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(5);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contributorsAdapter);

        if (isEmptyData()) {
            loadContributors();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_STATE);
            if (listState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contributorsViewModel.contributorsData.observe(this, (data)-> {
            contributorsAdapter.setContributors(data);
            hideProgressbar();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LIST_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }


    @Override
    protected void onPause() {
        super.onPause();
        contributorsViewModel.contributorsData.removeObservers(this);
    }

    @OnClick(R.id.fab)
    public void forceLoadContributors() {
        contributorsAdapter.setContributors(null);
        showProgressbar();
        loadContributors();
    }

    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    public void notifyError(String message) {
        Log.e(getClass().getName(), message);
        hideProgressbar();
        Snackbar.make(toolbar, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadContributors() {
        ContributorsApp.from(this)
                .getApiManager()
                .fetchAllContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contributorsViewModel, e -> notifyError(e.getMessage()));
    }

    private boolean isEmptyData() {
        List<?> collection = contributorsViewModel.contributorsData.getValue();
        return collection == null || collection.isEmpty();
    }

}
