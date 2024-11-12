package dev.sandroisu.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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

@Composable
fun NewsMainScreen() {
    NewsMainScreen(newsMainViewModel = viewModel())
}


@Composable
internal fun NewsMainScreen(newsMainViewModel: NewsMainViewModel = viewModel()) {
    val state by newsMainViewModel.state.collectAsState()
    when (val currentState = state) {
        is State.Success -> Articles(currentState.articlesUI)
        is State.Error -> ArticlesWithError(articles = currentState.articlesUI)
        is State.Loading -> ArticlesDuringUpdate(articles = currentState.articlesUI)
        State.None -> NewsEmpty()
    }
}

@Composable
private fun ArticlesWithError(articles: List<ArticleUI>?) {
    Column {
        Box(
            Modifier
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error during update", color = MaterialTheme.colorScheme.onError)
        }
        if (articles != null) {
            Articles(articlesUI = articles)
        }
    }
}

@Composable
private fun ArticlesDuringUpdate(articles: List<ArticleUI>?) {
    Column {
        Box(Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        if (articles != null) {
            Articles(articlesUI = articles)
        }
    }
}

@Composable
private fun NewsEmpty() {
    Box(contentAlignment = Alignment.Center) {
        Text("No news")
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
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyMedium,
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
