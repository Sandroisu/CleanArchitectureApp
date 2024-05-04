package dev.sandroisu.news.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.sandroisu.news.database.models.ArticleDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM articles")
    fun getAll(): Flow<List<ArticleDBO>>

    @Insert
    suspend fun insert(articles: List<ArticleDBO>)

    @Delete
    suspend fun remove(articles: List<ArticleDBO>)

    @Query("DELETE FROM articles")
    suspend fun clear()
}
