package dev.sandroisu.news.data.model

import dev.sandroisu.api.models.ArticleDTO
import dev.sandroisu.api.models.SourceDTO
import dev.sandroisu.news.database.models.ArticleDBO
import dev.sandroisu.news.database.models.SourceDBO
import java.util.Date

internal fun ArticleDBO.toArticle(): Article {
    return Article(
        cacheId = id,
        source = sourceDBO?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}

internal fun ArticleDTO.toArticle(): Article {
    return Article(
        cacheId = -1,
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}

internal fun ArticleDTO.toArticleDbo(): ArticleDBO {
    return ArticleDBO(
        sourceDBO = source?.toSourceDBO(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )

}

private fun SourceDTO.toSource(): Source {
    return Source(
        id = id,
        name = name,
    )
}

private fun SourceDTO.toSourceDBO(): SourceDBO {
    return SourceDBO(
        id = id,
        name = name,
    )
}

private fun SourceDBO.toSource(): Source {
    return Source(
        id = id,
        name = name,
    )
}