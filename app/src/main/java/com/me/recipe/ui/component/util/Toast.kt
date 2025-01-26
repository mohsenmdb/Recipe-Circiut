package com.me.recipe.ui.component.util

import android.content.Context
import android.widget.Toast


fun toast(
    context: Context,
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(context, message, duration).show()
}
