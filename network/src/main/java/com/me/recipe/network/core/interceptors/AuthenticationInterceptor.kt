package com.me.recipe.network.core.interceptors

import com.me.recipe.shared.datastore.UserDataStore
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * to use api without token add this header above api call function
 *@Headers("$NO_AUTHENTICATION:true")
* */

const val AUTHORIZATION_HEADER = "Authorization"
private const val USER_AGENT_HEADER = "HTTP_CUSTOMUSERAGENT"
private const val USER_AGENT_VALUE = "Android Native Client"

class AuthenticationInterceptor @Inject constructor(
    private val userDataStore: UserDataStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        var request = request()
        val token = userDataStore.getAccessToken()
        if (!token.isNullOrEmpty()) {
            val builder = request.newBuilder()
            builder.addHeader(USER_AGENT_HEADER, USER_AGENT_VALUE)
            Timber.d("Need to add auth [%s]", token)
            builder.header(AUTHORIZATION_HEADER, "Bearer $token")
            request = builder.build()
        }
        return@run proceed(request)
    }
}
