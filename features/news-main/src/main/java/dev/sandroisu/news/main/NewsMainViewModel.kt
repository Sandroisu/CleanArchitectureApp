package dev.sandroisu.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sandroisu.news.data.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class NewsMainViewModel(private val getAllArticlesUseCase: GetAllArticlesUseCase) :
    ViewModel() {
    private val state: StateFlow<State> = getAllArticlesUseCase().map { articles ->
        articles.toState()
    }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

}


private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(articles = data)
        is RequestResult.Success -> State.Success(articles = checkNotNull(data))
    }
}

sealed class State {
    data object None : State()

    class Loading(val articles: List<Article>?) : State()

    class Error : State()

    class Success(val articles: List<Article>) : State()
}
