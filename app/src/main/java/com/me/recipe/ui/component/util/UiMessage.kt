package com.me.recipe.ui.component.util

import androidx.annotation.StringRes
import com.me.recipe.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

sealed interface Message {
    val text: String
    data class Toast(override val text: String) : Message
    data class Snackbar(override val text: String) : Message
}

data class UiMessage(
    val message: Message,
    val throwable: Throwable? = null,
    @StringRes val actionText: Int,
) {
    companion object {
        fun createToast(
            t: Throwable,
            @StringRes actionText: Int = R.string.ok,
        ): UiMessage = UiMessage(
            message = Message.Toast(t.message ?: "Error occurred: $t"),
            throwable = t,
            actionText = actionText,
        )

        fun createToast(
            message: String,
            @StringRes actionText: Int = R.string.ok,
        ): UiMessage = UiMessage(
            message = Message.Toast(message),
            actionText = actionText,
        )

        fun createSnackbar(
            t: Throwable,
            @StringRes actionText: Int = R.string.ok,
        ): UiMessage = UiMessage(
            message = Message.Snackbar(t.message ?: "Error occurred: $t"),
            throwable = t,
            actionText = actionText,
        )

        fun createSnackbar(
            message: String,
            @StringRes actionText: Int = R.string.ok,
        ): UiMessage = UiMessage(
            message = Message.Snackbar(message),
            actionText = actionText,
        )
    }
}

class UiMessageManager {
    private val mutex = Mutex()

    private val _message = MutableStateFlow<UiMessage?>(null)

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _message

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _message.value = message
        }
    }

    suspend fun clearMessage() {
        mutex.withLock {
            _message.value = null
        }
    }
}
