package dev.sandroisu.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sandroisu.news.database.dao.ArticleDAO
import dev.sandroisu.news.database.models.ArticleDBO
import dev.sandroisu.news.database.utils.Converters

class NewsDatabase internal constructor(private val database: NewsRoomDatabase) {
    val articlesDao: ArticleDAO
        get() = database.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticleDAO
}

fun newsDatabase(applicationContext: Context): NewsDatabase {
    val newsRoomDatabase =
        Room.databaseBuilder(
            checkNotNull(applicationContext.applicationContext),
            NewsRoomDatabase::class.java,
            "news",
        ).build()
    return NewsDatabase(newsRoomDatabase)
}
