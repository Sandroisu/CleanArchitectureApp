package dev.sandroisu.newssearchapp.ui.theme

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sandroisu.newsapi.NewsApi
import dev.sandroisu.newssearchapp.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun newsApi(): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = TODO(),
            json = TODO()
        )
    }
}