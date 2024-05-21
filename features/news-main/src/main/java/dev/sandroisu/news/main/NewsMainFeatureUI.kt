package dev.sandroisu.news.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
fun NewsMain() {
    @Composable
    fun NewsList(articles: List<Article>)  {
        LazyColumn {
        }
    }
}
