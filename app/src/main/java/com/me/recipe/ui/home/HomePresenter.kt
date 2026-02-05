package com.me.recipe.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastFilter
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipelist.usecases.CategoriesRecipesUseCase
import com.me.recipe.domain.util.ForceFresh
import com.me.recipe.shared.datastore.SettingsDataStore
import com.me.recipe.shared.utils.CategoryRowType
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.ui.recipe.RecipeUiScreen
import com.me.recipe.ui.recipelist.RecipeListScreen
import com.me.recipe.util.errorformater.ErrorFormatter
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class HomePresenter @AssistedInject constructor(
    @Assisted private val screen: HomeUiScreen,
    @Assisted internal val navigator: Navigator,
    private val settingsDataStore: Lazy<SettingsDataStore>,
    private val getCategoriesUseCase: Lazy<CategoriesRecipesUseCase>,
    private val getOfflineCategoriesUseCase: Lazy<CategoriesRecipesUseCase>,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = remember { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)
        var refresher by remember { mutableStateOf(ForceFresh.refresh()) }

        LaunchedEffect(key1 = refresher) {
            getCategoriesUseCase.get().invoke(CategoriesRecipesUseCase.Params(forceRefresh = refresher))
            getOfflineCategoriesUseCase.get().invoke(CategoriesRecipesUseCase.Params(isOffline = true))
        }
        val isDarkTheme by remember { settingsDataStore.get().isDark }
        val categoryRows: Result<ImmutableList<CategoryRecipe>>? by getCategoriesUseCase.get().flow.collectAsState(initial = null)
        val offlineRows: Result<ImmutableList<CategoryRecipe>>? by getOfflineCategoriesUseCase.get().flow.collectAsState(initial = null)
        val slider = categoryRows?.getOrNull()?.firstOrNull { it.rowType == CategoryRowType.SLIDER }?.recipes

        val rows by remember(categoryRows?.getOrNull(), categoryRows?.exceptionOrNull()) {
            mutableStateOf(
                if (categoryRows?.getOrNull() != null) {
                    categoryRows?.getOrNull()?.fastFilter { it.rowType == CategoryRowType.ROW }?.toPersistentList()
                } else {
                    offlineRows?.getOrNull()?.fastFilter { it.rowType == CategoryRowType.ROW }?.toPersistentList()
                },
            )
        }

        LaunchedEffect(categoryRows?.exceptionOrNull()) {
            if (categoryRows?.exceptionOrNull() != null) {
                val error = errorFormatter.get().format(categoryRows?.exceptionOrNull())
                uiMessageManager.emitMessage(UiMessage.createSnackbar(error, actionText = R.string.try_again))
            }
        }

        return HomeUiState(
            sliderRecipes = slider,
            categoriesRecipes = rows,
            isDark = isDarkTheme,
            message = message,
            eventSink = { event ->
                when (event) {
                    is HomeUiEvent.OnRecipeClicked -> {
                        navigator.goTo(
                            RecipeUiScreen(
                                itemImage = event.recipe.image,
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
                    HomeUiEvent.OnRetryClicked -> stableScope.launch {
                        refresher = ForceFresh.refresh()
                    }
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
