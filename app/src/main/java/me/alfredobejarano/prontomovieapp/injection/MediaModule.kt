package me.alfredobejarano.prontomovieapp.injection

import android.content.Context
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import me.alfredobejarano.prontomovieapp.R
import javax.inject.Singleton

/**
 * MediaModule
 */
@Module
@InstallIn(ApplicationComponent::class)
class MediaModule {
    @Singleton
    @Provides
    fun provideMediaPlayer(@ApplicationContext ctx: Context): MediaPlayer =
        MediaPlayer.create(ctx, R.raw.blop)
}