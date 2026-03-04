package com.me.recipe.ui.addrecipe.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ColumnScope.IngredientsSection(
    ingredientText: String,
    ingredientsList: ImmutableList<String>,
    onValueChange: (String) -> Unit,
    onAddIngredientsClicked: OnClick,
    onRemoveIngredientsClicked: (String) -> Unit,
) {
    AddIngredientsRow(ingredientText, onValueChange, onAddIngredientsClicked)
    IngredientsChips(ingredientsList, onRemoveIngredientsClicked)
}

@Composable
private fun IngredientsChips(
    ingredientsList: ImmutableList<String>,
    onRemoveIngredientsClicked: (String) -> Unit,
) {
    FlowRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ingredientsList.fastForEach { item ->
            IngredientChip(onRemoveIngredientsClicked = onRemoveIngredientsClicked, item = item)
        }
    }
}

@Composable
private fun IngredientChip(
    onRemoveIngredientsClicked: (String) -> Unit,
    item: String,
) {
    AssistChip(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
        onClick = { onRemoveIngredientsClicked(item) },
        label = {
            Text(
                text = item,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onRemoveIngredientsClicked(item) },
            )
        },
    )
}

@Composable
private fun AddIngredientsRow(
    ingredientText: String,
    onValueChange: (String) -> Unit,
    onAddIngredientsClicked: OnClick,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        IngredientsInput(
            value = ingredientText,
            onValueChange = onValueChange,
        )
        Spacer(Modifier.width(8.dp))
        AddButton(onAddIngredientsClicked)
    }
}

@Composable
private fun AddButton(onAddIngredientsClicked: () -> Unit) {
    Button(
        onClick = onAddIngredientsClicked,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(top = 8.dp)
            .height(57.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

@Composable
private fun RowScope.IngredientsInput(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Ingredients") },
        placeholder = { Text("e.g. Eggs, Milk, Flour...") },
        modifier = Modifier.weight(1f),
        maxLines = 1,
    )
}

@Preview
@Composable
private fun IngredientsSectionPreview() {
    RecipeTheme(true) {
        Column {
            IngredientsSection(
                ingredientText = "",
                ingredientsList = persistentListOf("Milk", "Pasta"),
                onValueChange = {},
                onAddIngredientsClicked = {},
                onRemoveIngredientsClicked = {},
            )
        }
    }
}
