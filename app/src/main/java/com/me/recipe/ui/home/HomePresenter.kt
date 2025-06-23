package com.me.recipe.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.CategoriesRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SliderRecipesUsecase
import com.me.recipe.shared.datastore.SettingsDataStore
import com.me.recipe.shared.utils.getAllFoodCategories
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.ui.recipe.RecipeUiScreen
import com.me.recipe.ui.recipelist.RecipeListScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

class HomePresenter @AssistedInject constructor(
    @Assisted private val screen: HomeUiScreen,
    @Assisted internal val navigator: Navigator,
    private val settingsDataStore: Lazy<SettingsDataStore>,
    private val getRecipesUsecase: Lazy<CategoriesRecipesUsecase>,
    private val sliderRecipesUsecase: Lazy<SliderRecipesUsecase>,
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = remember { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)

        LaunchedEffect(key1 = Unit) {
            getRecipesUsecase.get().invoke(CategoriesRecipesUsecase.Params(getAllFoodCategories()))
            sliderRecipesUsecase.get().invoke(Unit)
        }
        val categoryRows: Result<ImmutableList<CategoryRecipe>>? by
            getRecipesUsecase.get().flow.collectAsState(initial = null)
        val sliders: Result<ImmutableList<Recipe>>? by
            sliderRecipesUsecase.get().flow.collectAsRetainedState(initial = null)
        val isDarkTheme by remember { settingsDataStore.get().isDark }

        return HomeUiState(
            sliderRecipes = sliders?.getOrNull(),
            categoriesRecipes = categoryRows?.getOrNull(),
            isDark = isDarkTheme,
            message = message,
            eventSink = { event ->
                when (event) {
                    is HomeUiEvent.OnRecipeClicked -> {
                        navigator.goTo(
                            RecipeUiScreen(
                                itemImage = event.recipe.featuredImage,
                                itemTitle = event.recipe.title,
                                itemId = event.recipe.id,
                                itemUid = event.recipe.uid,
                            ),
                        )
                    }

                    is HomeUiEvent.OnCategoryClicked -> {
                        navigator.goTo(RecipeListScreen(event.category.value))
                    }
                    is HomeUiEvent.OnRecipeLongClick -> {
                        stableScope.launch { uiMessageManager.emitMessage(UiMessage.createSnackbar(event.title)) }
                    }
                    HomeUiEvent.ToggleDarkTheme -> settingsDataStore.get().toggleTheme()
                    HomeUiEvent.ClearMessage -> stableScope.launch { uiMessageManager.clearMessage() }
                }
            },
        )
    }
}

@CircuitInject(HomeUiScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(screen: HomeUiScreen, navigator: Navigator): HomePresenter
}
