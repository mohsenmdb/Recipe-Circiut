package com.me.recipe.data.di

import com.me.recipe.data.features.auth.repository.AuthRepositoryDefault
import com.me.recipe.domain.features.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthRepositoryModule {
    @Binds
    @Singleton
    fun provideAuthRepository(default: AuthRepositoryDefault): AuthRepository
}
