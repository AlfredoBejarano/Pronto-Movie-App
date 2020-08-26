package me.alfredobejarano.prontomovieapp

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import me.alfredobejarano.prontomovieapp.injection.Injector
import javax.inject.Inject

/**
 * ProntoMovieApplication
 */
class ProntoMovieApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = injector

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        Injector.getComponentInstance(this).inject(this)
    }
}