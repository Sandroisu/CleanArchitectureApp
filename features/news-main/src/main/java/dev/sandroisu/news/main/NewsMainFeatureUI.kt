package dev.sandroisu.news.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewsMain(){
    NewsMain(newsMainViewModel = viewModel())
}


@Composable
internal fun NewsMain(newsMainViewModel: NewsMainViewModel = viewModel()) {
    val state by newsMainViewModel.state.collectAsState()
    when(val currentState = state) {
        is State.Success -> Articles(currentState)
        is State.Error -> TODO()
        is State.Loading -> TODO()
        State.None -> TODO()
    }
}


@Composable
private fun Articles(state: State.Success) {
    LazyColumn {
        items(state.articlesUI){ article ->
            key(article.id) {
                ArticleItem(article)
            }
        }
    }
}

@Composable
private fun ArticleItem(article: ArticleUI) {
    TODO("Not yet implemented")
}
