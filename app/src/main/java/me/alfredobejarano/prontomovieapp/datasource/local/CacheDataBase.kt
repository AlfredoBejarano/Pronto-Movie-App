package me.alfredobejarano.prontomovieapp.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.alfredobejarano.prontomovieapp.BuildConfig
import me.alfredobejarano.prontomovieapp.model.local.Movie

/**
 * CacheDataBase
 */
@Database(version = BuildConfig.VERSION_CODE, exportSchema = true, entities = [Movie::class])
abstract class CacheDataBase : RoomDatabase() {
    abstract fun provideMovieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "${BuildConfig.APPLICATION_ID}.CacheDataBase"

        @Volatile
        private var INSTANCE: CacheDataBase? = null

        private fun createInstance(ctx: Context): CacheDataBase =
            Room.databaseBuilder(ctx, CacheDataBase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}