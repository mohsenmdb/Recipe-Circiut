package com.me.recipe.data.utils

import kotlinx.coroutines.flow.Flow

interface Cache<KEY, RESPONSE, OUTPUT> {
    suspend fun write(key: KEY, response: RESPONSE)
    fun read(key: KEY): Flow<OUTPUT?>
    suspend fun delete(key: KEY)
    suspend fun deleteAll()

    suspend fun isEmpty(): Boolean = false
    suspend fun isEmpty(key: KEY): Boolean = false
}
