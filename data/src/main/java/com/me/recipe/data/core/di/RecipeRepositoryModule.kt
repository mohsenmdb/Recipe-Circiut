package com.me.recipe.data.core.di

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.repository.RecipeRepositoryImpl
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.network.features.recipe.RecipeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RecipeRepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(recipeDao: RecipeDao, recipeApi: RecipeApi, recipeDtoMapper: RecipeDtoMapper, entityMapper: RecipeEntityMapper, @IoDispatcher ioDispatcher: CoroutineDispatcher): RecipeRepository {
        return RecipeRepositoryImpl(recipeDao, recipeApi, entityMapper, recipeDtoMapper, ioDispatcher)
    }
}
