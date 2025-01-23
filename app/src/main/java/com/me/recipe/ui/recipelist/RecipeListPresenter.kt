package com.me.recipe.ui.recipelist

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.me.recipe.ui.search.SearchScreen
import com.me.recipe.ui.search.SearchUiEvent
import com.me.recipe.ui.search.SearchViewPresenter
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class RecipeListPresenter @AssistedInject constructor(
    @Assisted private val screen: RecipeListScreen,
    @Assisted private val navigator: Navigator,
    searchPresenterFactory: SearchViewPresenter.Factory,
) : Presenter<RecipeListUiState> {

    private val searchPresenter = searchPresenterFactory.create(
        screen = SearchScreen(title = null),
        navigator = navigator,
    )

    @SuppressLint("FlowOperatorInvokedInComposition")
    @Composable
    override fun present(): RecipeListUiState {
        val searchState = searchPresenter.present()
        LaunchedEffect(screen.query) {
            searchState.eventSink.invoke(SearchUiEvent.SetQueryForRecipeListPage(screen.query))
        }
        return RecipeListUiState(
            query = screen.query,
            recipes = searchState.recipes,
            loading = searchState.loading,
            appendingLoading = searchState.appendingLoading,
            selectedCategory = searchState.selectedCategory,
            categoryScrollPosition = searchState.categoryScrollPosition,
            message = searchState.message,
            errors = searchState.errors,
            eventSink = { event ->
                when (event) {
                    RecipeListUiEvent.OnNavigateBackClicked ->
                        navigator.pop()
                    RecipeListUiEvent.ClearMessage ->
                        searchState.eventSink.invoke(SearchUiEvent.ClearMessage)
                    is RecipeListUiEvent.OnRecipeClick ->
                        searchState.eventSink.invoke(SearchUiEvent.OnRecipeClick(event.recipe))
                    is RecipeListUiEvent.OnRecipeLongClick ->
                        searchState.eventSink.invoke(SearchUiEvent.OnRecipeLongClick(event.title))
                    is RecipeListUiEvent.OnChangeRecipeScrollPosition ->
                        searchState.eventSink.invoke(SearchUiEvent.OnChangeRecipeScrollPosition(event.index))
                }
            },
        )
    }

    @CircuitInject(RecipeListScreen::class, SingletonComponent::class)
    @AssistedFactory
    interface FactoryMorePresenter {
        fun create(screen: RecipeListScreen, navigator: Navigator): RecipeListPresenter
    }
}


