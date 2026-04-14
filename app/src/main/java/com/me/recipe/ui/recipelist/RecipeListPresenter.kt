package com.me.recipe.ui.recipelist

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.me.recipe.ui.search.SearchEvent
import com.me.recipe.ui.search.SearchPresenter
import com.me.recipe.ui.search.SearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.immutableListOf

class RecipeListPresenter @AssistedInject constructor(
    @Assisted private val screen: RecipeListScreen,
    @Assisted private val navigator: Navigator,
    searchPresenterFactory: SearchPresenter.Factory,
) : Presenter<RecipeListState> {

    private val searchPresenter = searchPresenterFactory.create(
        screen = SearchScreen(screen.query),
        navigator = navigator,
    )

    @SuppressLint("FlowOperatorInvokedInComposition")
    @Composable
    override fun present(): RecipeListState {
        val searchState = searchPresenter.present()
        return RecipeListState(
            query = screen.query,
            recipes = immutableListOf(),// Todo fix me
            isLoading = searchState.isLoading,
            isEmpty = searchState.isEmpty,
            appendingLoading = searchState.appendingLoading,
            selectedCategory = searchState.selectedCategory,
            message = searchState.message,
            errors = searchState.errors,
            eventSink = { event ->
                when (event) {
                    RecipeListEvent.OnNavigateBackClicked ->
                        navigator.pop()
                    RecipeListEvent.ClearMessage ->
                        searchState.eventSink.invoke(SearchEvent.ClearMessage)
                    is RecipeListEvent.OnRecipeClick ->
                        searchState.eventSink.invoke(SearchEvent.OnRecipeClick(event.recipe))
                    is RecipeListEvent.OnRecipeLongClick ->
                        searchState.eventSink.invoke(SearchEvent.OnRecipeLongClick(event.title))
                    is RecipeListEvent.OnChangeRecipeScrollPosition ->
                        searchState.eventSink.invoke(SearchEvent.OnChangeRecipeScrollPosition(event.index))
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
