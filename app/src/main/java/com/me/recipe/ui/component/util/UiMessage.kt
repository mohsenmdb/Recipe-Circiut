package com.me.recipe.ui.component.util

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

sealed interface Message {
    val message: String
    data class Dialog(override val message: String) : Message
    data class Toast(override val message: String) : Message
}

data class UiMessage(
    val message: Message,
    val throwable: Throwable? = null,
    val id: Long = UUID.randomUUID().mostSignificantBits,
) {
    companion object {
        fun createDialog(
            t: Throwable,
            id: Long = UUID.randomUUID().mostSignificantBits,
        ): UiMessage = UiMessage(
            message = Message.Dialog(t.message ?: "Error occurred: $t"),
            throwable = t,
            id = id,
        )

        fun createDialog(
            message: String,
            id: Long = UUID.randomUUID().mostSignificantBits,
        ): UiMessage = UiMessage(
            message = Message.Dialog(message),
            id = id,
        )

        fun createToast(
            t: Throwable,
            id: Long = UUID.randomUUID().mostSignificantBits,
        ): UiMessage = UiMessage(
            message = Message.Toast(t.message ?: "Error occurred: $t"),
            throwable = t,
            id = id,
        )

        fun createToast(
            message: String,
            id: Long = UUID.randomUUID().mostSignificantBits,
        ): UiMessage = UiMessage(
            message = Message.Toast(message),
            id = id,
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

