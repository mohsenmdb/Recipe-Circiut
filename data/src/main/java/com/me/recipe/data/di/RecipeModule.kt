package com.me.recipe.data.di

import com.me.recipe.data.features.recipe.store.TodayRecipeCache
import com.me.recipe.data.utils.Cache
import com.me.recipe.domain.features.recipe.model.Recipe
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipeModule {

    @Binds
    @Singleton
    fun provideRecipeCache(cache: TodayRecipeCache): Cache<Unit, Recipe, Recipe>
}
