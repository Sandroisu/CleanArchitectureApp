package dev.sandroisu.news.data

import dev.sandroisu.api.NewsApi
import dev.sandroisu.api.models.ArticleDTO
import dev.sandroisu.api.models.ResponseDTO
import dev.sandroisu.common.Logger
import dev.sandroisu.news.data.model.Article
import dev.sandroisu.news.data.model.toArticle
import dev.sandroisu.news.data.model.toArticleDbo
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.news.database.models.ArticleDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

public class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val logger: Logger,
) {
    public fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()

        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer(query)
        return cachedAllArticles
            .combine(remoteArticles) { dbos: RequestResult<List<Article>>, dtos: RequestResult<List<Article>> ->
                mergeStrategy.merge(dbos, dtos)
            }.flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll().map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest =
            flow { emit(database.articlesDao.getAll()) }
                .map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> { RequestResult.Success(it) }
                .catch {
                    emit(RequestResult.Error(error = it))
                    logger.error(LOG_TAG, "Error ${it.message}")
                }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest).map { result ->
            result.map { dbos -> dbos.map { it.toArticle() } }
        }
    }

    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything(query = query)) }.onEach { result ->
            if (result.isSuccess) {
                saveNetResponseToCache(result.getOrThrow().articles)
            }
        }.onEach { result ->
            if (result.isFailure) {
                logger.error(LOG_TAG, "Error ${result.exceptionOrNull()?.message}")
            }
        }.map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(
            apiRequest,
            start,
        ).map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
            result.map { response -> response.articles.map { it.toArticle() } }
        }
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    public fun fetchLatest(): Flow<RequestResult<List<Article>>> {
        TODO("Not yet implemented")
    }

    private companion object {
        const val LOG_TAG = "ArticleRepository"
    }
}

public sealed class RequestResult<out E : Any>(public open val data: E? = null) {
    public class InProgress<E : Any>(data: E? = null) : RequestResult<E>(data)
    public class Success<E : Any>(override val data: E) : RequestResult<E>(data)
    public class Error<E : Any>(
        data: E? = null,
        public val error: Throwable? = null,
    ) : RequestResult<E>(data)
}

public fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Error -> RequestResult.Error(data?.let(mapper))
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
