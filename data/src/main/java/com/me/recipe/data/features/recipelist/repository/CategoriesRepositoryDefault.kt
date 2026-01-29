package com.me.recipe.data.features.recipelist.repository

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.cache.recipe.model.RecipeEntity
import com.me.recipe.data.core.di.IoDispatcher
import com.me.recipe.data.features.recipe.mapper.CategoryDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipelist.repository.CategoriesRepository
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.shared.utils.CategoryRowType
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_CATEGORY_PAGE_SIZE
import com.me.recipe.shared.utils.getAllFoodCategories
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
import timber.log.Timber

class CategoriesRepositoryDefault @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val recipeEntityMapper: RecipeEntityMapper,
    private val categoryDtoMapper: CategoryDtoMapper,
    @IoDispatcher private var ioDispatcher: CoroutineDispatcher,
) : CategoriesRepository {

    override fun categoriesRecipes(): Flow<ImmutableList<CategoryRecipe>> =
        flow {
            // To show loading
            delay(500)
            emit(getCategoriesFromNetwork())
        }.flowOn(ioDispatcher)

    private suspend fun getCategoriesFromNetwork(): PersistentList<CategoryRecipe> {
        val response = recipeApi.categories()
        val categories = categoryDtoMapper.map(response)
        categories.forEach {
            recipeDao.insertRecipes(recipeEntityMapper.toEntityList(it.recipes))
        }
        return categories
    }

    override fun categoriesRecipesOffline(): Flow<ImmutableList<CategoryRecipe>> {
        return flow {
            emit(getOfflineCategories())
        }.flowOn(ioDispatcher)
    }

    private suspend fun getOfflineCategories(): PersistentList<CategoryRecipe> =
        buildList {
            getAllFoodCategories().forEach { category ->
                val cacheResult = getCachedCategories(category)
                val recipes = recipeEntityMapper.toDomainList(cacheResult).toPersistentList()
                if (recipes.isEmpty()) return@forEach
                add(CategoryRecipe(CategoryRowType.ROW, category, recipes))
            }
        }.toPersistentList()

    private suspend fun getCachedCategories(category: FoodCategory): List<RecipeEntity> =
        recipeDao.searchRecipes(
            pageSize = RECIPE_CATEGORY_PAGE_SIZE,
            query = category.name,
            page = 1,
        )
}
