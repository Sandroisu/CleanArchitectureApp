package dev.sandroisu.news.data.model

import dev.sandroisu.news.database.models.ArticleDBO
import dev.sandroisu.newsapi.models.ArticleDTO
import java.util.Date

internal fun ArticleDBO.toArticle(): Article {
    return Article(0, Source("", ""), "", "", "", "", "", Date(), "")
}

internal fun ArticleDTO.toArticleDbo(): ArticleDBO {

}
