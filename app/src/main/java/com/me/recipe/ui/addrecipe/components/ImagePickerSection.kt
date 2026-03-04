package com.me.recipe.ui.addrecipe.components

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.me.recipe.R
import com.me.recipe.ui.component.image.CoilImage

@Composable
internal fun ColumnScope.ImagePickerSection(
    imageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
) {
    TitleText()

    ImageBox(imageUri = imageUri)

    ButtonsRow(onImageSelected = onImageSelected)
}

@Composable
private fun TitleText() {
    Text(
        text = stringResource(R.string.recipe_image),
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun ImageBox(imageUri: Uri?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUri != null) {
            CoilImage(
                data = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Text(text = stringResource(R.string.no_image_selected))
        }
    }
}

@Composable
private fun ButtonsRow(onImageSelected: (Uri?) -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        GalleryButton(onImageSelected = onImageSelected)
        Spacer(Modifier.width(8.dp))
        CameraButton(onImageSelected = onImageSelected)
    }
}

@Composable
private fun RowScope.GalleryButton(
    onImageSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        onImageSelected(uri)
    }

    Button(
        onClick = { galleryLauncher.launch("image/*") },
        modifier = modifier.weight(1f),
    ) {
        Text(text = stringResource(R.string.pick_from_gallery))
    }
}

@Composable
private fun RowScope.CameraButton(
    onImageSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            onImageSelected(tempCameraUri)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            tempCameraUri = context.createImageUri()
            cameraLauncher.launch(tempCameraUri!!)
        }
    }

    Button(
        onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                tempCameraUri = context.createImageUri()
                cameraLauncher.launch(tempCameraUri!!)
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        modifier = modifier.weight(1f),
    ) {
        Text(text = stringResource(R.string.take_photo))
    }
}

fun Context.createImageUri(): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "recipe_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    return contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues,
    )!!
}
