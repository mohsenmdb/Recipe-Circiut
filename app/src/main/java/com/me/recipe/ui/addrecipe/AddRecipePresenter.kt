package com.me.recipe.ui.addrecipe

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.me.recipe.ui.component.util.UiMessageManager
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch

class AddRecipePresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<AddRecipeState> {

    @Composable
    override fun present(): AddRecipeState {
        val scope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)
        var title by rememberRetained { mutableStateOf("") }
        var description by rememberRetained { mutableStateOf("") }
        var ingredients by rememberRetained { mutableStateOf("") }
        var imageUri by rememberRetained { mutableStateOf<Uri?>(null) }
        val isLoading by rememberRetained { mutableStateOf(false) }
        val isSubmitEnabled = title.isNotBlank() &&
            description.isNotBlank() &&
            ingredients.isNotBlank() &&
            imageUri != null

        return AddRecipeState(
            title = title,
            imageUri = imageUri,
            description = description,
            ingredients = ingredients,
            isLoading = isLoading,
            isSubmitEnabled = isSubmitEnabled,
            message = message,
            eventSink = { event ->
                when (event) {
                    AddRecipeEvent.ClearMessage -> scope.launch { uiMessageManager.clearMessage() }
                    is AddRecipeEvent.OnTitleChanged -> title = event.value
                    is AddRecipeEvent.OnDescriptionChanged -> description = event.value
                    is AddRecipeEvent.OnIngredientsChanged -> ingredients = event.value
                    is AddRecipeEvent.OnImageSelected -> imageUri = event.uri
                    AddRecipeEvent.OnSubmitClicked -> TODO()
                }
            },
        )
    }
}

@CircuitInject(AddRecipeScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): AddRecipePresenter
}
