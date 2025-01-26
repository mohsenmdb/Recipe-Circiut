package com.me.recipe.ui.component.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun Toast(
    message: String,
    duration: Int = android.widget.Toast.LENGTH_SHORT,
) {
    android.widget.Toast.makeText(LocalContext.current, message, duration).show()
}
