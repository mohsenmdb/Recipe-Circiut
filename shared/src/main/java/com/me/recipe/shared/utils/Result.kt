package com.me.recipe.shared.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Error

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Error -> this.exception
            else -> null
        }

    val isLoading
        get() = this is Loading
    fun getOrNull(): T? =
        when {
            this is Success -> this.data
            else -> null
        }

    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

inline fun <R> runAsResultFlow(crossinline block: suspend () -> R): Flow<Result<R>> {
    return flow<Result<R>> {
        emit(Result.Success(block()))
    }.onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

suspend inline fun <R> runAsResult(crossinline block: suspend () -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Error(e)
    }
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
        is Result.Loading -> this
    }
}
