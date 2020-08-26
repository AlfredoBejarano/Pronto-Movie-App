package me.alfredobejarano.prontomovieapp.injection

import android.app.Application

object Injector {
    @Volatile
    private var COMPONENT_INSTANCE: AppComponent? = null

    private fun createComponent(app: Application) = DaggerAppComponent.builder()
        .application(app)
        .dataSourceModule(DataSourceModule(app))
        .build()


    fun getComponentInstance(app: Application) = COMPONENT_INSTANCE ?: synchronized(this) {
        COMPONENT_INSTANCE ?: createComponent(app).also { COMPONENT_INSTANCE = it }
    }
}