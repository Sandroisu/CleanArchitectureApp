package dev.sandroisu.news.data

import dev.sandroisu.news.data.model.Article
import dev.sandroisu.news.data.model.toArticle
import dev.sandroisu.news.data.model.toArticleDbo
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.news.database.models.ArticleDBO
import dev.sandroisu.newsapi.NewsApi
import dev.sandroisu.newsapi.models.ArticleDTO
import dev.sandroisu.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
    fun getAll(mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy()): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()
            .map { result ->
                result.map { articlesDbos ->
                    articlesDbos.map { it.toArticle() }
                }
            }
        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer()
            .map { requestResult: RequestResult<ResponseDTO<ArticleDTO>> ->
                requestResult.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
        return cachedAllArticles.combine(remoteArticles) { dboObjects: RequestResult<List<Article>>, dtoObjects: RequestResult<List<Article>> ->
            mergeStrategy.merge(dboObjects, dtoObjects)
        }.flatMapLatest { result ->
            if (result is RequestResult.Success) {
                database.articlesDao.observeAll()
                    .map { dbos -> dbos.map { it.toArticle() } }
                    .map { RequestResult.Success(it) }
            } else {
                flowOf(result)
            }
        }
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = flow { emit(database.articlesDao.getAll()) }
            .map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(result.getOrThrow().articles)
                }
            }.map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(apiRequest, start)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    fun fetchLatest(): Flow<RequestResult<List<Article>>>{
        TODO("Not yet implemented")
    }

}

sealed class RequestResult<out E : Any>(open val data: E? = null) {
    class InProgress<E : Any>(data: E? = null) : RequestResult<E>(data)

    class Success<E : Any>(override val data: E) : RequestResult<E>(data)
    class Error<E : Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>(data)
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Error -> RequestResult.Error()
        is RequestResult.InProgress -> RequestResult.InProgress(data?.let(mapper))
    }
}

internal fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> {
            error("Impossible branch")
        }
    }
}
