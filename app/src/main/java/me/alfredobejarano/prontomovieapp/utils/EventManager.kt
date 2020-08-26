package me.alfredobejarano.prontomovieapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventManager {
    private val mErrorLiveData = MutableLiveData<String>()
    val errorLiveData = mErrorLiveData as LiveData<String>
    fun showError(error: Throwable?) = mErrorLiveData.postValue(error?.localizedMessage)

    private val mShowLoadingLiveData = MutableLiveData<Boolean>()
    val showLoadingLiveData = mShowLoadingLiveData as LiveData<Boolean>
    fun showLoading(showLoading: Boolean) = mShowLoadingLiveData.postValue(showLoading)
}