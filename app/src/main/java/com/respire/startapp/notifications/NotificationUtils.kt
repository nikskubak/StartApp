package com.respire.startapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.respire.startapp.R
import com.respire.startapp.notifications.NotificationScheduler.CHANNEL_ID
import com.respire.startapp.notifications.NotificationScheduler.DESCRIPTION
import com.respire.startapp.notifications.NotificationScheduler.ICON
import com.respire.startapp.notifications.NotificationScheduler.TITLE
import java.util.*
import java.util.concurrent.TimeUnit


object NotificationScheduler {
    const val CHANNEL_ID = "App"
    const val TITLE = "TITLE"
    const val DESCRIPTION = "DESCRIPTION"
    const val ICON = "ICON"

    data class Builder(
        var context: Context,
        var id: String,
        var notificationDate: Date = Date(),
        var title: String = "",
        var description: String = "",
        var icon: Int = R.drawable.ic_launcher_foreground
    ) {
        fun id(id: String) = apply { this.id = id }
        fun notificationDate(date: Date) = apply { notificationDate = date }
        fun title(title: String) = apply { this.title = title }
        fun description(description: String) = apply { this.description = description }
        fun icon(icon: Int) = apply { this.icon = icon }
        fun schedule() =
            scheduleNotification(context, id, notificationDate, title, description, icon)
    }

    private fun scheduleNotification(
        context: Context,
        id: String,
        notificationDate: Date,
        title: String,
        description: String,
        icon: Int
    ) {
        val notificationRequest: WorkRequest =
            OneTimeWorkRequestBuilder<OneTimeScheduleWorker>()
                .addTag(id)
                .setInputData(
                    Data.Builder()
                        .putString(TITLE, title)
                        .putString(DESCRIPTION, description)
                        .putInt(ICON, icon)
                        .build()
                )
                .setInitialDelay(
                    notificationDate.time - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS
                )
                .build()
        WorkManager.getInstance(context).enqueue(notificationRequest)
    }

    fun removeScheduledNotification(context: Context, id: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(id)
    }
}

class OneTimeScheduleWorker(
    val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(inputData.getInt(ICON, R.drawable.ic_launcher_foreground))
            .setContentTitle(inputData.getString(TITLE))
            .setContentText(inputData.getString(DESCRIPTION))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
        return Result.success()
    }

    /**
     * Creates channel for notification for new versions of Android
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager: NotificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}