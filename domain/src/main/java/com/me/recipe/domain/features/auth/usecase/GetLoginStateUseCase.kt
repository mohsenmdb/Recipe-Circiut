package com.me.recipe.domain.features.auth.usecase

import com.me.recipe.domain.util.SubjectInteractor
import com.me.recipe.shared.datastore.LoginState
import com.me.recipe.shared.datastore.UserDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLoginStateUseCase @Inject constructor(
    private val userDataStore: UserDataStore,
) : SubjectInteractor<Any, LoginState>() {
    override fun createObservable(params: Any): Flow<LoginState> {
        return userDataStore.userFlow
    }
}
