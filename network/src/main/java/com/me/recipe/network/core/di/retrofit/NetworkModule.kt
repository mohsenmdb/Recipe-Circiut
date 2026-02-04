package com.me.recipe.network.core.di.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val HTTP_TIMEOUT_S = 30

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        @AuthenticationInterceptorQualifier interceptor: Interceptor,
        @LoggingInterceptorQualifier interceptor2: Interceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(HTTP_TIMEOUT_S.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT_S.toLong(), TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIMEOUT_S.toLong(), TimeUnit.SECONDS)

        builder.interceptors().add(interceptor2)
        builder.interceptors().add(interceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRecipeService(client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return Retrofit.Builder()
            .baseUrl(LOCAL_HOST_PATH)
            .callFactory {
                client.newCall(it)
            }
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}

const val LOCAL_HOST_PATH = "http://10.0.2.2:3000/"
