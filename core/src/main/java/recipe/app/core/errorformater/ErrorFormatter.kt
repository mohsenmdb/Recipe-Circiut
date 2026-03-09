package recipe.app.core.errorformater

interface ErrorFormatter {
    fun format(throwable: Throwable?): String
}
