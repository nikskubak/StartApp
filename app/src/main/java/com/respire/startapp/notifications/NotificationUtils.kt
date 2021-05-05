package com.respire.startapp.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.respire.startapp.R
import com.respire.startapp.notifications.NotificationScheduler.CHANNEL_GROUP
import com.respire.startapp.notifications.NotificationScheduler.CHANNEL_ID_TAG
import com.respire.startapp.notifications.NotificationScheduler.DESCRIPTION
import com.respire.startapp.notifications.NotificationScheduler.GROUP_ID
import com.respire.startapp.notifications.NotificationScheduler.ICON
import com.respire.startapp.notifications.NotificationScheduler.ID
import com.respire.startapp.notifications.NotificationScheduler.TITLE
import java.util.*
import java.util.concurrent.TimeUnit


object NotificationScheduler {
    const val CHANNEL_GROUP = "CHANNEL_GROUP"
    const val TITLE = "TITLE"
    const val DESCRIPTION = "DESCRIPTION"
    const val ICON = "ICON"
    const val ID = "ID"
    const val CHANNEL_ID_TAG = "CHANNEL_ID_TAG"
    const val GROUP_ID = 3

    data class Builder(
        var context: Context,
        var id: String,
        var notificationDate: Date = Date(),
        var title: String = "",
        var description: String = "",
        var icon: Int = R.mipmap.ic_launcher,
        var channelId: String = CHANNEL_GROUP
    ) {
        fun id(id: String) = apply { this.id = id }
        fun notificationDate(date: Date) = apply { notificationDate = date }
        fun title(title: String) = apply { this.title = title }
        fun description(description: String) = apply { this.description = description }
        fun icon(icon: Int) = apply { this.icon = icon }
        fun channelId(channelId: String) = apply { this.channelId = channelId }
        fun schedule() =
            scheduleNotification(context, id, notificationDate, title, description, icon, channelId)
    }

    private fun scheduleNotification(
        context: Context,
        id: String,
        notificationDate: Date,
        title: String,
        description: String,
        icon: Int,
        channelId: String
    ) {
        var notificationRequest: WorkRequest =
            OneTimeWorkRequestBuilder<AppointmentStartWorker>()
                .addTag(id)
                .setInputData(
                    Data.Builder()
                        .putString(TITLE, title)
                        .putString(DESCRIPTION, description)
                        .putInt(ICON, icon)
                        .putString(ID, id)
                        .putString(CHANNEL_ID_TAG, channelId)
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

abstract class OneTimeScheduleWorker(
    val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    abstract fun getOpenIntent(openedData: String?): PendingIntent?

    override fun doWork(): Result {
        createNotificationChannel(context, inputData.getString(CHANNEL_ID_TAG).orEmpty())
        var builder =
            NotificationCompat.Builder(context, inputData.getString(CHANNEL_ID_TAG).orEmpty())
                .setSmallIcon(inputData.getInt(ICON, R.mipmap.ic_launcher))
                .setContentTitle(inputData.getString(TITLE))
                .setContentText(inputData.getString(DESCRIPTION))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(CHANNEL_GROUP)
                .setContentIntent(getOpenIntent(inputData.getString(ID).toString()))
                .setAutoCancel(true)
        Log.e("inputData", inputData.getString(ID).toString())

        with(NotificationManagerCompat.from(context)) {
            notify(
                inputData.getString(ID).hashCode(),
                builder.build().apply { flags != Notification.FLAG_AUTO_CANCEL })
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val summaryNotification =
                NotificationCompat.Builder(context, inputData.getString(CHANNEL_ID_TAG).orEmpty())
                    .setContentText(inputData.getString(CHANNEL_ID_TAG).orEmpty())
                    .setSmallIcon(inputData.getInt(ICON, R.mipmap.ic_launcher))
                    .setStyle(
                        NotificationCompat.InboxStyle()
                            .addLine(inputData.getString(TITLE))
                            .setSummaryText(inputData.getString(CHANNEL_ID_TAG).orEmpty())
                    )
                    .setGroup(CHANNEL_GROUP)
                    .setGroupSummary(true)
                    .setAutoCancel(true)
            with(NotificationManagerCompat.from(context)) {
                notify(
                    GROUP_ID,
                    summaryNotification.build().apply { flags != Notification.FLAG_AUTO_CANCEL })
            }
        }
        return Result.success()
    }

    /**
     * Creates channel for notification for new versions of Android
     */
    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                channelId,
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager: NotificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
