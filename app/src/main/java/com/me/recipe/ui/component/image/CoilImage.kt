package com.me.recipe.ui.component.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.me.recipe.R

@Composable
internal fun CoilImage(
    data: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    @DrawableRes placeholder: Int? = R.drawable.empty_plate,
    @DrawableRes errorImage: Int? = R.drawable.empty_plate,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(data)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        error = if (errorImage != null) painterResource(id = errorImage) else null,
        placeholder = if (placeholder != null) painterResource(id = placeholder) else null,
    )
}
