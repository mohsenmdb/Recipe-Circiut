package com.me.recipe.ui.addrecipe

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.usecases.AddRecipeUseCase
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import recipe.app.core.errorformater.ErrorFormatter

class AddRecipePresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
    private val addRecipeUseCase: Lazy<AddRecipeUseCase>,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : Presenter<AddRecipeState> {

    @Composable
    override fun present(): AddRecipeState {
        val scope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)
        var title by rememberRetained { mutableStateOf("") }
        var description by rememberRetained { mutableStateOf("") }
        var ingredientText by rememberRetained { mutableStateOf("") }
        var ingredientsList by rememberRetained { mutableStateOf<PersistentList<String>>(persistentListOf()) }
        var imageUri by rememberRetained { mutableStateOf<Uri?>(null) }
        var isLoading by rememberRetained { mutableStateOf(false) }
        val isSubmitEnabled = title.isNotBlank() &&
            description.isNotBlank() &&
            ingredientsList.isNotEmpty() &&
            imageUri != null

        fun cleanForm() {
            title = ""
            description = ""
            ingredientText = ""
            ingredientsList = persistentListOf()
            imageUri = null
        }

        suspend fun sendRecipe() {
            isLoading = true
            addRecipeUseCase.get().invoke(
                AddRecipeUseCase.Params(
                    title = title,
                    description = description,
                    ingredients = ingredientsList.joinToString(","),
                    imageUri = imageUri!!,
                ),
            ).apply {
                getOrNull()?.let {
                    cleanForm()
                    uiMessageManager.emitMessage(UiMessage.createSnackbar(R.string.recipe_added))
                }
                exceptionOrNull()?.let {
                    uiMessageManager.emitMessage(UiMessage.createSnackbar(errorFormatter.get().format(it)))
                }
            }
            isLoading = false
        }

        return AddRecipeState(
            title = title,
            imageUri = imageUri,
            description = description,
            ingredientText = ingredientText,
            ingredientsList = ingredientsList,
            isLoading = isLoading,
            isSubmitEnabled = isSubmitEnabled,
            message = message,
            eventSink = { event ->
                when (event) {
                    AddRecipeEvent.ClearMessage -> scope.launch { uiMessageManager.clearMessage() }
                    is AddRecipeEvent.OnTitleChanged -> title = event.value
                    is AddRecipeEvent.OnDescriptionChanged -> description = event.value
                    is AddRecipeEvent.OnIngredientsChanged -> ingredientText = event.value
                    is AddRecipeEvent.OnImageSelected -> imageUri = event.uri
                    AddRecipeEvent.OnSubmitClicked -> scope.launch { sendRecipe() }
                    is AddRecipeEvent.OnRemoveIngredientsClicked -> ingredientsList = ingredientsList.remove(event.item)
                    AddRecipeEvent.OnAddIngredientsClicked -> {
                        ingredientsList = ingredientsList.add(ingredientText.trim().replaceFirstChar { it.uppercaseChar() })
                        ingredientText = ""
                    }
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
