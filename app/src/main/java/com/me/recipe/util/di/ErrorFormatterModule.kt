package com.me.recipe.util.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import recipe.app.core.errorformater.ErrorFormatter
import recipe.app.core.errorformater.ErrorFormatterImpl

@Module
@InstallIn(SingletonComponent::class)
interface ErrorFormatterModule {
    @Binds
    fun provideErrorFormatter(impl: ErrorFormatterImpl): ErrorFormatter
}
