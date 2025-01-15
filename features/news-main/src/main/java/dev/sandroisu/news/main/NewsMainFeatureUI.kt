@file:Suppress("UnusedParameter")

package dev.sandroisu.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.sandroisu.news.NewsTheme

@Composable
fun NewsMainScreen(modifier: Modifier = Modifier) {
    NewsMainScreen(newsMainViewModel = viewModel(), modifier = modifier)
}

@Composable
internal fun NewsMainScreen(
    newsMainViewModel: NewsMainViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val state by newsMainViewModel.state.collectAsState()
    val currentState = state

    NewsMainContent(currentState, modifier = modifier)
}

@Composable
private fun NewsMainContent(currentState: State, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        when (currentState) {
            is State.Error -> ErrorMessage(currentState)
            is State.Loading -> ProgressIndicator(currentState)
            is State.Success -> Articles(articlesState = currentState)
            State.None -> Unit
        }
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(NewsTheme.colorScheme.error)
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = state.toString(), color = NewsTheme.colorScheme.onError)
        }
        val articles = state.articlesUI
        if (articles != null) {

        }
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

