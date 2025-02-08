package dev.sandroisu.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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





