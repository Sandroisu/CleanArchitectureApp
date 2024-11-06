package dev.sandroisu.news.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewsMain(){
    NewsMain(newsMainViewModel = viewModel())
}

@Composable
internal fun NewsMain(newsMainViewModel: NewsMainViewModel = viewModel()) {

}
