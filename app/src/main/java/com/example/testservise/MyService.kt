package com.example.testservise

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.testservise.MainActivity.Companion.CHANNEL_ID
import com.example.testservise.MainActivity.Companion.URL


class MyService : Service() {
    private val mediaPlayer by lazy { MediaPlayer.create(this, Uri.parse(URL)) }
    private lateinit var wakeLock: PowerManager.WakeLock

    @SuppressLint("WakelockTimeout")
    override fun onCreate() {
        super.onCreate()

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText("Much longer text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(1, builder)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.isLooping = true
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock.release()
        mediaPlayer.stop()
    }
}
