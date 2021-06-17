package com.respire.startapp.features.reviews

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.respire.startapp.BuildConfig
import com.respire.startapp.R
import kotlinx.android.synthetic.main.dialog_rate.view.*

object InAppReviewHelper {
    fun reviewApp(
        context: Context,
        activity: Activity,
        onCompleteListener: (isSuccess: Boolean) -> Unit
    ) {
        val manager = ReviewManagerFactory.create(context)

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    onCompleteListener(true)
                }
            } else {
                task.exception?.printStackTrace()
                onCompleteListener(false)
                openAppInGooglePlay(context)
            }
        }
    }

    fun fakeReviewApp(
        context: Context,
        activity: Activity,
        onCompleteListener: (isSuccess: Boolean) -> Unit
    ) {
        val manager = FakeReviewManager(context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    onCompleteListener(true)
                }
            } else {
                task.exception?.printStackTrace()
                onCompleteListener(false)
                openAppInGooglePlay(context)
            }
        }
    }

    public fun showReviewDialog(
        context: Context,
        activity: Activity,
        onCompleteListener: (isSuccess: Boolean) -> Unit
    ) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.CustomDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rate, null, false)
        builder.setView(view)
        val dialog: AlertDialog = builder.create()
        view.rateButton.setOnClickListener {
            fakeReviewApp(context, activity, onCompleteListener)
            dialog.dismiss()
        }
        view.problemButton.setOnClickListener {
            openMailClient(activity)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openAppInGooglePlay(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        }
    }

    private fun openMailClient(activity: Activity) {
        try {
//            ShareCompat.IntentBuilder(activity)
//                .setType("text/plain")
//                .addEmailTo("respirecorp@gmail.com")
//                .setSubject("${activity.getString(R.string.app_trouble)} \"${activity.getString(R.string.app_name)}\"")
//                .setChooserTitle(activity.getString(R.string.mail_client))
//                .startChooser()
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf("respirecorp@gmail.com"))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "${activity.getString(R.string.app_trouble)} \"${activity.getString(R.string.app_name)}\""
                )
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}