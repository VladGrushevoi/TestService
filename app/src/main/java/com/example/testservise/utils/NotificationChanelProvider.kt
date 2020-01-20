package com.example.testservise.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.testservise.ui.MainActivity

object NotificationChanelProvider {
    fun createNotificationChannel(context: Context, name: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                MainActivity.CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(true)
                notificationManager.createNotificationChannel(this)
            }
        }
    }
}