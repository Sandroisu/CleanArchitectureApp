package dev.sandroisu.news.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewsMain() {
    NewsMain(newsMainViewModel = viewModel())
}


@Composable
internal fun NewsMain(newsMainViewModel: NewsMainViewModel = viewModel()) {
    val state by newsMainViewModel.state.collectAsState()
    when (val currentState = state) {
        is State.Success -> Articles(currentState)
        is State.Error -> TODO()
        is State.Loading -> TODO()
        State.None -> TODO()
    }
}


@Composable
private fun Articles(state: State.Success) {
    LazyColumn {
        items(state.articlesUI) { article ->
            key(article.id) {
                Article(article)
            }
        }
    }
}

@Preview
@Composable
private fun Article(@PreviewParameter(ArticleUIPreviewProvider::class) article: ArticleUI) {
    Column {
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
        )
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
        )
    }
}

private class ArticleUIPreviewProvider : PreviewParameterProvider<ArticleUI>{
    override val values: Sequence<ArticleUI>
        get() = sequenceOf(
            ArticleUI(
                id = 1L,
                title = "Title",
                description = "Here can be some long text",
                imageUrl = "https://picsum.photos/seed/picsum/200/300",
                url = "https://www.lipsum.com/"
            ),
            ArticleUI(
                id = 2L,
                title = "Title",
                description = "Here can be some long text",
                imageUrl = "https://picsum.photos/seed/picsum/200/300",
                url = "https://www.lipsum.com/"
            ),
            ArticleUI(
                id = 3L,
                title = "Title",
                description = "Here can be some long text",
                imageUrl = "https://picsum.photos/seed/picsum/200/300",
                url = "https://www.lipsum.com/"
            )
        )

}
