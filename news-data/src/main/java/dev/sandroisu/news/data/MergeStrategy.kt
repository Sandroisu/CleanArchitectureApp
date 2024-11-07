package dev.sandroisu.news.data

interface MergeStrategy<E> {

    fun merge(cache: E, server: E): E

}

class RequestResultMergeStrategy<T: Any> : MergeStrategy<RequestResult<T>> {
    override fun merge(cache: RequestResult<T>, server: RequestResult<T>): RequestResult<T> {
       return when {
            cache is RequestResult.InProgress && server is RequestResult.InProgress -> merge(
                cache = cache,
                server = server,
            )
            cache is RequestResult.Success && server is RequestResult.InProgress -> merge(
                cache = cache,
                server = server,
            )
            cache is RequestResult.InProgress && server is RequestResult.Success -> merge(
                cache = cache,
                server = server,
            )
            cache is RequestResult.Success && server is RequestResult.Error -> merge(
                cache = cache,
                server = server,
            )

            else -> error("Not implemented yet")
        }

    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return when {
            (server.data != null) -> RequestResult.InProgress(data = server.data)
            else -> RequestResult.InProgress(data = cache.data)
        }
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(cache.data)
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(server.data)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(cache.data, server.error)
    }
}