package me.alfredobejarano.prontomovieapp

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp

/**
 * ProntoMovieApplication
 */
@HiltAndroidApp
class ProntoMovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}