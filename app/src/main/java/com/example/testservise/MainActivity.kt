package com.example.testservise

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        button_start.setOnClickListener {
            if (!isMyServiceRunning(MyService::class.java)) {
                ContextCompat.startForegroundService(this, Intent(this, MyService::class.java))
            }
        }

        button_stop.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Location notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        const val URL =
            "https://cdns-preview-6.dzcdn.net/stream/c-6c2fab13b800a517459da1d8f2c7fd9d-3.mp3"
        const val CHANNEL_ID = "main_activity"
    }
}