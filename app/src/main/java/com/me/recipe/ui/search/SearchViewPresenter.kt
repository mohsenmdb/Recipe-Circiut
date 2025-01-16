package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
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
import timber.log.Timber

class SearchViewPresenter @AssistedInject constructor(
    @Assisted private val screen: SearchScreen,
    @Assisted internal val navigator: Navigator,
    private val searchRecipesUsecase: Lazy<SearchRecipesUsecase>,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        val stableScope = rememberStableCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            searchRecipesUsecase.get().invoke(SearchRecipesUsecase.Params())
        }
        val recipes by searchRecipesUsecase.get().flow.collectAsState(initial = null)
        val recipesResult = recipes?.getOrNull()
        val appendedRecipes by remember { mutableStateOf<ImmutableList<Recipe>>(persistentListOf()) }
        LaunchedEffect(recipesResult) {
            recipesResult?.let { newRecipes ->
                appendedRecipes.toMutableList().addAll(newRecipes)
            }
        }
        Timber.d("RecipePresenter recipe = $appendedRecipes")

        navigator.toString()
        return SearchUiState(
            recipes = appendedRecipes,
            eventSink = { event ->
                when (event) {
                    SearchUiEvent.NewSearchEvent -> TODO()
                    is SearchUiEvent.OnChangeRecipeScrollPosition -> TODO()
                    is SearchUiEvent.OnQueryChanged -> TODO()
                    is SearchUiEvent.OnRecipeClick -> TODO()
                    is SearchUiEvent.OnRecipeLongClick -> TODO()
                    is SearchUiEvent.OnSelectedCategoryChanged -> TODO()
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
