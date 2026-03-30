package com.me.recipe.domain.features.search.model

import com.me.recipe.domain.features.recipe.model.Recipe
import kotlinx.collections.immutable.ImmutableList

data class Vitrine(
    val items: ImmutableList<Recipe>,
    val nextPage: Int,
)
