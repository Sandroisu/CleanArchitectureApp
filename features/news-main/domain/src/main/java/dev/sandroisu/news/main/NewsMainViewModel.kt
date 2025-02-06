package dev.sandroisu.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sandroisu.news.data.RequestResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
        is RequestResult.Error -> State.Error(articlesUI = data?.toImmutableList())
        is RequestResult.InProgress -> State.Loading(articlesUI = data?.toImmutableList())
        is RequestResult.Success -> State.Success(articlesUI = data.toImmutableList())
    }
}


public sealed class State(public open val articlesUI: ImmutableList<ArticleUI>?) {

    public data object None : State(null)


    public class Loading(articlesUI: ImmutableList<ArticleUI>? = null) : State(articlesUI)


    public class Error(articlesUI: ImmutableList<ArticleUI>? = null) : State(articlesUI)


    public class Success(override val articlesUI: ImmutableList<ArticleUI>) : State(articlesUI)
}
