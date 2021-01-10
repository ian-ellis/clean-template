package com.github.ianellis.clean.presentation.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class ViewModelProviderModule {

    @Provides
    fun providesViewModelProvider(
        factory: ViewModelProvider.Factory,
        activity: AppCompatActivity
    ): ViewModelProvider {
        return ViewModelProvider(activity, factory)
    }
}
