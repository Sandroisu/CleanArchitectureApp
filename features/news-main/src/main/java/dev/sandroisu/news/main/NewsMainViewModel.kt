package dev.sandroisu.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sandroisu.news.data.ArticlesRepository
import dev.sandroisu.news.data.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

internal class NewsMainViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
) : ViewModel() {
    private val state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
        .map { articles ->
            articles.toState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun forceUpdate(){
    }

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

    class Loading(val articles: List<Article>? = null) : State()

    class Error(val articles: List<Article>? = null) : State()

    class Success(val articles: List<Article>) : State()
}
