package dev.sandroisu.news.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class NewsMainViewModel : ViewModel() {
    private val _state = MutableStateFlow(State.None)

    val state: StateFlow<State>
        get() = _state.asStateFlow()
}

sealed class State {
    data object None : State()

    data object Loading : State()

    data object Error : State()

    data class Success(val articles: List<Article>) : State()
}
