package dev.sandroisu.newssearchapp.ui.theme

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sandroisu.news.data.ArticlesRepository
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.news.database.newsDatabase
import dev.sandroisu.newsapi.NewsApi
import dev.sandroisu.newssearchapp.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
        )
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return newsDatabase(context)
    }


}