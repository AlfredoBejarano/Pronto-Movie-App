package me.alfredobejarano.prontomovieapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.alfredobejarano.prontomovieapp.utils.EventManager.showError
import me.alfredobejarano.prontomovieapp.utils.EventManager.showLoading
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel
 */

/**
 * Runs a Job in the given [CoroutineContext].
 * @param ctx CoroutineContext to execute the work from (Default is [Dispatchers.IO]).
 * @param block Work to execute.
 */
fun ViewModel.execute(ctx: CoroutineContext = Dispatchers.IO, block: suspend () -> Unit) =
    viewModelScope.launch(ctx) {
        showLoading(true)
        try {
            block()
        } catch (t: Throwable) {
            showError(t)
        }
        showLoading(false)
    }

/**
 * Returns a LiveData reporting the result of a work that ran in the given [CoroutineContext].
 * @param ctx CoroutineContext to execute the work from (Default is [Dispatchers.IO]).
 * @param block Work to execute.
 */
fun <T> executeForLiveData(ctx: CoroutineContext = Dispatchers.IO, block: suspend () -> T) =
    liveData(ctx) {
        showLoading(true)
        val result = try {
            block()
        } catch (t: Throwable) {
            showError(t)
            null
        }
        emit(result)
        showLoading(false)
    }