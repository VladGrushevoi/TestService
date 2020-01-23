package com.example.testservise.ui.app

import android.app.Application
import com.example.testservise.utils.NotificationHelper

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChannel(this, "AudioNtfc", CHANNEL_1_ID)
    }

    companion object {
        const val CHANNEL_1_ID = "channel1"
    }
}