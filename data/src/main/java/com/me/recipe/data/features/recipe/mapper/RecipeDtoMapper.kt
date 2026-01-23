package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.network.core.di.retrofit.LOCAL_HOST_PATH
import com.me.recipe.network.features.recipe.model.RecipeDto
import java.util.UUID
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

class RecipeDtoMapper :
    DomainMapper<RecipeDto, Recipe> {

    override fun mapToDomainModel(model: RecipeDto, uid: String?): Recipe {
        return Recipe(
            id = model.id ?: -1,
            uid = uid ?: UUID.randomUUID().toString(),
            title = model.title.orEmpty(),
            image = model.image.orEmpty().replace("http://localhost:3000/", LOCAL_HOST_PATH),// this replace is just for local server not production
            rating = model.rating,
            publisher = model.publisher.orEmpty(),
            ingredients = model.ingredients?.split(",")?.toPersistentList() ?: persistentListOf(),
            date = model.dateUpdated.orEmpty(),
            dateTimestamp = model.dateUpdatedTimeStamp ?: 0L,
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            id = domainModel.id,
            title = domainModel.title,
            image = domainModel.image,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            ingredients = domainModel.ingredients.joinToString(",") ,
            dateUpdated = domainModel.date,
            dateUpdatedTimeStamp = domainModel.dateTimestamp,
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Recipe>): List<RecipeDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}
