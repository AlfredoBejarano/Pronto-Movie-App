package me.alfredobejarano.prontomovieapp.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Date
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * CacheManager
 */
class CacheManager private constructor(private val prefs: SharedPreferences) {
    fun createMovieCache() = prefs.edit {
        val now = Date().time
        val cacheDuration = MILLISECONDS.convert(MOVIES_CACHE_DURATION_HOURS, HOURS)
        putLong(MOVIES_CACHE_TIMESTAMP_KEY, now + cacheDuration).apply()
    }

    fun isMovieCacheValid(): Boolean {
        val now = Date().time
        val timestamp = prefs.getLong(MOVIES_CACHE_TIMESTAMP_KEY, now)
        return timestamp > now && getMaxPages() != NO_MAX_PAGES
    }

    fun updatePagination(maxPages: Int, currentPage: Int) = prefs.edit {
        putInt(MAX_PAGES_KEY, maxPages)
        putInt(CURRENT_PAGE_KEY, currentPage)
    }

    fun getMaxPages() = prefs.getInt(MAX_PAGES_KEY, NO_MAX_PAGES)

    fun getCurrentPage() = prefs.getInt(CURRENT_PAGE_KEY, 1)

    companion object {
        const val NO_MAX_PAGES = -1
        private const val MAX_PAGES_KEY = "max_pages"
        private const val CURRENT_PAGE_KEY = "current_page"
        private const val MOVIES_CACHE_DURATION_HOURS = 12L
        private const val CACHE_PREFERENCES_FILE_NAME = "cache_file"
        private const val MOVIES_CACHE_TIMESTAMP_KEY = "movies_cache"

        @Volatile
        private var INSTANCE: CacheManager? = null

        private fun createInstance(ctx: Context) = CacheManager(
            ctx.getSharedPreferences(CACHE_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        )

        fun getInstance(ctx: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createInstance(ctx).also { newCacheManager -> INSTANCE = newCacheManager }
        }
    }
}