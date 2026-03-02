package com.me.recipe.shared.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.me.recipe.shared.utils.IoDispatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface LoginState {
    data class LoggedIn(val user: UserInfo) : LoginState
    data object LoggedOut : LoginState
}
data class UserInfo(
    val accessToken: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
)

@Singleton
class UserDataStore @Inject constructor(
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val _userFlow = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val userFlow: StateFlow<LoginState> get() = _userFlow

    init {
        CoroutineScope(ioDispatcher).launch {
            context.dataStore.data.collect { prefs ->
                _userFlow.value = getLoginState(prefs)
            }
        }
    }

    fun getLoginState(prefs: Preferences): LoginState {
        val jwtToken = prefs[ACCESS_TOKEN_KEY]
        fun isLoggedOut(): Boolean {
            return jwtToken.isNullOrEmpty()
        }
        if (isLoggedOut()) return LoginState.LoggedOut

        return LoginState.LoggedIn(
            user = UserInfo(
                accessToken = jwtToken!!,
                username = prefs[USERNAME_KEY].orEmpty(),
                firstName = prefs[FIRST_NAME_KEY].orEmpty(),
                lastName = prefs[LAST_NAME_KEY].orEmpty(),
                age = prefs[AGE_KEY].orEmpty(),
            ),
        )
    }
    fun getAccessToken(): String? =
        if (_userFlow.value is LoginState.LoggedIn) (_userFlow.value as LoginState.LoggedIn).user.accessToken else null

    suspend fun setUser(user: UserInfo) = withContext(ioDispatcher) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = user.accessToken
            prefs[USERNAME_KEY] = user.username
            prefs[FIRST_NAME_KEY] = user.firstName
            prefs[LAST_NAME_KEY] = user.lastName
            prefs[AGE_KEY] = user.age
        }
//        _userFlow.value = user
    }

    suspend fun logout() = withContext(ioDispatcher) {
        context.dataStore.edit { it.clear() }
        _userFlow.value = LoginState.LoggedOut
    }

    companion object {
        private const val APP_PREFERENCE_NAME = "user_info"

        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val USERNAME_KEY = stringPreferencesKey("user_name")
        private val FIRST_NAME_KEY = stringPreferencesKey("first_name")
        private val LAST_NAME_KEY = stringPreferencesKey("last_name")
        private val AGE_KEY = stringPreferencesKey("age")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = APP_PREFERENCE_NAME,
        )
    }
}
