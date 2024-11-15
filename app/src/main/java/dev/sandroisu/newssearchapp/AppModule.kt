package dev.sandroisu.newssearchapp

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sandroisu.api.NewsApi
import dev.sandroisu.api.utils.NewsApiKeyInterceptor
import dev.sandroisu.common.AndroidLogcatLogger
import dev.sandroisu.common.AppDispatchers
import dev.sandroisu.common.Logger
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.news.database.newsDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient? {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(NewsApiKeyInterceptor(BuildConfig.NEWS_API_KEY))
                .build()
        } else {
            null
        }
    }

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient
        )
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return newsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers {
        return AppDispatchers()
    }

    @Provides
    @Singleton
    fun provideAppLogger(): Logger {
        return AndroidLogcatLogger()
    }


}