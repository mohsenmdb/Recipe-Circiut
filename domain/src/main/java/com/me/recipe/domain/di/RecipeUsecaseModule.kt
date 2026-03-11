package com.me.recipe.domain.di

import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUseCase
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.features.recipelist.usecases.RestoreRecipesUseCase
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUseCase
import com.me.recipe.domain.features.recipelist.usecases.TopRecipeUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RecipeUsecaseModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipeUsecase(
        recipeListRepository: RecipeListRepository,
    ): SearchRecipesUseCase {
        return SearchRecipesUseCase(recipeListRepository = recipeListRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipesUsecase(
        recipeListRepository: RecipeListRepository,
    ): RestoreRecipesUseCase {
        return RestoreRecipesUseCase(recipeListRepository = recipeListRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipeUsecase(
        recipeRepository: RecipeRepository,
    ): GetRecipeUseCase {
        return GetRecipeUseCase(recipeRepository = recipeRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideTopRecipeUsecase(
        recipeListRepository: RecipeListRepository,
    ): TopRecipeUsecase {
        return TopRecipeUsecase(recipeListRepository = recipeListRepository)
    }
}
