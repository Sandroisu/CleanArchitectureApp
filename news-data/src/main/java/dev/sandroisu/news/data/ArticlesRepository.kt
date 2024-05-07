package dev.sandroisu.news.data

import dev.sandroisu.news.data.model.Article
import dev.sandroisu.news.data.model.toArticleDbo
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.news.database.models.ArticleDBO
import dev.sandroisu.newsapi.NewsApi
import dev.sandroisu.newsapi.models.ArticleDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
    fun getAll(): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult.Success<List<ArticleDBO>>> = getAllFromDatabase()
        val remoteArticles =
            flow {
                emit(api.everything())
            }.map { result ->
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    RequestResult.Success(response.articles)
                } else {
                    RequestResult.Error(null)
                }
            }.filterIsInstance<RequestResult.Success<List<ArticleDTO>?>>()
                .map { successResult ->
                    successResult.requreData()
                        .map { articleDTO -> articleDTO.toArticleDbo() }
                        .let { RequestResult.Success<List<ArticleDBO>?>(it) }
                }.onEach { requestResult ->
                    database.articlesDao.insert(requestResult.requreData())
                }

        cachedAllArticles.combine(remoteArticles)
        return flow {
            RequestResult.InProgress(
                cachedAllArticles,
            )
        }
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
    }

    private fun getAllFromServer(): Flow<RequestResult.Success<List<ArticleDBO>?>> {
        return flow {
            emit(api.everything())
        }.map { result ->
            if (result.isSuccess) {
                val response = result.getOrThrow()
                RequestResult.Success(response.articles)
            } else {
                RequestResult.Error(null)
            }
        }.filterIsInstance<RequestResult.Success<List<ArticleDTO>?>>()
            .map { successResult ->
                successResult.requreData()
                    .map { articleDTO -> articleDTO.toArticleDbo() }
                    .let { RequestResult.Success<List<ArticleDBO>?>(it) }
            }.onEach { requestResult ->
                database.articlesDao.insert(requestResult.requreData())
            }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
    }
}

sealed class RequestResult<E>(internal val data: E?) {
    class InProgress<E>(data: E?) : RequestResult<E>(data)

    class Success<E>(data: E?) : RequestResult<E>(data)

    class Error<E>(data: E?) : RequestResult<E>(data)
}

fun <T : Any> RequestResult<T?>.requreData(): T {
    return checkNotNull(data)
}

internal fun <I, O> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
}
