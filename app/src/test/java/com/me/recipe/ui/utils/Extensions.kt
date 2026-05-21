package com.me.recipe.ui.utils

import app.cash.turbine.ReceiveTurbine
import com.me.recipe.ui.auth.AuthState
import dagger.Lazy

fun <T> lazyOfDagger(value: T): Lazy<T> = Lazy { value }

suspend fun ReceiveTurbine<AuthState>.awaitState(
    predicate: (AuthState) -> Boolean,
): AuthState {
    repeat(5) {
        val state = awaitItem()
        if (predicate(state)) return state
    }
    error("Expected matching AuthState within 5 emissions")
}
