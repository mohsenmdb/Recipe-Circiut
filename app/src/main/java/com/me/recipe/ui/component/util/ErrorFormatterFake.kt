package com.me.recipe.ui.component.util

import recipe.app.core.errorformater.ErrorFormatter

class ErrorFormatterFake : ErrorFormatter {
    override fun format(throwable: Throwable?): String {
        return throwable?.localizedMessage ?: ""
    }
}
