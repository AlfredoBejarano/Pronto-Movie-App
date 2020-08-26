package me.alfredobejarano.prontomovieapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventManager {
    private val mPlayFavoriteSoundLiveData = MutableLiveData<Unit>()
    val playFavoriteSoundLiveData = mPlayFavoriteSoundLiveData as LiveData<Unit>
    fun requestFavoriteSoundPlay() = mPlayFavoriteSoundLiveData.postValue(Unit)

    private val mRequestNextPageLiveData = MutableLiveData<Unit>()
    val requestNextPageLiveData = mRequestNextPageLiveData as LiveData<Unit>
    fun requestNextPage() = mRequestNextPageLiveData.postValue(Unit)

    private val mShowLoadingLiveData = MutableLiveData<Boolean>()
    val showLoadingLiveData = mShowLoadingLiveData as LiveData<Boolean>
    fun showLoading(showLoading: Boolean) = mShowLoadingLiveData.postValue(showLoading)
}