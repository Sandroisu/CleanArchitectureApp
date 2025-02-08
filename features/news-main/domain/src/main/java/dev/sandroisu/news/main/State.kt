package dev.sandroisu.news.main

import kotlinx.collections.immutable.ImmutableList

public sealed class State(public open val articlesUI: ImmutableList<ArticleUI>?) {

    public data object None : State(null)


    public class Loading(articlesUI: ImmutableList<ArticleUI>? = null) : State(articlesUI)


    public class Error(articlesUI: ImmutableList<ArticleUI>? = null) : State(articlesUI)


    public class Success(override val articlesUI: ImmutableList<ArticleUI>) : State(articlesUI)
}
