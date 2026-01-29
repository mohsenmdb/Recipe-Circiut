package com.me.recipe.data.core.di

import com.me.recipe.data.features.recipelist.repository.CategoriesRepositoryDefault
import com.me.recipe.domain.features.recipelist.repository.CategoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CategoriesRepositoryModule {
    @Binds
    @Singleton
    fun provideCategoriesRepository(default: CategoriesRepositoryDefault): CategoriesRepository
}
