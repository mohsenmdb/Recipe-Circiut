package com.me.recipe.shared.data

data class DataState<out T>(
    val data: T? = null,
    val error: Throwable? = null,
    val loading: Boolean = false,
) {
    companion object {

        fun <T> success(
            data: T,
        ): DataState<T> = DataState(
            data = data,
        )

        fun <T> error(
            message: Throwable,
        ): DataState<T> = DataState(
            error = message,
        )

        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}
