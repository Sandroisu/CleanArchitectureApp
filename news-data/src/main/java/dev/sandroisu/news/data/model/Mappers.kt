package dev.sandroisu.news.data.model

import dev.sandroisu.api.models.ArticleDTO
import dev.sandroisu.news.database.models.ArticleDBO
import java.util.Date

internal fun ArticleDBO.toArticle(): Article {
    return Article(0, Source("", ""), "", "", "", "", "", Date(), "")
}

internal fun ArticleDTO.toArticle(): Article {
    return Article(0, Source("", ""), "", "", "", "", "", Date(), "")
}

internal fun ArticleDTO.toArticleDbo(): ArticleDBO {
    return ArticleDBO(0, dev.sandroisu.news.database.models.Source("", ""), "", "", "", "", "", Date(), "")

}
