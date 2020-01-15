package com.example.testservise

import android.media.MediaPlayer

class CustomMediaPlayer(): MediaPlayer() {
    var link: String? = null

    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        link = path
    }
}