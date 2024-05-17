package dev.sandroisu.news.main

import dev.sandroisu.news.data.ArticlesRepository
import dev.sandroisu.news.data.RequestResult
import dev.sandroisu.news.data.model.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {
    operator fun invoke(): Flow<RequestResult<List<Article>>> {
        return repository.getAll()
    }
}
