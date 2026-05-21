package com.me.recipe.data.features.recipe.store

import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.utils.Cache
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.shared.utils.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder

interface TodayRecipeDataSource

class RemoteTodayRecipeDataSource @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDtoMapper: RecipeDtoMapper,
) : TodayRecipeDataSource {
    suspend fun getTodayRecipe(): Recipe {
        val recipe = recipeApi.get(20)
        return recipeDtoMapper.mapToDomainModel(recipe.data!!)
    }
}

class TodayRecipeStore @Inject constructor(
    private val remote: RemoteTodayRecipeDataSource,
    private val cache: Cache<Unit, Recipe, Recipe>,
    @IoDispatcher private val io: CoroutineDispatcher,
) : Store<Unit, Recipe> by StoreBuilder.from<Unit, Recipe, Recipe>(
    fetcher = Fetcher.of<Unit, Recipe> {
        withContext(io) { remote.getTodayRecipe() }
    },
    sourceOfTruth = SourceOfTruth.of<Unit, Recipe, Recipe>(
        reader = cache::read,
        writer = cache::write,
        delete = cache::delete,
        deleteAll = cache::deleteAll,
    ),
).build()
