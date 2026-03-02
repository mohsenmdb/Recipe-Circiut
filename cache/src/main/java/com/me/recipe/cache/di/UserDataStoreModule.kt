package com.me.recipe.cache.di

import android.content.Context
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.shared.utils.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object UserDataStoreModule {
    @Provides
    @Singleton
    internal fun provideUserDataStore(@ApplicationContext context: Context, @IoDispatcher ioDispatcher: CoroutineDispatcher): UserDataStore =
        UserDataStore(context, ioDispatcher)
}
