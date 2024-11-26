package dev.sandroisu.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import dev.sandroisu.news.NewsTheme

@Composable
fun NewsMainScreen() {
    NewsMainScreen(newsMainViewModel = viewModel())
}


@Composable
internal fun NewsMainScreen(newsMainViewModel: NewsMainViewModel = viewModel()) {
    val state by newsMainViewModel.state.collectAsState()
    val currentState = state
    if (currentState != State.None) {
        NewsMainContent(currentState)
    }
}

@Composable
private fun NewsMainContent(currentState: State) {
    Column {
        if (currentState is State.Error) {
            ErrorMessage(currentState)
        }
        if (currentState is State.Loading) {
            ProgressIndicator(currentState)
        }
        if (currentState.articlesUI != null) {
            Articles(articlesUI = currentState.articlesUI)
        }
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(NewsTheme.colorScheme.error)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update", color = NewsTheme.colorScheme.onError)
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun Articles(@PreviewParameter(ArticlesUIPreviewProvider::class) articlesUI: List<ArticleUI>) {
    LazyColumn {
        items(articlesUI) { article ->
            key(article.id) {
                Article(article)
            }
        }
    }
}

@Preview
@Composable
private fun Article(@PreviewParameter(ArticleUIPreviewProvider::class) article: ArticleUI) {
    Column(modifier = Modifier.padding(8.dp)) {
        AsyncImage(model = article.imageUrl, contentDescription = "Article image")
        Text(
            text = article.title,
            style = NewsTheme.typography.headlineMedium,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = article.description,
            style = NewsTheme.typography.bodyMedium,
            maxLines = 3,
        )
    }
}


private class ArticlesUIPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {
    override val values: Sequence<List<ArticleUI>>
        get() = sequenceOf(
            listOf(
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
        )
}

private class ArticleUIPreviewProvider : PreviewParameterProvider<ArticleUI> {
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
