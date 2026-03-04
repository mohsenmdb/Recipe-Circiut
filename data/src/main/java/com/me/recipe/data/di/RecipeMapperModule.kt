package com.me.recipe.data.di

import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipelist.mapper.CategoryDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeMapperModule {

    @Singleton
    @Provides
    fun provideRecipeDtoMapper(): RecipeDtoMapper {
        return RecipeDtoMapper()
    }

    @Singleton
    @Provides
    fun provideCategoryDtoMapper(recipeDtoMapper: RecipeDtoMapper): CategoryDtoMapper {
        return CategoryDtoMapper(recipeDtoMapper)
    }

    @Singleton
    @Provides
    fun provideCacheRecipeMapper(): RecipeEntityMapper {
        return RecipeEntityMapper()
    }
}
