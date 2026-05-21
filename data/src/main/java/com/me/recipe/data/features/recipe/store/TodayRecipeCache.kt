package com.me.recipe.data.features.recipe.store

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.utils.Cache
import com.me.recipe.domain.features.recipe.model.Recipe
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodayRecipeCache @Inject constructor(
    val recipeDao: RecipeDao,
    val recipeEntityMapper: RecipeEntityMapper,
) : Cache<Unit, Recipe, Recipe> {

    override suspend fun write(key: Unit, response: Recipe) {
        recipeDao.updateRecipe(recipeEntityMapper.mapFromDomainModel(response, true))
    }

    override fun read(key: Unit): Flow<Recipe?> {
        return recipeDao.observeTodayRecipe().map { recipe ->
            recipe?.let { recipeEntityMapper.mapToDomainModel(recipe) }
        }
    }

    override suspend fun delete(key: Unit) {
    }

    override suspend fun deleteAll() {
    }
}
