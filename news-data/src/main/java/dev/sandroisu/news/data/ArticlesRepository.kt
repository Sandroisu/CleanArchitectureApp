package dev.sandroisu.news.data

import dev.sandroisu.news.data.model.Article
import dev.sandroisu.news.data.model.toArticle
import dev.sandroisu.news.database.NewsDatabase
import dev.sandroisu.newsapi.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
    fun getAll(): RequestResult<Flow<List<Article>>> {
        return RequestResult.InProgress(
            database.articlesDao.getAll().map { articles ->
                articles.map { it.toArticle() }
            },
        )
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
