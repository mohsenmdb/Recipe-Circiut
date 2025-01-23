package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.RestoreRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.domain.util.ForceFresh
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import com.me.recipe.shared.utils.getFoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.PositiveAction
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.ui.recipe.RecipeUiScreen
import com.me.recipe.ui.search.SearchUiEvent.ClearMessage
import com.me.recipe.ui.search.SearchUiEvent.NewSearchEvent
import com.me.recipe.ui.search.SearchUiEvent.OnChangeRecipeScrollPosition
import com.me.recipe.ui.search.SearchUiEvent.OnQueryChanged
import com.me.recipe.ui.search.SearchUiEvent.OnRecipeClick
import com.me.recipe.ui.search.SearchUiEvent.OnRecipeLongClick
import com.me.recipe.ui.search.SearchUiEvent.OnSelectedCategoryChanged
import com.me.recipe.ui.search.SearchUiEvent.SearchClearEvent
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewPresenter @AssistedInject constructor(
    @Assisted private val screen: SearchScreen,
    @Assisted internal val navigator: Navigator,
    private val searchRecipesUsecase: Lazy<SearchRecipesUsecase>,
    private val restoreRecipesUsecase: Lazy<RestoreRecipesUsecase>,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = remember { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)
        var errorDialogInfo by remember { mutableStateOf<GenericDialogInfo?>(null) }
        var forceRefresher by remember { mutableStateOf<ForceFresh?>(null) }

        var recipeListPage by rememberSaveable { mutableIntStateOf(RECIPE_PAGINATION_FIRST_PAGE) }
        var recipeScrollPosition by rememberSaveable { mutableIntStateOf(INITIAL_RECIPE_LIST_POSITION) }
        var selectedCategory by rememberSaveable { mutableStateOf<FoodCategory?>(null) }
        var searchText by rememberSaveable { mutableStateOf("") }
        var query by rememberSaveable { mutableStateOf(screen.query) }
        var categoriesScrollPosition by rememberSaveable { mutableStateOf(0 to 0) }
        val recipes by searchRecipesUsecase.get().flow.collectAsState(initial = null)
        val restoredRecipes by restoreRecipesUsecase.get().flow.collectAsState(initial = null)
        val recipesResult = recipes?.getOrNull()
        val restoredRecipesResult = restoredRecipes?.getOrNull()
        var appendedRecipes by remember { mutableStateOf<ImmutableList<Recipe>>(persistentListOf()) }
        var appendingLoading by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(key1 = query, key2 = recipeListPage, key3 = forceRefresher) {
            if (recipeListPage > 1 && appendedRecipes.isEmpty()) {
                restoreRecipesUsecase.get().invoke(RestoreRecipesUsecase.Params(query = query, page = recipeListPage))
                return@LaunchedEffect
            }

            searchRecipesUsecase.get().invoke(SearchRecipesUsecase.Params(query = query, page = recipeListPage, refresher = forceRefresher))
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
        var loading by remember(appendedRecipes, recipes?.exceptionOrNull()) { mutableStateOf(appendedRecipes.isEmpty() && recipes?.exceptionOrNull() == null) }

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
                                loading = true
                                errorDialogInfo = null
                            },
                        ),
                    )
                    .onDismiss { errorDialogInfo = null }
                    .build()
            }
        }
        fun resetSearchState() {
            appendedRecipes = persistentListOf()
            recipeListPage = RECIPE_PAGINATION_FIRST_PAGE
            recipeScrollPosition = INITIAL_RECIPE_LIST_POSITION
        }
        fun onSelectedCategoryChanged(category: String, position: Int, offset: Int) {
            resetSearchState()
            selectedCategory = getFoodCategory(category)
            query = category
            categoriesScrollPosition = position to offset
        }
        fun onNewSearchEvent() {
            selectedCategory = null
            resetSearchState()
            query = searchText
        }
        fun navigateToRecipePage(recipe: Recipe) {
            navigator.goTo(
                RecipeUiScreen(
                    itemImage = recipe.featuredImage,
                    itemTitle = recipe.title,
                    itemId = recipe.id,
                    itemUid = recipe.uid,
                ),
            )
        }
        fun checkReachEndOfTheList(position: Int): Boolean {
            if ((position + 1) < (recipeListPage * RECIPE_PAGINATION_PAGE_SIZE) || loading) return false
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
        return SearchUiState(
            recipes = appendedRecipes,
            loading = loading,
            appendingLoading = appendingLoading,
            selectedCategory = selectedCategory,
            categoryScrollPosition = categoriesScrollPosition,
            query = searchText,
            message = message,
            errors = errorDialogInfo,
            eventSink = { event ->
                when (event) {
                    is OnSelectedCategoryChanged -> {
                        onSelectedCategoryChanged(event.category, event.position, event.offset)
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
                        Timber.d("OnRecipeLongClick ${event.title}")
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
        fun create(screen: SearchScreen, navigator: Navigator): SearchViewPresenter
    }
    companion object {
        const val INITIAL_RECIPE_LIST_POSITION = 0
    }
}
