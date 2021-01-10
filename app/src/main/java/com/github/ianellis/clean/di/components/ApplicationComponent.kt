package com.github.ianellis.clean.di.components

import android.app.Application
import com.github.ianellis.clean.App
import com.github.ianellis.clean.appframework.catfacts.di.CatFactsFrameworkModule
import com.github.ianellis.clean.appframework.networking.di.NetworkingModule
import com.github.ianellis.clean.catfacts.di.modules.ApplicationActivityModule
import com.github.ianellis.clean.data.catfacts.di.CatFactsDataModule
import com.github.ianellis.clean.presentation.di.ViewModelFactoryModule
import com.ianellis.github.clean.domain.common.catfacts.summary.di.CatFactDomainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    NetworkingModule::class,
    ViewModelFactoryModule::class,
    ApplicationActivityModule::class,

    CatFactsDataModule::class,
    CatFactDomainModule::class,
    CatFactsFrameworkModule::class,

])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: App)
}
