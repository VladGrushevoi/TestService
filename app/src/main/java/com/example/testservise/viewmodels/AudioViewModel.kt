package com.example.testservise.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testservise.repos.AudioRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AudioViewModel : ViewModel() {
    private val audioLiveData = MutableLiveData<String>()
    private val audioRepo = AudioRepo()

    fun getAudioData() : LiveData<String> {
        GlobalScope.launch(Dispatchers.IO) {
            audioLiveData.value = audioRepo.fetchAudio()
        }
        return audioLiveData
    }
}