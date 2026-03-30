package com.me.recipe.data.features.search

import com.me.recipe.domain.features.search.repository.VitrineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface VitrineRepositoryModule {
    @Binds
    fun bindVitrineRepo(default: VitrineRepositoryDefault): VitrineRepository
}
