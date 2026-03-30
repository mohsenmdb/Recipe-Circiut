package com.me.recipe.domain.features.search.repository

import com.me.recipe.domain.features.search.model.Vitrine

interface VitrineRepository {
    suspend fun getVitrine(query: String, page: Int, loadMore: Boolean = false): Vitrine
}
