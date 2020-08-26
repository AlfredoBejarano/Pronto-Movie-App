package me.alfredobejarano.prontomovieapp.injection

import android.app.Application
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import me.alfredobejarano.prontomovieapp.R
import javax.inject.Singleton

/**
 * MediaModule
 */
@Module
class MediaModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer.create(application, R.raw.blop)
}