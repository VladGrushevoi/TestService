package com.example.testservise.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.testservise.R
import com.example.testservise.extentions.setStreamingAttributes
import com.example.testservise.ui.MainActivity.Companion.IS_LOOPING
import com.example.testservise.ui.MainActivity.Companion.URL
import com.example.testservise.ui.app.App.Companion.CHANNEL_1_ID
import com.example.testservise.ui.notifications.NotificationBroadcastReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.util.concurrent.TimeUnit


class MyService : Service() {
    val mediaPlayer by lazy { MediaPlayer() }
    private val audioDuration = MutableLiveData<Long>()
    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        mediaPlayer.setStreamingAttributes()
        mediaSession = MediaSessionCompat(this, "tag")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        GlobalScope.launch {
            prepareMediaPlayer(intent)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    private suspend fun prepareMediaPlayer(intent: Intent) = withContext(Dispatchers.IO) {
        mediaPlayer.apply {
            reset()
                intent.apply {
                    setDataSource(this.getStringExtra(URL))
                    setOnCompletionListener {
                        stop()
                        reset()
                    }
                    isLooping = this.getBooleanExtra(IS_LOOPING, false)
                    prepare()
                    start()
                }
                withContext(Dispatchers.Main) {
                    createNotification()
                    audioDuration.value = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
                }
        }
    }

    private fun createNotification() {
        val buttonIntent = Intent(this, NotificationBroadcastReceiver::class.java)
        buttonIntent.putExtra("player", mediaPlayer as Serializable)
        val btPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, 0)

        val image = BitmapFactory.decodeResource(resources, R.drawable.img)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle(getString(R.string.track))
            .setContentText(getString(R.string.album))
            .setLargeIcon(image)
            .addAction(R.drawable.ic_fast_rewind, "Rewind", null)
            .addAction(R.drawable.ic_play, "Previous", btPendingIntent)
            .addAction(R.drawable.ic_fast_forward, "Forward", null)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(
                    1, 2
                ).setMediaSession(mediaSession.sessionToken)
            )
            .setSubText("Sub Text")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        startForeground(1, notification)
    }

    fun getDuration() = audioDuration

    inner class LocalBinder : Binder() {
        fun getService() = this@MyService
    }
}