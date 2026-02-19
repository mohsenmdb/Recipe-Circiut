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

data class User(
    val accessToken: String = "",
    val email: String = "",
    val username: String = "",
    val phone: String = "",
)

@Singleton
class UserDataStore @Inject constructor(
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val _userFlow = MutableStateFlow(User())
    val userFlow: StateFlow<User> get() = _userFlow

    init {
        CoroutineScope(ioDispatcher).launch {
            context.dataStore.data.collect { prefs ->
                _userFlow.value = User(
                    accessToken = prefs[ACCESS_TOKEN_KEY] ?: "",
                    email = prefs[EMAIL_KEY] ?: "",
                    username = prefs[USERNAME_KEY] ?: "",
                    phone = prefs[PHONE_KEY] ?: "",
                )
            }
        }
    }

    fun getUser(): User = _userFlow.value

    suspend fun setAccessToken(token: String) = saveValue(ACCESS_TOKEN_KEY, token)
    suspend fun setEmail(email: String) = saveValue(EMAIL_KEY, email)
    suspend fun setUsername(username: String) = saveValue(USERNAME_KEY, username)
    suspend fun setPhone(phone: String) = saveValue(PHONE_KEY, phone)

    suspend fun setUser(user: User) = withContext(ioDispatcher) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = user.accessToken
            prefs[EMAIL_KEY] = user.email
            prefs[USERNAME_KEY] = user.username
            prefs[PHONE_KEY] = user.phone
        }
        _userFlow.value = user
    }

    suspend fun clearAll() = withContext(ioDispatcher) {
        context.dataStore.edit { it.clear() }
        _userFlow.value = User()
    }

    private suspend fun saveValue(key: Preferences.Key<String>, value: String) {
        withContext(ioDispatcher) {
            context.dataStore.edit { prefs ->
                prefs[key] = value
            }
            _userFlow.value = when (key) {
                ACCESS_TOKEN_KEY -> _userFlow.value.copy(accessToken = value)
                EMAIL_KEY -> _userFlow.value.copy(email = value)
                USERNAME_KEY -> _userFlow.value.copy(username = value)
                PHONE_KEY -> _userFlow.value.copy(phone = value)
                else -> _userFlow.value
            }
        }
    }

    companion object {
        private const val APP_PREFERENCE_NAME = "user_info"

        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val USERNAME_KEY = stringPreferencesKey("user_name")
        private val PHONE_KEY = stringPreferencesKey("user_phone")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = APP_PREFERENCE_NAME,
        )
    }
}
