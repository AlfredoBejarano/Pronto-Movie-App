package me.alfredobejarano.prontomovieapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventManager {
    private val mPlayFavoriteSoundLiveData = MutableLiveData<Unit>()
    val playFavoriteSoundLiveData = mPlayFavoriteSoundLiveData as LiveData<Unit>

    fun requestFavoriteSoundPlay() = mPlayFavoriteSoundLiveData.postValue(Unit)
}