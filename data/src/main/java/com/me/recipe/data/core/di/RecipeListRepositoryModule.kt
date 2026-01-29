package com.me.recipe.data.core.di

import com.me.recipe.data.features.recipelist.repository.RecipeListRepositoryDefault
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipeListRepositoryModule {

    @Binds
    @Singleton
    fun provideRecipeListRepository(default: RecipeListRepositoryDefault): RecipeListRepository
}
