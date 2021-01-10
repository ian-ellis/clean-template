package com.github.ianellis.clean.catfacts.di.modules

import androidx.appcompat.app.AppCompatActivity
import com.github.ianellis.clean.catfacts.summaries.di.modules.CatFactsSummariesNavigationModule
import com.github.ianellis.clean.presentation.catfacts.summary.di.CatFactsSummariesPresentationModule
import com.github.ianellis.clean.presentation.di.ViewModelProviderModule
import com.ianellis.github.clean.apppresentation.catfacts.summaries.CatFactSummariesActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ApplicationActivityModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelProviderModule::class,
            CatFactsSummariesPresentationModule::class,
            CatFactsSummariesNavigationModule::class
        ]
    )
    abstract fun bindCatFactSummariesActivity(): CatFactSummariesActivity

    @Binds
    abstract fun provideAppCompatActivity(activity: CatFactSummariesActivity): AppCompatActivity
}
