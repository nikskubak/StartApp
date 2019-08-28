package com.respire.startapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.respire.startapp.R
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var vmFactory: MainViewModel.Factory
    lateinit var viewModel: MainViewModel
    private val layoutManager = LinearLayoutManager(this)
    private val adapter: EntityRecyclerAdapter = EntityRecyclerAdapter(mutableListOf()) {
        openAppInGooglePlay(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = ViewModelProviders.of(this, vmFactory)[MainViewModel::class.java]
        retrieveEntities()
    }

    private fun retrieveEntities() {
        swipeRefreshLayout.isRefreshing = true
        viewModel.getEntities().observe(this, Observer<Result<MutableList<Entity>>> {
            it.data?.let { data ->
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
            it.error?.printStackTrace()
            swipeRefreshLayout.isRefreshing = false
        })
    }

    private fun initViews() {
        entitiesRecyclerView.layoutManager = layoutManager
        entitiesRecyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener { retrieveEntities() }
    }

    private fun openAppInGooglePlay(it: String?) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$it")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$it")
                )
            )
        }
    }
}
