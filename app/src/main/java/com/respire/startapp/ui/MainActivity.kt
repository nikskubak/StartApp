package com.respire.startapp.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.estimote.sdk.SystemRequirementsChecker
import com.respire.startapp.R
import com.respire.startapp.beacon.AppBeaconService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleOwner{

    @Inject
    lateinit var vmFactory: MainViewModel.Factory
    lateinit var viewModel: MainViewModel
    private val layoutManager = LinearLayoutManager(this)
    private val adapter: EntityRecyclerAdapter = EntityRecyclerAdapter(mutableListOf()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = ViewModelProviders.of(this, vmFactory)[MainViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    private fun initViews() {
        startButton.setOnClickListener {
            viewModel.startBeaconMonitoring()
            bindService(Intent(this, AppBeaconService::class.java),
                serviceConnection, Context.BIND_AUTO_CREATE)
            startButton.visibility = View.GONE
        }
        stopButton.setOnClickListener { viewModel.stopBeaconMonitoring() }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.isServiceBound) {
            unbindService(serviceConnection)
            viewModel.isServiceBound = false
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as AppBeaconService.LocalBinder
            viewModel.isServiceBound = true
            Log.e("MonitoringListener", "MonitoringListener")
            binder.service.monitoringListener = object : MonitoringListener{
                override fun onEnteredRegion() {
                    Log.e("MonitoringListener", "onEnteredRegion")
                    rootView.setBackgroundResource(R.drawable.enter_region)
                }

                override fun onExitedRegion() {
                    Log.e("MonitoringListener", "onExitedRegion")
                    rootView.setBackgroundResource(R.drawable.background)
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.isServiceBound  = false
        }
    }

    interface MonitoringListener{
        fun onEnteredRegion()
        fun onExitedRegion()
    }

}
