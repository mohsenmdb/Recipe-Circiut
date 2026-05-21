package com.me.recipe.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

fun TemporalAmount.inPast(): Instant = Instant.now().minus(this)

suspend inline fun <Key : Any, Output : Any> Store<Key, Output>.fetch(
    key: Key,
    forceFresh: Boolean = false,
): Output = when {
    // If we're forcing a fresh fetch, do it now
    forceFresh -> fresh(key)
    else -> get(key)
}
inline fun <Key : Any, Output : Any> Store<Key, Output>.observe(
    key: Key,
    forceFresh: Boolean = false,
): Flow<Output> {
    return stream(StoreReadRequest.cached(key, forceFresh))
        .filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
        .map(StoreReadResponse<Output>::requireData)
}

suspend inline fun <Key : Any, Output : Any> Store<Key, Output>.fetchNoMemory(
    key: Key,
    forceFresh: Boolean = false,
): Output = when {
    // If we're forcing a fresh fetch, do it now
    forceFresh -> fresh(key)
    else -> stream(
        StoreReadRequest.skipMemory(key, refresh = false),
    ).filterNot {
        it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData
    }.first().requireData()
}

// //

@Suppress("NOTHING_TO_INLINE")
inline fun <Key : Any, Local : Any, Output : Any> storeBuilder(
    fetcher: Fetcher<Key, Local>,
    sourceOfTruth: SourceOfTruth<Key, Local, Output>,
): StoreBuilder<Key, Output> = StoreBuilder.from(fetcher, sourceOfTruth)

fun <Key : Any, Local : Any, Output : Any> SourceOfTruth<Key, Local, Output>.usingDispatchers(
    readDispatcher: CoroutineDispatcher,
    writeDispatcher: CoroutineDispatcher,
): SourceOfTruth<Key, Local, Output> {
    val wrapped = this
    return object : SourceOfTruth<Key, Local, Output> {
        override fun reader(key: Key): Flow<Output?> = wrapped.reader(key).flowOn(readDispatcher)

        override suspend fun write(key: Key, value: Local) = withContext(writeDispatcher) {
            wrapped.write(key, value)
        }

        override suspend fun delete(key: Key) = withContext(writeDispatcher) {
            wrapped.delete(key)
        }

        override suspend fun deleteAll() = withContext(writeDispatcher) {
            wrapped.deleteAll()
        }
    }
}
