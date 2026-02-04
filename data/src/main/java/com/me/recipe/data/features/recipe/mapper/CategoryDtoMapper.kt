package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.data.core.utils.mappers.NullableInputMapper
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.network.features.recipe.model.CategoriesDto
import com.me.recipe.network.features.recipe.model.FoodCategoryDto
import com.me.recipe.network.features.recipe.model.RowTypeDto
import com.me.recipe.shared.utils.CategoryRowType
import com.me.recipe.shared.utils.FoodCategory
import javax.inject.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

class CategoryDtoMapper @Inject constructor(private val recipeDtoMapper: RecipeDtoMapper) :
    NullableInputMapper<CategoriesDto, PersistentList<CategoryRecipe>> {
    override fun map(input: CategoriesDto?): PersistentList<CategoryRecipe> {
        return buildList {
            input?.categories?.forEach {
                if (it.recipes.isNullOrEmpty()) return@forEach
                val recipes = recipeDtoMapper.toDomainList(it.recipes!!)
                add(
                    CategoryRecipe(
                        rowType = getRowType(it.type),
                        category = mapCategory(it.categoryName),
                        recipes = recipes.toPersistentList(),
                    ),
                )
            }
        }.toPersistentList()
    }

    private fun getRowType(type: RowTypeDto?): CategoryRowType {
        return when (type) {
            RowTypeDto.ROW -> CategoryRowType.ROW
            RowTypeDto.SLIDER -> CategoryRowType.SLIDER
            else -> CategoryRowType.UNKNOWN
        }
    }
    private fun mapCategory(category: FoodCategoryDto?): FoodCategory {
        return when (category) {
            FoodCategoryDto.CHICKEN -> FoodCategory.CHICKEN
            FoodCategoryDto.SOUP -> FoodCategory.SOUP
            FoodCategoryDto.DESSERT -> FoodCategory.DESSERT
            FoodCategoryDto.VEGETARIAN -> FoodCategory.VEGETARIAN
            FoodCategoryDto.MILK -> FoodCategory.MILK
            FoodCategoryDto.VEGAN -> FoodCategory.VEGAN
            FoodCategoryDto.PIZZA -> FoodCategory.PIZZA
            FoodCategoryDto.DONUT -> FoodCategory.DONUT
            FoodCategoryDto.WATER -> FoodCategory.WATER
            FoodCategoryDto.BEEF -> FoodCategory.BEEF
            FoodCategoryDto.PASTA -> FoodCategory.PASTA
            FoodCategoryDto.ALL -> FoodCategory.ALL
            else -> FoodCategory.UNKNOWN
        }
    }
}
