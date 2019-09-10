package com.respire.startapp.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.respire.startapp.base.ObservableAndroidViewModel
import com.respire.startapp.base.Result
import com.respire.startapp.beacon.AppBeaconService
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkUtil
import com.respire.startapp.repositories.EntityRepository
import javax.inject.Inject

class MainViewModel constructor(val app: Application, var entityRepository: EntityRepository) :
    ObservableAndroidViewModel(app) {

    fun getEntities(): LiveData<Result<MutableList<Entity>>> {
        return entityRepository.getEntities(NetworkUtil.isConnected(app.baseContext))
    }

    fun startBeaconMonitoring() {
        app.startService(Intent(app, AppBeaconService::class.java))
        startServiceByAlarm(app)
    }

    /* Create an repeat Alarm that will invoke the background service for each execution time.
     * The interval time can be specified by your self.  */
    private fun startServiceByAlarm(context: Context) {
        // Get alarm manager.
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create intent to invoke the background service.
        val intent = Intent(context, AppBeaconService::class.java)
        val pendingIntent =
            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val startTime = System.currentTimeMillis()
        val intervalTime = (60 * 1000).toLong()

        val message = "Start service use repeat alarm. "

//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        Log.e("TAG_BOOT_BROADCAST_RECEIVER", message)

        // Create repeat alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent)
    }

    class Factory @Inject constructor(
        var application: Application,
        var entityRepository: EntityRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application, entityRepository) as T
        }
    }

}