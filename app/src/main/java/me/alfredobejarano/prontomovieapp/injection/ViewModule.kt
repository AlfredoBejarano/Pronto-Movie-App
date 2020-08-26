package me.alfredobejarano.prontomovieapp.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.alfredobejarano.prontomovieapp.view.MainActivity
import me.alfredobejarano.prontomovieapp.view.MovieListFragment

/**
 * ViewModule
 */
@Module
abstract class ViewModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment
}