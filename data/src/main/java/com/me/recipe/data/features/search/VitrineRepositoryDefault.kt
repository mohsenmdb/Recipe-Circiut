package com.me.recipe.data.features.search

import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.domain.features.search.model.Vitrine
import com.me.recipe.domain.features.search.repository.VitrineRepository
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.network.features.recipe.model.RecipeDto
import com.me.recipe.network.features.recipe.model.RecipeSearchDto
import dagger.Lazy
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class VitrineRepositoryDefault @Inject constructor(
    private val listApi: Lazy<RecipeApi>,
    private val mapper: Lazy<RecipeDtoMapper>,
) : VitrineRepository {

    override suspend fun getVitrine(query: String, page: Int, loadMore: Boolean): Vitrine {
        val response: RecipeSearchDto = listApi.get().search(page, PAGE_SIZE, query)
        val items = mapper.get().toDomainList(response.data.results).toImmutableList()

        return Vitrine(
            items = items,
            nextPage = page + 1
        )
    }

    companion object {
        const val PAGE_SIZE = 10
        const val FIRST_PAGE = 1
    }
}
