package com.github.ianellis.clean.appframework.networking.di

import android.app.Application
import com.github.ianellis.appframework.BuildConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkingModule {

    @Provides
    @Singleton
    fun providesRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl("https://cat-fact.herokuapp.com/")
            .build()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, CACHE_SIZE)
    }

    @SuppressWarnings("MagicNumber")
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(cache: Cache): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        return httpClient
            .addLogging()
            .cache(cache)
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    private fun OkHttpClient.Builder.addLogging(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            this.addInterceptor(interceptor)
        }
        return this
    }

    companion object {
        private const val CACHE_SIZE = 10L * 1024L * 1024L // 10 MiB
    }
}
