package com.example.testservise.extentions

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build

@SuppressLint("ObsoleteSdkInt")
fun MediaPlayer.setStreamingAttributes() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
        )
    } else {
        this.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }
}
