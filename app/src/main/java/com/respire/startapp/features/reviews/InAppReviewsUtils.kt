package com.respire.startapp.features.reviews

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager

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
            }
        }
    }
}