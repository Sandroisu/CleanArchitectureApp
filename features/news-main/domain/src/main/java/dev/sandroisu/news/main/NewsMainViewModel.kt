package dev.sandroisu.news.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
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
public class NewsMainViewModel @Inject internal constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
) : ViewModel() {

    private companion object {
        const val DEFAULT_QUERY = "android"
    }

    public val state: StateFlow<State> = getAllArticlesUseCase.get().invoke(query = DEFAULT_QUERY)
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

@Stable
public sealed class State(public open val articlesUI: List<ArticleUI>?) {
    @Immutable
    public data object None : State(null)

    @Stable
    public class Loading(articlesUI: List<ArticleUI>? = null) : State(articlesUI)

    @Stable
    public class Error(articlesUI: List<ArticleUI>? = null) : State(articlesUI)

    @Stable
    public class Success(override val articlesUI: List<ArticleUI>) : State(articlesUI)
}
