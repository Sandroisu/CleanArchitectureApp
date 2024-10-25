package dev.sandroisu.news.data

interface MergeStrategy<E> {

    fun merge(right: E, left: E): E

}

private class RequestResultMergeStrategy<T> : MergeStrategy<RequestResult<T>> {
    override fun merge(right: RequestResult<T>, left: RequestResult<T>): RequestResult<T> {
        when {
            right is RequestResult.InProgress && left is RequestResult.InProgress -> merge(
                right = right,
                left = left,
            )

            else -> error("Not implemented yet")
        }

    }

    private fun merge(
        right: RequestResult.InProgress<T>,
        left: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return when {
            (left.data != null) -> RequestResult.InProgress(data = left.data)
            else -> RequestResult.InProgress(data = right.data)
        }
    }

    private fun <>merge(
        right: RequestResult.Success<T>,
        left: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return when {
            (left.data != null) -> RequestResult.InProgress(data = left.data)
            else -> RequestResult.InProgress(data = right.data)
        }
    }
}
