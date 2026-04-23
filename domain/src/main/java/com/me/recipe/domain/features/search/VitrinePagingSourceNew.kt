package com.me.recipe.domain.features.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.search.repository.VitrineRepository
import com.me.recipe.shared.utils.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import recipe.app.core.BuildConfig

class VitrinePagingSourceNew @Inject constructor(
    private val repository: VitrineRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PagingSource<VitrinePagingKey, Recipe>() {

    override fun getRefreshKey(state: PagingState<VitrinePagingKey, Recipe>): VitrinePagingKey? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPageIndex = state.pages.indexOf(state.closestPageToPosition(anchorPosition))
            state.pages.getOrNull(anchorPageIndex + 1)?.prevKey ?: state.pages.getOrNull(
                anchorPageIndex - 1,
            )?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<VitrinePagingKey>): LoadResult<VitrinePagingKey, Recipe> =
        withContext(ioDispatcher) {
            val key = requireNotNull(params.key) { "key was null for $params" }
            try {
                if (BuildConfig.DEBUG) delay(1000)
                val (data: List<Recipe>, nextPageUrl) = repository.getVitrine(
                    query = key.query,
                    page = key.page,
                    pageSize = PAGE_SIZE,
                    loadMore = key.loadMore,
                    onlyMyRecipes = key.onlyMyRecipes,
                )

                val nextKey = nextPageUrl.takeIf { it > 0 && data.size >= PAGE_SIZE }?.let {
                    VitrinePagingKey(
                        query = key.query,
                        onlyMyRecipes = key.onlyMyRecipes,
                        page = it,
                        loadMore = true,
                    )
                }

                LoadResult.Page(
                    data = data,
                    nextKey = nextKey,
                    prevKey = null,
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

    companion object {
        const val PAGE_SIZE = 10
        const val FIRST_PAGE = 1
    }
}
