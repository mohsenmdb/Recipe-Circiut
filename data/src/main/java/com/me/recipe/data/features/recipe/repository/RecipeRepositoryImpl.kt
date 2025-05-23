package com.me.recipe.data.features.recipe.repository

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.data.core.di.IoDispatcher
import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.network.features.recipe.RecipeApi
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val entityMapper: RecipeEntityMapper,
    private val recipeDtoMapper: RecipeDtoMapper,
    @IoDispatcher private var ioDispatcher: CoroutineDispatcher,
) : RecipeRepository {
    override fun getRecipe(
        recipeId: Int,
        uid: String,
    ): Flow<Recipe> = flow {
        // just to show loading, cache is fast
        delay(1000)

        var recipe = getRecipeFromCache(recipeId = recipeId, uid = uid)

        if (recipe != null) {
            emit(recipe)
        } else {
            // if the recipe is null, it means it was not in the cache for some reason. So get from network.
            val networkRecipe = getRecipeFromNetwork(recipeId)
            // insert into cache
            recipeDao.insertRecipe(
                entityMapper.mapFromDomainModel(networkRecipe),
            )
            // get from cache
            recipe = getRecipeFromCache(recipeId = recipeId, uid = uid)

            // emit and finish
            if (recipe != null) {
                emit(recipe)
            } else {
                throw Exception("Unable to get recipe from the cache.")
            }
        }
    }.flowOn(ioDispatcher)

    private suspend fun getRecipeFromCache(recipeId: Int, uid: String): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity, uid)
        }
    }

    private suspend fun getRecipeFromNetwork(recipeId: Int): Recipe {
        val recipes = recipeApi.get(recipeId)
        return recipeDtoMapper.mapToDomainModel(recipes)
    }
}
