package me.alfredobejarano.prontomovieapp.injection

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import me.alfredobejarano.prontomovieapp.BuildConfig
import me.alfredobejarano.prontomovieapp.datasource.local.CacheDataBase
import me.alfredobejarano.prontomovieapp.datasource.local.CacheManager
import me.alfredobejarano.prontomovieapp.datasource.local.MovieDao
import me.alfredobejarano.prontomovieapp.datasource.remote.TheMoviesDbApiInterceptor
import me.alfredobejarano.prontomovieapp.datasource.remote.TheMoviesDbApiService
import me.alfredobejarano.prontomovieapp.model.MovieListResultObjectToMovieMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * DataSourceModule
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
class DataSourceModule(private val application: Application) {
    private val gsonConverterFactory by lazy { GsonConverterFactory.create(Gson()) }

    private val authInterceptor by lazy {
        TheMoviesDbApiInterceptor()
    }

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                BODY
            } else {
                NONE
            }
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private val retrofitClient by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    private val cacheDatabase by lazy {
        CacheDataBase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideTheMovieDbApiService(): TheMoviesDbApiService =
        retrofitClient.create(TheMoviesDbApiService::class.java)

    @Provides
    @Singleton
    fun provideMovieDao(): MovieDao = cacheDatabase.provideMovieDao()

    @Provides
    @Singleton
    fun provideCacheManager(): CacheManager = CacheManager.getInstance(application)

    @Provides
    fun provideMovieListResultObjectToMovieMapper() = MovieListResultObjectToMovieMapper()
}