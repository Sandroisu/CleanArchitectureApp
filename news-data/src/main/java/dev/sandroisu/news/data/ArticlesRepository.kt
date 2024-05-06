package dev.sandroisu.news.data

import dev.sandroisu.news.data.model.Article
import dev.sandroisu.news.data.model.toArticle
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.newsapi.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
    fun getAll(): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles =
            database.articlesDao
                .getAll()
                .map { articles ->
                    articles.map { it.toArticle() }
                }
        val remoteArticles =
            flow {
                emit(api.everything())
            }.map { result ->
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    response.articles
                } else {
                    throw result.exceptionOrNull() ?: IOException("Unknown error")
                }
            }.map { articlesDtos ->
                articlesDtos.map { articleDto -> articleDto.toArticleDbo() }
            }

        cachedAllArticles.combine(remoteArticles)
        return flow {
            RequestResult.InProgress(
                cachedAllArticles,
            )
        }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
    }
}

sealed class RequestResult<E>(protected val data: E?) {
    class InProgress<E>(data: E?) : RequestResult<E>(data)

    class Success<E>(data: E?) : RequestResult<E>(data)

    class Error<E>(data: E?) : RequestResult<E>(data)
}
