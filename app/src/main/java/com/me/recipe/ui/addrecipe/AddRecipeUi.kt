package com.me.recipe.ui.addrecipe

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.ui.addrecipe.components.AddRecipeTopBar
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(AddRecipeScreen::class, SingletonComponent::class)
@Composable
internal fun AddRecipeUi(
    state: AddRecipeState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val containerColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
    )
    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = state.message,
        onClearMessage = { state.eventSink(AddRecipeEvent.ClearMessage) },
    )

    Scaffold(
        containerColor = containerColor,
        snackbarHost = {
            DefaultSnackbar(
                snackbarHostState = snackbarHostState,
                onAction = { snackbarHostState.currentSnackbarData?.performAction() },
            )
        },
        topBar = {
            AddRecipeTopBar()
        }
    ) { padding ->
        Content(
            state = state,
            modifier = modifier.padding(padding),
        )
    }
}



@Composable
private fun Content(state: AddRecipeState, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {

        TitleInput(
            value = state.title,
            onValueChange = { state.eventSink(AddRecipeEvent.OnTitleChanged(it)) }
        )

        DescriptionInput(
            value = state.description,
            onValueChange = { state.eventSink(AddRecipeEvent.OnDescriptionChanged(it)) }
        )

        IngredientsInput(
            value = state.ingredients,
            onValueChange = { state.eventSink(AddRecipeEvent.OnIngredientsChanged(it)) }
        )

        ImagePickerSection(
            imageUri = state.imageUri,
            onImageSelected = { state.eventSink(AddRecipeEvent.OnImageSelected(it)) }
        )

        SubmitButton(
            isLoading = state.isLoading,
            enabled = state.isSubmitEnabled,
            onClick = { state.eventSink(AddRecipeEvent.OnSubmitClicked) }
        )
    }
}

@Composable
private fun TitleInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Title") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun DescriptionInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Description") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        maxLines = 5
    )
}
@Composable
private fun IngredientsInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Ingredients") },
        placeholder = { Text("e.g. Eggs, Milk, Flour...") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        maxLines = 6
    )
}

@Composable
private fun ImagePickerSection(
    imageUri: Uri?,
    onImageSelected: (Uri?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text("Recipe Image", style = MaterialTheme.typography.titleMedium)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                CoilImage(
                    data = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("No image selected")
            }
        }

        ImagePickerButton(
            onClick = { /* trigger image picker */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun ImagePickerButton(
    onClick: OnClick,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = "Select Image")
    }
}

@Composable
private fun SubmitButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text("Add Recipe")
        }
    }
}

@Preview
@Composable
private fun AddRecipeScreenPreview() {
    RecipeTheme(true) {
        AddRecipeUi(
            state = AddRecipeState(
                eventSink = {},
            ),
        )
    }
}
