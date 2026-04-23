package com.me.recipe.data.features.search

import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.domain.features.search.model.Vitrine
import com.me.recipe.domain.features.search.repository.VitrineRepository
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.network.features.recipe.model.RecipeSearchDto
import dagger.Lazy
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList

class VitrineRepositoryDefault @Inject constructor(
    private val listApi: Lazy<RecipeApi>,
    private val mapper: Lazy<RecipeDtoMapper>,
) : VitrineRepository {

    override suspend fun getVitrine(
        query: String,
        page: Int,
        pageSize: Int,
        onlyMyRecipes: Boolean,
        loadMore: Boolean,
    ): Vitrine {
        val response: RecipeSearchDto = if (onlyMyRecipes) {
            listApi.get().mine(page, pageSize)
        } else {
            listApi.get().search(page, pageSize, query)
        }
        val items = mapper.get().toDomainList(response.data.results).toImmutableList()
        val nextPage = page + 1
        return Vitrine(
            items = items,
            nextPage = nextPage,
        )
    }
}
