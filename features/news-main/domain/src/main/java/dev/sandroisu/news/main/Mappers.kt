package dev.sandroisu.news.main

import dev.sandroisu.news.data.RequestResult
import dev.sandroisu.news.data.model.Article
import kotlinx.collections.immutable.toImmutableList

internal fun RequestResult<List<ArticleUI>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(articlesUI = data?.toImmutableList())
        is RequestResult.InProgress -> State.Loading(articlesUI = data?.toImmutableList())
        is RequestResult.Success -> State.Success(articlesUI = data.toImmutableList())
    }
}

internal fun Article.toUiArticles(): ArticleUI {
    return ArticleUI(
        id = cacheId,
        title = title,
        description = description,
        imageUrl = urlToImage,
        url = url,
    )
}
