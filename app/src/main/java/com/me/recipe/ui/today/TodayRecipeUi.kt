package com.me.recipe.ui.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.ui.recipe.RecipeContent
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(TodayRecipeScreen::class, SingletonComponent::class)
@Composable
internal fun TodayRecipeUi(
    state: TodayRecipeState,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = stringResource(R.string.today_recommended_recipe),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 16.dp),
        )
        RecipeContent(
            isLoading = state.isLoading,
            message = state.message,
            recipe = state.recipe,
            clearMessage = { state.eventSink(TodayRecipeEvent.ClearMessage) },
            onLikeClicked = { state.eventSink(TodayRecipeEvent.OnLikeClicked) },
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    RecipeTheme {
        TodayRecipeUi(state = TodayRecipeState.testData())
    }
}
