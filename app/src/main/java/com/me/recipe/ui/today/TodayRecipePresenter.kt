package com.me.recipe.ui.today

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.usecases.GetTodayRecipeUseCase
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class TodayRecipePresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
    private val getTodayRecipeUseCase: GetTodayRecipeUseCase,
) : Presenter<TodayRecipeState> {

    @Composable
    override fun present(): TodayRecipeState {
        val profilesResult by remember { getTodayRecipeUseCase() }.collectAsState(initial = null)
        return TodayRecipeState(
            recipe = profilesResult?.getOrNull() ?: Recipe.EMPTY,
            isLoading = profilesResult?.getOrNull() == null && profilesResult?.exceptionOrNull() == null,
            exception = profilesResult?.exceptionOrNull(),
            eventSink = {},
        )
    }
}

@CircuitInject(TodayRecipeScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): TodayRecipePresenter
}
