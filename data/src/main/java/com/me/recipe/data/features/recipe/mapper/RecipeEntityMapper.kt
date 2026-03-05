package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.cache.recipe.model.RecipeEntity
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.shared.utils.DateUtils
import java.util.UUID
import kotlinx.collections.immutable.toPersistentList

class RecipeEntityMapper : DomainMapper<RecipeEntity, Recipe> {

    override fun mapToDomainModel(model: RecipeEntity, uid: String?): Recipe {
        return Recipe(
            id = model.id,
            uid = uid ?: UUID.randomUUID().toString(),
            title = model.title,
            description = model.description,
            image = model.image,
            rating = model.rating,
            publisher = model.publisher,
            ingredients = convertIngredientsToList(model.ingredients).toPersistentList(),
            date = model.date,
        )
    }

    fun mapFromDomainModel(domainModel: Recipe): RecipeEntity {
        return RecipeEntity(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            image = domainModel.image,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            ingredients = domainModel.ingredients.joinToString(","),
            date = domainModel.date,
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp()),
        )
    }

    private fun convertIngredientsToList(ingredientsString: String?): List<String> {
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let {
            for (ingredient in it.split(",")) {
                list.add(ingredient)
            }
        }
        return list
    }

    fun toDomainList(recipes: List<RecipeEntity>): List<Recipe> {
        return recipes.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Recipe>): List<RecipeEntity> {
        return initial.map { mapFromDomainModel(it) }
    }
}
