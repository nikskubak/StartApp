package com.respire.startapp.beacon

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BeaconService : Service() {

    private var networkService: NetworkService? = null
    val beaconMonitor: BeaconMonitor by lazy {
        BeaconMonitor(this) { beacon ->
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
//        sendBeaconDataToServer(
//            BeaconData(
//                "1",
//                "1",
//                "1",
//                "1",
//                null
//            )
//        )
        beaconMonitor.startRanging()
        return super.onStartCommand(intent, flags, startId)
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
                        beaconData.proximity
                    )?.execute()
                    Log.e("sendBeaconDataToServer", response?.body()?.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                //implement me
            }
        }
    }

    inner class LocalBinder : Binder() {
        internal val service: BeaconService
            get() = this@BeaconService
    }

}
