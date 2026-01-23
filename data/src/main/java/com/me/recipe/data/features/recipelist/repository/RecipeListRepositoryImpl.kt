package com.me.recipe.data.features.recipelist.repository

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.cache.recipe.model.RecipeEntity
import com.me.recipe.data.core.di.IoDispatcher
import com.me.recipe.data.features.recipe.mapper.CategoryDtoMapper
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val recipeEntityMapper: RecipeEntityMapper,
    private val recipeDtoMapper: RecipeDtoMapper,
    private val categoryDtoMapper: CategoryDtoMapper,
    @IoDispatcher private var ioDispatcher: CoroutineDispatcher,
) : RecipeListRepository {

    override fun search(page: Int, query: String, size: Int): Flow<ImmutableList<Recipe>> = flow {
        val recipes = getRecipesFromNetwork(page = page, query = query, size = size)
        recipeDao.insertRecipes(recipeEntityMapper.toEntityList(recipes))
        val cachedRecipes = getCachedRecipes(query = query, page = page)
        val list = recipeEntityMapper.toDomainList(cachedRecipes).toPersistentList()
        emit(list)
    }.flowOn(ioDispatcher)

    override fun categoriesRecipes(categories: ImmutableList<FoodCategory>): Flow<ImmutableList<CategoryRecipe>> =
        flow {
            // To show loading
            delay(500)
            emit(getCategoriesFromNetwork())
        }.catch {
            emit(getOfflineCategories(categories))
            // TODO send a message to show this is offline data
        }.flowOn(ioDispatcher)

    override fun slider(): Flow<ImmutableList<Recipe>> = flow {
        val recipes = getRecipesFromNetwork(page = 1, size = RECIPE_SLIDER_PAGE_SIZE, query = "")
        emit(recipes.toPersistentList())
    }.flowOn(ioDispatcher)

    override fun restore(page: Int, query: String): Flow<ImmutableList<Recipe>> =
        flow {
            // just to show pagination, api is fast
            delay(500)
            val cacheResult = restoreCachedRecipes(query, page)
            val list = recipeEntityMapper.toDomainList(cacheResult).toPersistentList()
            emit(list)
        }.flowOn(ioDispatcher)


    override suspend fun getTopRecipe(): Recipe {
        return try {
            getRecipesFromNetwork(1, "", 1).firstOrNull() ?: Recipe.EMPTY
        } catch (e: Exception) {
            Recipe.EMPTY
        }
    }

    private suspend fun getCategoriesFromNetwork(): PersistentList<CategoryRecipe> {
        val response = recipeApi.categories()
        val categories = categoryDtoMapper.map(response)
        categories.forEach {
            recipeDao.insertRecipes(recipeEntityMapper.toEntityList(it.recipes))
        }
        return categories
    }

    private suspend fun getOfflineCategories(categories: ImmutableList<FoodCategory>): PersistentList<CategoryRecipe> =
        buildList {
            categories.forEach { category ->
                val cacheResult = getCachedCategories(category)
                val recipes = recipeEntityMapper.toDomainList(cacheResult).toPersistentList()
                if (recipes.isEmpty()) return@forEach
                add(CategoryRecipe(category, recipes))
            }
        }.toPersistentList()

    private suspend fun getRecipesFromNetwork(page: Int, query: String, size: Int): List<Recipe> {
        val recipes = recipeApi.search(page = page, query = query, size = size).data.results
        val mappedRecipes = recipeDtoMapper.toDomainList(recipes)
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

    private suspend fun getCachedCategories(category: FoodCategory): List<RecipeEntity> =
        recipeDao.searchRecipes(
            pageSize = RECIPE_CATEGORY_PAGE_SIZE,
            query = category.name,
            page = 1,
        )

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
