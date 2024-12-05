package com.me.recipe.util.di

import com.slack.circuit.foundation.Circuit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CircuitModule {
    @Provides
    fun provideCircuit(
        presenterFactories: @JvmSuppressWildcards Set<com.slack.circuit.runtime.presenter.Presenter.Factory>,
        uiFactories: @JvmSuppressWildcards Set<com.slack.circuit.runtime.ui.Ui.Factory>,
    ): com.slack.circuit.foundation.Circuit {
        return com.slack.circuit.foundation.Circuit.Builder.build()
    }
}
