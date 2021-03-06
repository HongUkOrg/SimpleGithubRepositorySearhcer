package com.bleo.simplegithubserachapplication.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bleo.simplegithubserachapplication.R
import com.bleo.simplegithubserachapplication.main.view.MainRecyclerViewAdapter
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    // MARK: - Properties
    private lateinit var viewModel: MainViewModel

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val recyclerViewAdapter = MainRecyclerViewAdapter()

    // MARK: - View Life Cycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setUp()
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    // MARK: - SetUp
    private fun setUp() {
        githubRecyclerView.adapter = recyclerViewAdapter
        githubRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    // MARK: - Bind
    private fun bind() {

        searchEditText
            .textChanges()
            .debounce(200, TimeUnit.MILLISECONDS)
            .filter { it.isEmpty() == false }
            .subscribe {
                viewModel.searchTextChanged(it)
                viewModel.searchRepositories()
            }
            .addTo(disposable)

        searchBtn
            .clicks()
            .subscribe {
                viewModel.searchRepositories()
            }
            .addTo(disposable)

        viewModel
            .githubItemRelay
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                recyclerViewAdapter.items = it
            }
            .addTo(disposable)

    }
}