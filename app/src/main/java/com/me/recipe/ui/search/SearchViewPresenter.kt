package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
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

        var recipeListPage by remember { mutableIntStateOf(RECIPE_PAGINATION_FIRST_PAGE) }
        var recipeScrollPosition by remember { mutableIntStateOf(INITIAL_RECIPE_LIST_POSITION) }
        var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
        var searchText by remember { mutableStateOf("") }
        var query by remember { mutableStateOf("") }
        var categoriesScrollPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }
        LaunchedEffect(key1 = query) {
            searchRecipesUsecase.get().invoke(SearchRecipesUsecase.Params(query = query))
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

        return SearchUiState(
            recipes = appendedRecipes,
            loading = appendedRecipes.isEmpty() && recipes?.exceptionOrNull() == null,
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
                    is SearchUiEvent.OnRecipeClick ->{
                        navigator.goTo(
                            RecipeUiScreen(
                                itemImage = event.recipe.featuredImage,
                                itemTitle = event.recipe.title,
                                itemId = event.recipe.id,
                                itemUid = event.recipe.uid
                            )
                        )
                    }
                    is SearchUiEvent.OnChangeRecipeScrollPosition -> {}
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
