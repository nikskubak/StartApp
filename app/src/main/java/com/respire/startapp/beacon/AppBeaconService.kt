package com.respire.startapp.beacon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.respire.startapp.R
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkService
import com.respire.startapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppBeaconService : Service() {

    var monitoringListener: MainActivity.MonitoringListener? = null
    private var networkService: NetworkService? = null
    val CHANNEL_ID = "ForegroundServiceChannel"
    val beaconMonitor: BeaconMonitor by lazy {
        BeaconMonitor(this, monitoringListener) { beacon ->
            Toast.makeText(
                this,
                "Sent beacon with major ${beacon.major}, inRange ${beacon.inRange}",
                Toast.LENGTH_SHORT
            ).show()
            sendBeaconDataToServer(beacon)
        }
    }

    internal var localBinder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        Log.e("onCreate", "onCreate")
        networkService =
            NetworkService.getAuthRetrofitService("https://clients.hmsl.nl")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.e("onBind", "onBind")
        return localBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("onStartCommand", "onStartCommand")
        val resultLivaData = MutableLiveData<Result<MutableList<Entity>>>()
        val result = Result<MutableList<Entity>>()
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Beacons monitoring")
            .setContentText(input)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0L))
            .build()

        startForeground(1, notification)
        beaconMonitor.startMonitoring()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconMonitor.stopMonitoring()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Beacons monitoring channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun sendBeaconDataToServer(beaconData: BeaconData) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = networkService?.sendBeacon(
                        beaconData.uuid,
                        beaconData.major,
                        beaconData.minor,
                        beaconData.rssi,
                        beaconData.proximity,
                        beaconData.inRange
                    )?.execute()
                    Log.e("sendBeaconDataToServer", "beacon was sent")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                //implement me
            }
        }
    }

    inner class LocalBinder : Binder() {
        internal val service: AppBeaconService
            get() = this@AppBeaconService
    }

}
