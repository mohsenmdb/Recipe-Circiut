package com.me.recipe.ui.component.util

import androidx.compose.runtime.staticCompositionLocalOf
import recipe.app.core.errorformater.ErrorFormatter

val LocalErrorFormatter = staticCompositionLocalOf<ErrorFormatter> {
    error("No Error Formatter available")
}
