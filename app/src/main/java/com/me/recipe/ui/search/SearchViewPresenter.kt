package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.getFoodCategory
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
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewPresenter @AssistedInject constructor(
    @Assisted private val screen: SearchScreen,
    @Assisted internal val navigator: Navigator,
    private val searchRecipesUsecase: Lazy<SearchRecipesUsecase>,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        val stableScope = rememberStableCoroutineScope()

        var recipeListPage by remember { mutableIntStateOf(RECIPE_PAGINATION_FIRST_PAGE) }
        var recipeScrollPosition by remember { mutableIntStateOf(INITIAL_RECIPE_LIST_POSITION) }
        var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
        var searchQuery by remember { mutableStateOf("") }
        var categoriesScrollPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }
        LaunchedEffect(key1 = searchQuery) {
            searchRecipesUsecase.get().invoke(SearchRecipesUsecase.Params(query = searchQuery))
        }
        val recipes by searchRecipesUsecase.get().flow.collectAsState(initial = null)
        val recipesResult = recipes?.getOrNull()
        var appendedRecipes by remember { mutableStateOf<ImmutableList<Recipe>>(persistentListOf()) }
        LaunchedEffect(recipesResult) {
            recipesResult?.let { newRecipes ->
                appendedRecipes = (appendedRecipes + newRecipes).toPersistentList()
            }
        }
        Timber.d("SearchViewPresenter appendedRecipes = $recipesResult")
        fun isNewSearchSetBySelectingFromCategoryList(): Boolean {
            return selectedCategory?.value == searchQuery
        }
        fun resetSearchState() {
            appendedRecipes = persistentListOf()
            recipeListPage = RECIPE_PAGINATION_FIRST_PAGE
            recipeScrollPosition = INITIAL_RECIPE_LIST_POSITION
//            if (!isNewSearchSetBySelectingFromCategoryList()) {
//                selectedCategory = null
//            }
        }
        fun onSelectedCategoryChanged(category: String, position: Int, offset: Int) {
            resetSearchState()
            selectedCategory = getFoodCategory(category)
            searchQuery = category
            categoriesScrollPosition = position to offset
        }
        return SearchUiState(
            recipes = appendedRecipes,
            loading = appendedRecipes.isEmpty() && recipes?.exceptionOrNull() == null,
            selectedCategory = selectedCategory,
            eventSink = { event ->
                when (event) {
                    is SearchUiEvent.OnSelectedCategoryChanged -> {
                        onSelectedCategoryChanged(event.category, event.position, event.offset)
                    }
                    SearchUiEvent.NewSearchEvent -> TODO()
                    is SearchUiEvent.OnChangeRecipeScrollPosition -> TODO()
                    is SearchUiEvent.OnQueryChanged -> TODO()
                    is SearchUiEvent.OnRecipeClick -> TODO()
                    is SearchUiEvent.OnRecipeLongClick -> TODO()
                    SearchUiEvent.RestoreStateEvent -> TODO()
                    SearchUiEvent.SearchClearEvent -> TODO()
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
