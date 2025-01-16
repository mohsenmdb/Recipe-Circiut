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
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import com.me.recipe.shared.utils.getFoodCategory
import com.me.recipe.ui.recipe.RecipeUiScreen
import com.me.recipe.ui.search.SearchViewModel.Companion.INITIAL_RECIPE_LIST_POSITION
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
import timber.log.Timber

class SearchViewPresenter @AssistedInject constructor(
    @Assisted private val screen: SearchScreen,
    @Assisted internal val navigator: Navigator,
    private val searchRecipesUsecase: Lazy<SearchRecipesUsecase>,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        val stableScope = rememberStableCoroutineScope()
        var recipeListPage by rememberSaveable { mutableIntStateOf(RECIPE_PAGINATION_FIRST_PAGE) }
        var recipeScrollPosition by rememberSaveable { mutableIntStateOf(INITIAL_RECIPE_LIST_POSITION) }
        var selectedCategory by rememberSaveable { mutableStateOf<FoodCategory?>(null) }
        var searchText by rememberSaveable { mutableStateOf("") }
        var query by rememberSaveable { mutableStateOf("") }
        var categoriesScrollPosition by rememberSaveable { mutableStateOf<Pair<Int, Int>?>(null) }
        val recipes by searchRecipesUsecase.get().flow.collectAsState(initial = null)
        val recipesResult = recipes?.getOrNull()
        var appendedRecipes by remember { mutableStateOf<ImmutableList<Recipe>>(persistentListOf()) }
        var appendingLoading by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(key1 = query, key2 = recipeListPage) {
            searchRecipesUsecase.get().invoke(SearchRecipesUsecase.Params(query = query, page = recipeListPage))
        }
        LaunchedEffect(recipesResult) {
            recipesResult?.let { newRecipes ->
                appendedRecipes = (appendedRecipes + newRecipes).toPersistentList()
            }
            appendingLoading = false
        }
        val loading by rememberSaveable(appendedRecipes) { mutableStateOf(appendedRecipes.isEmpty() && recipes?.exceptionOrNull() == null) }

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
                    itemUid = recipe.uid
                )
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
            query = searchText,
            eventSink = { event ->
                when (event) {
                    is SearchUiEvent.OnSelectedCategoryChanged -> {
                        onSelectedCategoryChanged(event.category, event.position, event.offset)
                    }
                    is SearchUiEvent.OnQueryChanged -> {
                        searchText = event.query
                    }
                    SearchUiEvent.SearchClearEvent -> {
                        searchText = ""
                        query = ""
                    }
                    SearchUiEvent.NewSearchEvent -> onNewSearchEvent()
                    is SearchUiEvent.OnRecipeClick -> navigateToRecipePage(event.recipe)
                    is SearchUiEvent.OnChangeRecipeScrollPosition -> handleRecipeListPositionChanged(event.index)
                    is SearchUiEvent.OnRecipeLongClick -> {}
                    SearchUiEvent.RestoreStateEvent -> TODO()
                }
            },
        )
    }
}

@CircuitInject(SearchScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(screen: SearchScreen, navigator: Navigator): SearchViewPresenter
}
