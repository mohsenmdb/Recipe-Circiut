package com.me.recipe.data.di

import com.me.recipe.data.features.recipe.repository.AddRecipeRepositoryDefault
import com.me.recipe.data.features.recipe.repository.RecipeRepositoryDefault
import com.me.recipe.domain.features.recipe.repository.AddRecipeRepository
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipeRepositoryModule {
    @Binds
    @Singleton
    fun provideRecipeRepository(default: RecipeRepositoryDefault): RecipeRepository

    @Binds
    @Singleton
    fun provideAddRecipeRepository(default: AddRecipeRepositoryDefault): AddRecipeRepository
}
