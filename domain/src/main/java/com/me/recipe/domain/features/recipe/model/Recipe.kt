package com.me.recipe.domain.features.recipe.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class Recipe(
    val id: Int,
    val uid: String,
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: String,
    val sourceUrl: String,
    val ingredients: ImmutableList<String>,
    val date: String,
    val dateTimestamp: Long,
) {
    companion object {
        val EMPTY = Recipe(
            id = -1,
            uid = "",
            title = "",
            publisher = "",
            featuredImage = "",
            rating = "",
            sourceUrl = "",
            ingredients = persistentListOf(),
            date = "",
            dateTimestamp = 0L,
        )

        fun testData() = Recipe(
            id = 1,
            uid = "uid",
            title = "Thai Soup",
            publisher = "Thai Kitchen",
            featuredImage = "Url",
            rating = "15",
            sourceUrl = "Url",
            ingredients = persistentListOf("Soup", "Meat", "Rice"),
            date = "Nov 12 2025",
            dateTimestamp = 0L,
        )
    }
}
