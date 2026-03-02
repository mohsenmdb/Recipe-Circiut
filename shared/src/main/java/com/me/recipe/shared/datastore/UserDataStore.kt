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
    private val _userFlow = MutableStateFlow(UserInfo())
    val userFlow: StateFlow<UserInfo> get() = _userFlow

    init {
        CoroutineScope(ioDispatcher).launch {
            context.dataStore.data.collect { prefs ->
                _userFlow.value = UserInfo(
                    accessToken = prefs[ACCESS_TOKEN_KEY] ?: "",
                    username = prefs[USERNAME_KEY] ?: "",
                    firstName = prefs[FIRST_NAME_KEY] ?: "",
                    lastName = prefs[LAST_NAME_KEY] ?: "",
                    age = prefs[AGE_KEY] ?: "",
                )
            }
        }
    }

    fun getUser(): UserInfo = _userFlow.value

    suspend fun setAccessToken(token: String) = saveValue(ACCESS_TOKEN_KEY, token)
    suspend fun setEmail(email: String) = saveValue(FIRST_NAME_KEY, email)
    suspend fun setUsername(username: String) = saveValue(USERNAME_KEY, username)
    suspend fun setPhone(phone: String) = saveValue(AGE_KEY, phone)

    suspend fun setUser(user: UserInfo) = withContext(ioDispatcher) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = user.accessToken
            prefs[USERNAME_KEY] = user.username
            prefs[FIRST_NAME_KEY] = user.firstName
            prefs[LAST_NAME_KEY] = user.lastName
            prefs[AGE_KEY] = user.age
        }
        _userFlow.value = user
    }

    suspend fun clearAll() = withContext(ioDispatcher) {
        context.dataStore.edit { it.clear() }
        _userFlow.value = UserInfo()
    }

    private suspend fun saveValue(key: Preferences.Key<String>, value: String) {
        withContext(ioDispatcher) {
            context.dataStore.edit { prefs ->
                prefs[key] = value
            }
            _userFlow.value = when (key) {
                ACCESS_TOKEN_KEY -> _userFlow.value.copy(accessToken = value)
                USERNAME_KEY -> _userFlow.value.copy(username = value)
                FIRST_NAME_KEY -> _userFlow.value.copy(firstName = value)
                LAST_NAME_KEY -> _userFlow.value.copy(lastName = value)
                AGE_KEY -> _userFlow.value.copy(age = value)
                else -> _userFlow.value
            }
        }
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
