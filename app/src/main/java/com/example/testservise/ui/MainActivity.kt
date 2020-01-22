package com.example.testservise.ui

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.testservise.R
import com.example.testservise.services.MyService
import com.example.testservise.services.MyService.LocalBinder
import com.example.testservise.utils.NotificationChanelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Runnable
import java.util.concurrent.TimeUnit

open class MainActivity : AppCompatActivity() {
    private lateinit var  myService: MyService
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NotificationChanelProvider.createNotificationChannel(this, NOTIFICATION)
        val intent = Intent(this, MyService().javaClass)
        val connection = serviceConnection()

        button_start.setOnClickListener {
            intent.putExtra(URL, editText.text.toString())
            bindService(intent, connection)
            ContextCompat.startForegroundService(this, intent)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    myService.mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        button_stop.setOnClickListener {
            stopService(intent)
            unbindService(connection)
        }

        button_clear.setOnClickListener {
            editText.text.clear()
        }
    }

    private fun bindService(
        intent: Intent,
        connection: ServiceConnection
    ) {
        bindService(
            intent,
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun serviceConnection() = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocalBinder
            myService = binder.getService()
            myService.getDuration().observe(this@MainActivity, Observer {
                initializeSeekBar(it)
            })
        }

        override fun onServiceDisconnected(className: ComponentName) {}
    }

    private fun initializeSeekBar(duration: Long) {
        this.toast(duration)

        seekBar.max = duration.toInt()

        runnable = Runnable {
            seekBar.progress =
                TimeUnit.MILLISECONDS.toSeconds(myService.mediaPlayer.currentPosition.toLong())
                    .toInt()
            handler.postDelayed(runnable, DELAY.toLong())
        }
        handler.postDelayed(runnable, DELAY.toLong())
    }

    companion object {
        const val DELAY = 1000
        const val NOTIFICATION = "play_notification"
        const val CHANNEL_ID = "main_activity"
        const val URL = "url_key"
    }
}

private fun MainActivity.toast(obj: Any) {
    Toast.makeText(this, obj.toString(), Toast.LENGTH_SHORT).show()
}
