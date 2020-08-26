package me.alfredobejarano.prontomovieapp.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import me.alfredobejarano.prontomovieapp.ProntoMovieApplication
import javax.inject.Singleton

/**
 * AppComponent
 */
@Singleton
@Component(
    modules = [
        ViewModule::class,
        MediaModule::class,
        ViewModelModule::class,
        DataSourceModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
        fun mediaModule(module: MediaModule): Builder
        fun dataSourceModule(module: DataSourceModule): Builder
    }

    fun inject(app: ProntoMovieApplication)
}