package com.me.recipe.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.CategoriesRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.CategoriesRecipesUsecase2
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

class MainViewPresenter @AssistedInject constructor(
    @Assisted private val screen: MainUiScreen,
    @Assisted internal val navigator: Navigator,
    private val getRecipesUsecase: Lazy<CategoriesRecipesUsecase2>,
) : Presenter<MainUiState> {

    @Composable
    override fun present(): MainUiState {
        val stableScope = rememberStableCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            getRecipesUsecase.get().invoke(CategoriesRecipesUsecase2.Params(getAllFoodCategories()))
        }

        val categoryRows: Result<ImmutableList<CategoryRecipe>>? by getRecipesUsecase.get().flow.collectAsState(initial = null)

        Timber.d("categoriesRecipes getOrNull= ${categoryRows?.getOrNull()}")

        navigator.toString()
        return MainUiState(
            sliderRecipes = persistentListOf(Recipe.testData()),
            categoriesRecipes = categoryRows?.getOrNull() ?: persistentListOf(),
            eventSink = { event ->
                when (event) {
                    OnPlayClicked -> {}
                }
            },
        )
    }
}

@CircuitInject(MainUiScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(screen: MainUiScreen, navigator: Navigator): MainViewPresenter
}
