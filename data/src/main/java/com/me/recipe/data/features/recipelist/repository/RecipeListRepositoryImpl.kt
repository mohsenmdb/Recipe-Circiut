package com.me.recipe.data.features.recipelist.repository

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.cache.recipe.model.RecipeEntity
import com.me.recipe.data.core.di.IoDispatcher
import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_CATEGORY_PAGE_SIZE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import com.me.recipe.shared.utils.RECIPE_SLIDER_PAGE_SIZE
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper,
    @IoDispatcher private var ioDispatcher: CoroutineDispatcher,
) : RecipeListRepository {

    override fun search(page: Int, query: String, size: Int): Flow<ImmutableList<Recipe>> = flow {
        val recipes = getRecipesFromNetwork(page = page, query = query, size = size)
        recipeDao.insertRecipes(entityMapper.toEntityList(recipes))
        val cachedRecipes = getCachedRecipes(query = query, page = page)
        val list = entityMapper.toDomainList(cachedRecipes).toPersistentList()
        emit(list)
    }.flowOn(ioDispatcher)

    override fun categoriesRecipes(categories: ImmutableList<FoodCategory>): Flow<ImmutableList<CategoryRecipe>> =
        flow {
            emit(getOfflineCategories(categories))
            emit(getCategoriesFromNetwork(categories))
        }.flowOn(ioDispatcher)

    override fun slider(): Flow<ImmutableList<Recipe>> = flow {
        val recipes = getRecipesFromNetwork(page = 1, size = RECIPE_SLIDER_PAGE_SIZE, query = "")
        emit(recipes.toPersistentList())
    }.flowOn(ioDispatcher)

    override fun restore(page: Int, query: String): Flow<ImmutableList<Recipe>> =
        flow {
            // just to show pagination, api is fast
            delay(1000)
            val cacheResult = restoreCachedRecipes(query, page)
            val list = entityMapper.toDomainList(cacheResult).toPersistentList()
            emit(list)
        }.flowOn(ioDispatcher)


    override suspend fun getTopRecipe(): Recipe {
        return try {
            getRecipesFromNetwork(1, "", 1).firstOrNull() ?: Recipe.EMPTY
        } catch (e: Exception) {
            Recipe.EMPTY
        }
    }

    private suspend fun getCategoriesFromNetwork(categories: ImmutableList<FoodCategory>): PersistentList<CategoryRecipe> =
        buildList {
            categories.forEach { category ->
                val apiResponse = getRecipesFromNetwork(
                    page = 1,
                    query = category.name,
                    size = RECIPE_CATEGORY_PAGE_SIZE,
                )
                recipeDao.insertRecipes(entityMapper.toEntityList(apiResponse))

                val cacheResult = recipeDao.searchRecipes(
                    pageSize = RECIPE_CATEGORY_PAGE_SIZE,
                    query = category.name,
                    page = 1,
                )
                val recipes = entityMapper.toDomainList(cacheResult).toPersistentList()
                if (recipes.isEmpty()) return@forEach
                add(CategoryRecipe(category, recipes))
            }
        }.toPersistentList()

    private suspend fun getOfflineCategories(categories: ImmutableList<FoodCategory>): PersistentList<CategoryRecipe> =
        buildList {
            categories.forEach { category ->
                val cacheResult = recipeDao.searchRecipes(
                    pageSize = RECIPE_CATEGORY_PAGE_SIZE,
                    query = category.name,
                    page = 1,
                )
                val recipes = entityMapper.toDomainList(cacheResult).toPersistentList()
                if (recipes.isEmpty()) return@forEach
                add(CategoryRecipe(category, recipes))
            }
        }.toPersistentList()

    private suspend fun getRecipesFromNetwork(page: Int, query: String, size: Int): List<Recipe> {
        val recipes = recipeApi.search(page = page, query = query, size = size).data.results
        val mappedRecipes = dtoMapper.toDomainList(recipes)
        return mappedRecipes
    }


    private suspend fun getCachedRecipes(query: String, page: Int): List<RecipeEntity> =
        if (query.isBlank()) {
            recipeDao.getAllRecipes(pageSize = RECIPE_PAGINATION_PAGE_SIZE, page = page)
        } else {
            recipeDao.searchRecipes(
                pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                page = page,
                query = query,
            )
        }

    private suspend fun restoreCachedRecipes(query: String, page: Int): List<RecipeEntity> =
        if (query.isBlank()) {
            recipeDao.restoreAllRecipes(pageSize = RECIPE_PAGINATION_PAGE_SIZE, page = page)
        } else {
            recipeDao.restoreRecipes(
                query = query,
                pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                page = page,
            )
        }
}
