package com.vgarshyn.gitapp.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

import com.vgarshyn.gitapp.R
import com.vgarshyn.gitapp.rest.model.Contributor
import com.vgarshyn.gitapp.viewmodel.ContributorsViewModel

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.disposables.Disposable

/**
 * Main activity uses to show list of contributors
 * Contains logic to display data.
 * Logic for handling rotation and invoking network calls implemented through Android Architectural Components
 * See more: https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * Created by v.garshyn on 11.02.18.*
 */
class MainActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.contributorsList)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.totalCount)
    lateinit var textCount: TextView

    private val contributorsAdapter: ContributorsListAdapter = ContributorsListAdapter()
    private lateinit var contributorsViewModel: ContributorsViewModel
    private var errorHandler: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contributorsViewModel = ViewModelProviders.of(this).get(ContributorsViewModel::class.java)

        ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(5)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contributorsAdapter
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            val listState = savedInstanceState.getParcelable<Parcelable>(KEY_LIST_STATE)
            if (listState != null) {
                recyclerView!!.layoutManager.onRestoreInstanceState(listState)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        errorHandler = contributorsViewModel.subscribeErrorHandler { notifyError(it.message) }
        contributorsViewModel.contributorsData.observe(this@MainActivity, Observer<List<Contributor>> {
            contributorsAdapter.setContributors(it)
            updateTextCount(it)
            hideProgressbar()
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelable(KEY_LIST_STATE, recyclerView.layoutManager.onSaveInstanceState())
    }


    override fun onPause() {
        super.onPause()
        errorHandler?.dispose()
    }

    /**
     * Renew dataset by executing new network request
     */
    @OnClick(R.id.fab)
    fun forceLoadContributors() {
        contributorsAdapter.setContributors(null)
        showProgressbar()
        textCount.visibility = View.INVISIBLE
        contributorsViewModel.loadContributors()
    }

    /**
     * Display progressbar
     */
    fun showProgressbar() {
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Hide progressbar
     */
    fun hideProgressbar() {
        progressBar.visibility = View.GONE
    }

    /**
     * Update text of total count of contributors
     * @param data
     */
    private fun updateTextCount(data: List<Contributor>?) {
        if (data.orEmpty().isNotEmpty()) {
            textCount.visibility = View.VISIBLE
            textCount.text = getString(R.string.total_count, data!!.size)
        } else {
            textCount.visibility = View.INVISIBLE
        }
    }

    /**
     * Show error message as [Snackbar]
     * @param message
     */
    fun notifyError(message: String?) {
        message.let {
            Log.e(javaClass.name, message)
            hideProgressbar()
            Snackbar.make(toolbar, message as CharSequence, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {

        private val KEY_LIST_STATE = "state.list_position"
    }

}
