package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.me.recipe.BuildConfig
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.RestoreRecipesUseCase
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUseCase
import com.me.recipe.domain.util.ForceFresh
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import com.me.recipe.shared.utils.getFoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.PositiveAction
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.ui.recipe.RecipeScreen
import com.me.recipe.ui.search.SearchEvent.ClearMessage
import com.me.recipe.ui.search.SearchEvent.NewSearchEvent
import com.me.recipe.ui.search.SearchEvent.OnChangeRecipeScrollPosition
import com.me.recipe.ui.search.SearchEvent.OnQueryChanged
import com.me.recipe.ui.search.SearchEvent.OnRecipeClick
import com.me.recipe.ui.search.SearchEvent.OnRecipeLongClick
import com.me.recipe.ui.search.SearchEvent.OnSelectedCategoryChanged
import com.me.recipe.ui.search.SearchEvent.SearchClearEvent
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import recipe.app.core.errorformater.ErrorFormatter

class SearchPresenter @AssistedInject constructor(
    @Assisted private val screen: SearchScreen,
    @Assisted internal val navigator: Navigator,
    private val searchRecipesUseCase: Lazy<SearchRecipesUseCase>,
    private val restoreRecipesUseCase: Lazy<RestoreRecipesUseCase>,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : Presenter<SearchState> {

    @Composable
    override fun present(): SearchState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)
        var errorDialogInfo by rememberRetained { mutableStateOf<GenericDialogInfo?>(null) }
        var forceRefresher by rememberRetained { mutableStateOf<ForceFresh?>(null) }

        var recipeListPage by rememberRetained { mutableIntStateOf(RECIPE_PAGINATION_FIRST_PAGE) }
        var recipeScrollPosition by rememberRetained { mutableIntStateOf(INITIAL_RECIPE_LIST_POSITION) }
        var selectedCategory by rememberRetained { mutableStateOf<FoodCategory?>(null) }
        var searchText by rememberRetained { mutableStateOf("") }
        var query by rememberRetained { mutableStateOf(screen.query) }
        val recipes by searchRecipesUseCase.get().flow.collectAsRetainedState(initial = null)
        val restoredRecipes by restoreRecipesUseCase.get().flow.collectAsRetainedState(initial = null)
        var recipesResult by remember(recipes?.getOrNull()) { mutableStateOf(recipes?.getOrNull()) }
        val restoredRecipesResult = restoredRecipes?.getOrNull()
        var appendedRecipes by rememberRetained { mutableStateOf<ImmutableList<Recipe>>(persistentListOf()) }
        var appendingLoading by rememberRetained { mutableStateOf(false) }
        var isLoading by remember(appendedRecipes, recipesResult, recipes?.exceptionOrNull()) {
            mutableStateOf(appendedRecipes.isEmpty() && recipesResult == null && recipes?.exceptionOrNull() == null)
        }
        LaunchedEffect(key1 = query, key2 = recipeListPage, key3 = forceRefresher) {
            if (BuildConfig.DEBUG) delay(1000)
            if (recipeListPage > 1 && appendedRecipes.isEmpty()) {
                restoreRecipesUseCase.get().invoke(RestoreRecipesUseCase.Params(query = query, page = recipeListPage))
                return@LaunchedEffect
            }

            searchRecipesUseCase.get().invoke(SearchRecipesUseCase.Params(query = query, page = recipeListPage, refresher = forceRefresher))
            isLoading = false
        }
        LaunchedEffect(recipesResult, restoredRecipesResult) {
            if (!restoredRecipesResult.isNullOrEmpty() && recipesResult.isNullOrEmpty()) {
                appendedRecipes = restoredRecipesResult
                return@LaunchedEffect
            }
            recipesResult?.let { newRecipes ->
                appendedRecipes = (appendedRecipes + newRecipes).toPersistentList()
            }
            appendingLoading = false
        }

        LaunchedEffect(recipes?.exceptionOrNull()) {
            if (recipes?.exceptionOrNull() != null) {
                errorDialogInfo = GenericDialogInfo.Builder()
                    .title(R.string.error)
                    .description(errorFormatter.get().format(recipes?.exceptionOrNull()))
                    .positive(
                        PositiveAction(
                            positiveBtnTxt = R.string.try_again,
                            onPositiveAction = {
                                forceRefresher = ForceFresh.refresh()
                                isLoading = true
                                errorDialogInfo = null
                            },
                        ),
                    )
                    .onDismiss { errorDialogInfo = null }
                    .build()
            }
        }
        fun resetSearchState() {
            recipesResult = null
            appendedRecipes = persistentListOf()
            recipeListPage = RECIPE_PAGINATION_FIRST_PAGE
            recipeScrollPosition = INITIAL_RECIPE_LIST_POSITION
        }
        fun onSelectedCategoryChanged(category: String) {
            resetSearchState()
            selectedCategory = getFoodCategory(category)
            query = category
        }
        fun onNewSearchEvent() {
            isLoading = true
            selectedCategory = null
            resetSearchState()
            query = searchText
        }
        fun navigateToRecipePage(recipe: Recipe) {
            navigator.goTo(
                RecipeScreen(
                    itemImage = recipe.image,
                    itemTitle = recipe.title,
                    itemId = recipe.id,
                    itemUid = recipe.uid,
                ),
            )
        }
        fun checkReachEndOfTheList(position: Int): Boolean {
            if ((position + 1) < (recipeListPage * RECIPE_PAGINATION_PAGE_SIZE) || isLoading) return false
            if ((recipeScrollPosition + 1) < (recipeListPage * RECIPE_PAGINATION_PAGE_SIZE)) return false
            return true
        }
        fun handleRecipeListPositionChanged(position: Int) {
            recipeScrollPosition = position
            if (checkReachEndOfTheList(position)) {
                appendingLoading = true
                recipeListPage += 1
            }
        }
        return SearchState(
            recipes = appendedRecipes,
            isLoading = isLoading,
            isEmpty = appendedRecipes.isEmpty(),
            appendingLoading = appendingLoading,
            selectedCategory = selectedCategory,
            query = searchText,
            message = message,
            errors = errorDialogInfo,
            eventSink = { event ->
                when (event) {
                    is OnSelectedCategoryChanged -> {
                        isLoading = true
                        onSelectedCategoryChanged(event.category)
                    }
                    is OnQueryChanged -> {
                        searchText = event.query
                    }
                    SearchClearEvent -> {
                        searchText = ""
                        query = ""
                    }
                    NewSearchEvent -> onNewSearchEvent()
                    is OnRecipeClick -> navigateToRecipePage(event.recipe)
                    is OnChangeRecipeScrollPosition -> handleRecipeListPositionChanged(event.index)
                    is OnRecipeLongClick -> {
                        stableScope.launch { uiMessageManager.emitMessage(UiMessage.createSnackbar(event.title)) }
                    }
                    ClearMessage -> stableScope.launch { uiMessageManager.clearMessage() }
                }
            },
        )
    }

    @CircuitInject(SearchScreen::class, SingletonComponent::class)
    @AssistedFactory
    interface Factory {
        fun create(screen: SearchScreen, navigator: Navigator): SearchPresenter
    }
    companion object {
        const val INITIAL_RECIPE_LIST_POSITION = 0
    }
}
