package com.me.recipe.data.features.recipe.repository

import android.content.Context
import android.net.Uri
import com.me.recipe.domain.features.recipe.repository.AddRecipeRepository
import com.me.recipe.network.features.recipe.RecipeApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddRecipeRepositoryDefault @Inject constructor(
    private val api: RecipeApi,
    @ApplicationContext private val context: Context,
) : AddRecipeRepository {
    override suspend fun addRecipe(
        title: String,
        description: String,
        ingredients: String,
        imageUri: Uri,
    ) {
        val titleBody = title.toRequestBody("text/plain".toMediaType())
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val ingredientsBody = ingredients.toRequestBody("text/plain".toMediaType())
        val imagePart = imageUri.toMultipart(context)

        api.addRecipe(
            title = titleBody,
            description = descriptionBody,
            ingredients = ingredientsBody,
            image = imagePart,
        )
    }
}
fun Uri.toMultipart(context: Context): MultipartBody.Part {
    val contentResolver = context.contentResolver

    val inputStream = contentResolver.openInputStream(this)
        ?: throw IllegalArgumentException("Cannot open image uri")

    val bytes = inputStream.readBytes()

    val requestBody = bytes.toRequestBody("image/*".toMediaType())

    return MultipartBody.Part.createFormData(
        name = "image",
        filename = "recipe_${System.currentTimeMillis()}.jpg",
        body = requestBody,
    )
}
