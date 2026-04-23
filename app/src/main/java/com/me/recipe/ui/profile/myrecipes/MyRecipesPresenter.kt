package com.me.recipe.ui.profile.myrecipes

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

class MyRecipesPresenter @AssistedInject constructor(
    @Assisted private val screen: MyRecipesScreen,
    @Assisted private val navigator: Navigator,
    searchPresenterFactory: SearchPresenter.Factory,
) : Presenter<MyRecipesState> {

    private val searchPresenter = searchPresenterFactory.create(
        screen = SearchScreen(onlyMyRecipes = true),
        navigator = navigator,
    )

    @SuppressLint("FlowOperatorInvokedInComposition")
    @Composable
    override fun present(): MyRecipesState {
        val searchState = searchPresenter.present()
        return MyRecipesState(
            items = searchState.items,
            message = searchState.message,
            errors = searchState.errors,
            eventSink = { event ->
                when (event) {
                    MyRecipesEvent.OnNavigateBackClicked ->
                        navigator.pop()

                    MyRecipesEvent.ClearMessage ->
                        searchState.eventSink.invoke(SearchEvent.ClearMessage)

                    is MyRecipesEvent.OnRecipeClick ->
                        searchState.eventSink.invoke(SearchEvent.OnRecipeClick(event.recipe))

                    is MyRecipesEvent.OnRecipeLongClick ->
                        searchState.eventSink.invoke(SearchEvent.OnRecipeLongClick(event.title))

                    MyRecipesEvent.OnRetryClicked ->
                        searchState.eventSink.invoke(SearchEvent.OngRetryClicked)

                    MyRecipesEvent.OnAppendingRetryClicked ->
                        searchState.eventSink.invoke(SearchEvent.OnAppendingRetryClicked)
                }
            },
        )
    }

    @CircuitInject(MyRecipesScreen::class, SingletonComponent::class)
    @AssistedFactory
    interface FactoryMorePresenter {
        fun create(screen: MyRecipesScreen, navigator: Navigator): MyRecipesPresenter
    }
}
