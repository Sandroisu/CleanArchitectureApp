package dev.sandroisu.news.data

import dev.sandroisu.news.data.model.Article
import dev.sandroisu.news.data.model.toArticle
import dev.sandroisu.news.data.model.toArticleDbo
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.news.database.models.ArticleDBO
import dev.sandroisu.newsapi.NewsApi
import dev.sandroisu.newsapi.models.ArticleDTO
import kotlinx.coroutines.flow.Flow
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
                    successResult.requireData()
                        .map { articleDTO -> articleDTO.toArticleDbo() }
                        .let { RequestResult.Success<List<ArticleDBO>?>(it) }
                }.onEach { requestResult ->
                    database.articlesDao.insert(requestResult.requireData())
                }

        return flow { remoteArticles.map { it.data?.onEach { adbo -> adbo.toArticle() } } }
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
    }

    private fun getAllFromDatabaseSilently(): Flow<RequestResult.Success<List<ArticleDBO>>> {
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
                successResult.requireData()
                    .map { articleDTO -> articleDTO.toArticleDbo() }
                    .let { RequestResult.Success<List<ArticleDBO>?>(it) }
            }.onEach { requestResult ->
                database.articlesDao.insert(requestResult.requireData())
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

fun <T : Any> RequestResult<T?>.requireData(): T {
    return checkNotNull(data)
}

internal fun <I, O> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
}
