package me.alfredobejarano.prontomovieapp.injection

import android.app.Application
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewComponent
import me.alfredobejarano.prontomovieapp.R
import javax.inject.Singleton

/**
 * MediaModule
 */
@Module
@InstallIn(FragmentComponent::class)
class MediaModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer.create(application, R.raw.blop)
}