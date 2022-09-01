package com.respire.startapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.respire.startapp.R
import com.respire.startapp.databinding.ActivityMainBinding
import com.respire.startapp.features.notifications.NotificationScheduler
import com.respire.startapp.features.reviews.InAppReviewHelper
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var vmFactory: MainViewModel.Factory
    lateinit var viewModel: MainViewModel
    private val layoutManager = LinearLayoutManager(this)
    private val adapter: EntityRecyclerAdapter = EntityRecyclerAdapter(mutableListOf()) {
        openAppInGooglePlay(it)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
        viewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        initUiChangesListeners()
        retrieveEntities()
        showNotification()
        InAppReviewHelper.showReviewDialog(this, this) {
            Log.e("InAppReviewHelper", it.toString())
        }
    }

    private fun initUiChangesListeners() {
        lifecycleScope.launchWhenStarted {
            viewModel.entitiesUiState.collect {
                it.data?.let { data ->
                    adapter.data = data
                    adapter.notifyDataSetChanged()
                }
                it.error?.printStackTrace()
                swipeRefreshLayout.isRefreshing = false
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.errorUiState.collect {
                it?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun showNotification() {
        NotificationScheduler.Builder(this, "unique notification id")
            .title("Test title")
            .description("Test description")
            .icon(R.drawable.ic_launcher_foreground)
            .channelId("Notifications about events")
            .notificationDate(Date().apply {
                time = System.currentTimeMillis() + DateUtils.MINUTE_IN_MILLIS
            })
            .schedule()
    }

    private fun retrieveEntities() {
        swipeRefreshLayout.isRefreshing = true
        viewModel.getEntities()
    }

    private fun initViews() {
        entitiesRecyclerView.layoutManager = layoutManager
        entitiesRecyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshEntities()
        }
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
