package me.alfredobejarano.prontomovieapp.datasource.remote

import me.alfredobejarano.prontomovieapp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

/**
 * TheMoviesDbApiInterceptor
 */
class TheMoviesDBApiAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(request().newBuilder().apply {
            url(addQueryParamsToURL(request().url.newBuilder()))
        }.build())
    }

    private fun addQueryParamsToURL(urlBuilder: HttpUrl.Builder) = urlBuilder.apply {
        val deviceRegion = getLanguageAndRegion()
        addQueryParameter("api_key", BuildConfig.API_KEY)
        addQueryParameter("language", deviceRegion.first)
        addQueryParameter("region", deviceRegion.second)
    }.build()

    /**
     * Retrieves the language and country (region) from the current device
     * locale in ISO-639-1 format.
     *
     * @return [Pair] object containing the language in the first position
     * and the country in the second position. Ex: es, MX
     */
    private fun getLanguageAndRegion() = Locale.getDefault().run {
        Pair(language ?: "es", country.toUpperCase(this))
    }
}