package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.data.core.utils.toDate
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.network.core.di.retrofit.LOCAL_HOST_PATH
import com.me.recipe.network.features.recipe.model.RecipeDto
import java.util.UUID
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe> {

    override fun mapToDomainModel(model: RecipeDto, uid: String?): Recipe {
        val timestamp = model.updatedAt ?: model.createdAt ?: 0L
        return Recipe(
            id = model.id ?: -1,
            uid = uid ?: UUID.randomUUID().toString(),
            title = model.title.orEmpty(),
            // this replace is just for local server not production
            image = model.image.orEmpty().replace("http://localhost:3000/", LOCAL_HOST_PATH),
            rating = model.rating,
            publisher = model.publisher?.firstName.orEmpty() + "-" + model.publisher?.lastName.orEmpty(),
            ingredients = model.ingredients?.split(",")?.toPersistentList() ?: persistentListOf(),
            date = timestamp.toDate(),
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }
}
