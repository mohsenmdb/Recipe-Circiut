package com.me.recipe.network.core.di.modules

import com.me.recipe.network.features.recipe.AuthApi
import com.me.recipe.network.features.recipe.RecipeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesRecipeService(retrofit: Retrofit): RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
