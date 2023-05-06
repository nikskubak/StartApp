package com.respire.startapp.features.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import androidx.work.WorkerParameters
import com.respire.startapp.uiComposeHilt.MainComposeHiltActivity

class EntityStartWorker(context: Context, workerParameters: WorkerParameters) :
    OneTimeScheduleWorker(context, workerParameters) {

    override fun getOpenIntent(openedData: String?): PendingIntent? {

        var resultIntent = Intent(context, MainComposeHiltActivity::class.java)
        resultIntent.putExtra("openedData", openedData)
        val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()
//        return PendingIntent.getActivity(context, uniqueInt, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(uniqueInt, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return resultPendingIntent

    }
}
