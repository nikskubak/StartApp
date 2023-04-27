package com.respire.startapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.respire.startapp.R
import com.respire.startapp.databinding.ActivityMainBinding
import com.respire.startapp.features.notifications.NotificationScheduler
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var vmFactory: MainViewModel.Factory
    lateinit var viewModel: MainViewModel
    private val layoutManager = LinearLayoutManager(this)
    private val adapter: ModelRecyclerAdapter = ModelRecyclerAdapter(mutableListOf()) {
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
        viewModel = ViewModelProvider(this, vmFactory)[MainViewModel::class.java]
        initUiChangesListeners()
        retrieveModels()
//        showNotification()
//        InAppReviewHelper.showReviewDialog(this, this) {
//            Log.e("InAppReviewHelper", it.toString())
//        }
    }

    private fun initUiChangesListeners() {
        lifecycleScope.launchWhenStarted {
            viewModel.modelsUiState.collect {
                if(it.isSuccess){
                    adapter.data = it.getOrDefault(emptyList())
                    adapter.notifyDataSetChanged()
                }else{
                    it.exceptionOrNull()?.printStackTrace()
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.errorUiState.collect {
                it?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    binding.swipeRefreshLayout.isRefreshing = false
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

    private fun retrieveModels() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getModels()
    }

    private fun initViews() {
        binding.entitiesRecyclerView.layoutManager = layoutManager
        binding.entitiesRecyclerView.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshModels()
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
