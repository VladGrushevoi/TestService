package com.example.testservise.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {
    fun createNotificationChannel(context: Context, name: String, id: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                id,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(false)
                notificationManager.createNotificationChannel(this)
            }
        }
    }
}