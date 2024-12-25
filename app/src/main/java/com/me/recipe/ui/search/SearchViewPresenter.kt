package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.CategoriesRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SliderRecipesUsecase
import com.slack.circuit.retained.collectAsRetainedState
import com.me.recipe.shared.utils.getAllFoodCategories
import com.me.recipe.ui.home.MainUiEvent.OnPlayClicked
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
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        val stableScope = rememberStableCoroutineScope()

        navigator.toString()
        return SearchUiState(
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
