package dev.sandroisu.news.main

import dev.sandroisu.news.data.ArticlesRepository
import dev.sandroisu.news.data.RequestResult
import dev.sandroisu.news.data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import dev.sandroisu.news.data.model.Article as DataArticle

internal class GetAllArticlesUseCase @Inject constructor(private val repository: ArticlesRepository) {
    operator fun invoke(): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toUiArticles() }
                }
            }
    }
}

private fun DataArticle.toUiArticles(): ArticleUI {
    return ArticleUI(id =10L,
        title = "Title",
        description = "Here can be some long text",
        imageUrl = "https://picsum.photos/seed/picsum/200/300",
        url = "https://www.lipsum.com/")
}

