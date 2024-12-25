package dev.sandroisu.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sandroisu.news.data.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
   private val getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
) : ViewModel() {

    private companion object {
        const val DEFAULT_QUERY = "android"
    }

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke(query = DEFAULT_QUERY)
        .map { articles ->
            articles.toState()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)


}

private fun RequestResult<List<ArticleUI>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(articlesUI = data)
        is RequestResult.InProgress -> State.Loading(articlesUI = data)
        is RequestResult.Success -> State.Success(articlesUI = checkNotNull(data))
    }
}

internal sealed class State(val articlesUI: List<ArticleUI>?) {
    data object None : State(null)

    class Loading(articlesUI: List<ArticleUI>? = null) : State(articlesUI)

    class Error(articlesUI: List<ArticleUI>? = null) : State(articlesUI)

    class Success(articlesUI: List<ArticleUI>) : State(articlesUI)
}
