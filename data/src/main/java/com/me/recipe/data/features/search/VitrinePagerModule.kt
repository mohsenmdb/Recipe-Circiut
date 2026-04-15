package com.me.recipe.data.features.search

import androidx.paging.PagingSource
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.search.VitrinePagingKey
import com.me.recipe.domain.features.search.VitrinePagingSourceNew
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface VitrinePagerModule {
    @Binds
    fun provideVitrinePagerNew(vitrine: VitrinePagingSourceNew): PagingSource<VitrinePagingKey, Recipe>
}
