package com.example.testservise.repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRepo {

    suspend fun fetchAudio() : String = withContext(Dispatchers.IO) {
        URL
    }

    companion object {
        const val URL = "https://cdns-preview-e.dzcdn.net/stream/c-e77d23e0c8ed7567a507a6d1b6a9ca1b-7.mp3"
    }

}