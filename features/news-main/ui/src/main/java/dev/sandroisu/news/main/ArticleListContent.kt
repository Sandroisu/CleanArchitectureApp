package dev.sandroisu.news.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.sandroisu.news.NewsTheme


@Composable
internal fun Articles(articlesState: State.Success) {
    ArticleList(articlesUi = articlesState.articlesUI)
}

@Preview
@Composable
internal fun ArticleList(
    @PreviewParameter(ArticlesUIPreviewProvider::class) articlesUi: List<ArticleUI>
) {
    LazyColumn {
        items(articlesUi) { article ->
            key(article.id) {
                Article(article)
            }
        }
    }
}

@Preview
@Composable
private fun Article(@PreviewParameter(ArticleUIPreviewProvider::class) article: ArticleUI) {
    Row(Modifier.padding(bottom = 4.dp)) {
        article.imageUrl?.let {
            var isImageVisible by remember { mutableStateOf(true) }
            if (isImageVisible) {
                AsyncImage(
                    model = article.imageUrl,
                    onError = {
                        isImageVisible = false
                              },
                    contentDescription = stringResource(R.string.nmain_article_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp),
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.padding(8.dp)) {
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
                    url = "https://www.lipsum.com/",
                ),
                ArticleUI(
                    id = 2L,
                    title = "Title",
                    description = "Here can be some long text",
                    imageUrl = "https://picsum.photos/seed/picsum/200/300",
                    url = "https://www.lipsum.com/",
                ),
                ArticleUI(
                    id = 3L,
                    title = "Title",
                    description = "Here can be some long text",
                    imageUrl = "https://picsum.photos/seed/picsum/200/300",
                    url = "https://www.lipsum.com/",
                ),
            ),
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
                url = "https://www.lipsum.com/",
            ),
            ArticleUI(
                id = 2L,
                title = "Title",
                description = "Here can be some long text",
                imageUrl = "https://picsum.photos/seed/picsum/200/300",
                url = "https://www.lipsum.com/",
            ),
            ArticleUI(
                id = 3L,
                title = "Title",
                description = "Here can be some long text",
                imageUrl = "https://picsum.photos/seed/picsum/200/300",
                url = "https://www.lipsum.com/",
            ),
        )
}

