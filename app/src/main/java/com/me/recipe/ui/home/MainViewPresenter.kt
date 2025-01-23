package com.me.recipe.ui.home

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
import com.me.recipe.ui.recipe.RecipeUiScreen
import com.me.recipe.ui.recipelist.RecipeListScreen
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

class MainViewPresenter @AssistedInject constructor(
    @Assisted private val screen: MainUiScreen,
    @Assisted internal val navigator: Navigator,
    private val getRecipesUsecase: Lazy<CategoriesRecipesUsecase>,
    private val sliderRecipesUsecase: Lazy<SliderRecipesUsecase>,
) : Presenter<MainUiState> {

    @Composable
    override fun present(): MainUiState {
        val stableScope = rememberStableCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            getRecipesUsecase.get().invoke(CategoriesRecipesUsecase.Params(getAllFoodCategories()))
            sliderRecipesUsecase.get().invoke(Unit)
        }
        val categoryRows: Result<ImmutableList<CategoryRecipe>>? by
        getRecipesUsecase.get().flow.collectAsState(initial = null)
        val sliders: Result<ImmutableList<Recipe>>? by
        sliderRecipesUsecase.get().flow.collectAsRetainedState(initial = null)

        return MainUiState(
            sliderRecipes = sliders?.getOrNull(),
            categoriesRecipes = categoryRows?.getOrNull(),
            eventSink = { event ->
                when (event) {
                    is MainUiEvent.OnRecipeClicked -> {
                        navigator.goTo(
                            RecipeUiScreen(
                                itemImage = event.recipe.featuredImage,
                                itemTitle = event.recipe.title,
                                itemId = event.recipe.id,
                                itemUid = event.recipe.uid
                            )
                        )
                    }

                    is MainUiEvent.OnCategoryClicked -> {
                        navigator.goTo(RecipeListScreen(event.category.value))
                    }
                    is MainUiEvent.OnRecipeLongClick -> {}
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
