package com.example.testservise.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.testservise.R
import com.example.testservise.extentions.setStreamingAttributes
import com.example.testservise.ui.MainActivity.Companion.CHANNEL_ID
import com.example.testservise.ui.MainActivity.Companion.URL
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MyService : Service() {
    val mediaPlayer by lazy { MediaPlayer() }
    private lateinit var notification: Notification
    private val audioDuration = MutableLiveData<Long>()

    override fun onCreate() {
        super.onCreate()

        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText("Much longer text")
            .build()
        mediaPlayer.setStreamingAttributes()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        GlobalScope.launch {
            prepareMediaPlayer(intent)
            startForeground(1, notification)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    private suspend fun prepareMediaPlayer(intent: Intent) = withContext(Dispatchers.IO) {
        mediaPlayer.apply {
            setDataSource(intent.getStringExtra(URL))
            setOnCompletionListener {
                stop()
                reset()
            }
            isLooping = true
            prepare()
            start()

            withContext(Dispatchers.Main) {
                audioDuration.value = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
            }
        }
    }

    fun getDuration() = audioDuration

    inner class LocalBinder : Binder() {
        fun getService() = this@MyService
    }
}
