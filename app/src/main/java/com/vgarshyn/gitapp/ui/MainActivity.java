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

import com.vgarshyn.gitapp.R;
import com.vgarshyn.gitapp.viewmodel.ContributorsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Main activity uses to show list of contributors
 * Contains logic to display data.
 * Logic for handling rotation and invoking network calls implemented through Android Architectural Components
 * See more: https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * Created by v.garshyn on 11.02.18.*
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_LIST_STATE = "state.list_position";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contributorsList)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ContributorsListAdapter contributorsAdapter;
    private ContributorsViewModel contributorsViewModel;
    private Disposable errorHandler;

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
        errorHandler = contributorsViewModel.subscribeErrorHandler(e -> notifyError(e.getMessage()));
        contributorsViewModel.getContributorsData().observe(this, (data) -> {
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
        errorHandler.dispose();
    }

    /**
     * Renew dataset by executing new network request
     */
    @OnClick(R.id.fab)
    public void forceLoadContributors() {
        contributorsAdapter.setContributors(null);
        showProgressbar();
        contributorsViewModel.loadContributors();
    }

    /**
     * Display progressbar
     */
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progressbar
     */
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Show error message as {@link Snackbar}
     * @param message
     */
    public void notifyError(String message) {
        Log.e(getClass().getName(), message);
        hideProgressbar();
        Snackbar.make(toolbar, message, Snackbar.LENGTH_LONG).show();
    }

}
