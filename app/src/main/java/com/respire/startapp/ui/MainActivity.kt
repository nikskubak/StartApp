package com.respire.startapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.respire.startapp.R
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.features.notifications.NotificationScheduler
import com.respire.startapp.features.reviews.InAppReviewHelper
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_rate.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        retrieveEntities()
        showNotification()
        showRateDialog()
    }

    private fun showReview() {
        InAppReviewHelper.fakeReviewApp(this, this) {
            Log.e("InAppReviewHelper", it.toString())
        }
    }

    private fun showRateDialog() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this, R.style.CustomDialog)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null, false)
        builder.setView(view)
        val dialog: AlertDialog = builder.create()
        view.rateButton.setOnClickListener {
//            openAppInGooglePlay()
            showReview()
            dialog.dismiss()
        }
        view.problemButton.setOnClickListener {
//            openMailClient()
            dialog.dismiss()
        }
        dialog.show()
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
