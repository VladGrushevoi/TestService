package com.example.testservise.services

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

abstract class BaseService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Int.MAX_VALUE).forEach { service ->
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}