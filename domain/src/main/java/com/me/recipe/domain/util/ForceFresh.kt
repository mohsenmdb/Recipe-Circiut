package com.me.recipe.domain.util

data class ForceFresh(
    private val forceFresh: Boolean = false,
    private val requestAtMs: Long = 0
) {
    fun shouldRefresh() =
        forceFresh && (System.currentTimeMillis() - requestAtMs) < 1_000

    companion object {
        fun refresh(): ForceFresh {
            return ForceFresh(forceFresh = true, requestAtMs = System.currentTimeMillis())
        }
    }
}
