package dev.sandroisu.news.data.model

import java.util.Date

public data class Article(
    val cacheId: Long,
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date,
    val content: String,
)

public data class Source(
    val id: String,
    val name: String?,
)
