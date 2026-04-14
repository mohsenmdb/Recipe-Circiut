package com.me.recipe.domain.features.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.ForceFresh
import com.me.recipe.domain.util.PagingInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

data class VitrinePagingKey(
    val query: String,
    val page: Int,
    val loadMore: Boolean,
)

class ObservePagedVitrineNew @Inject constructor(
    private val pagingSource: PagingSource<VitrinePagingKey, Recipe>,
) : PagingInteractor<ObservePagedVitrineNew.Params, Recipe>() {

    override fun createObservable(params: Params): Flow<PagingData<Recipe>> {
        return Pager(
            config = params.pagingConfig,
            initialKey = params.key,
            pagingSourceFactory = { pagingSource },
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val key: VitrinePagingKey,
        val forceRefresh: ForceFresh? = null,
    ) : PagingInteractor.Parameters<Recipe>
}
