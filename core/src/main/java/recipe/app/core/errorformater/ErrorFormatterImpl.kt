package recipe.app.core.errorformater

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException
import recipe.app.core.BuildConfig
import recipe.app.core.R
import recipe.app.core.errorformater.exceptions.ReadableException
import recipe.app.core.errorformater.exceptions.RecipeDataException
import retrofit2.HttpException

class ErrorFormatterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ErrorFormatter {
    override fun format(throwable: Throwable?): String {
        return formater(throwable)
    }

    private fun formater(throwable: Throwable?): String {
        val errorMessage: StringResource = when (throwable) {
            is ReadableException -> StringResource.Text(throwable.readableMessage)
            is RecipeDataException -> StringResource.ResId(R.string.something_went_wrong)
            is UnknownHostException, is ConnectException -> StringResource.ResId(R.string.not_connected_to_internet)
            is SocketTimeoutException, is TimeoutException -> StringResource.ResId(R.string.connection_timeout_exception)
            is SSLHandshakeException -> StringResource.ResId(R.string.server_error_retry)
            is HttpException -> {
                when {
                    throwable.code() == UNAUTHORIZED_ERROR_CODE -> {
                        StringResource.ResId(R.string.server_error_unauthorized)
                    }
                    throwable.code() == USER_ALREADY_EXIST_ERROR_CODE -> {
                        StringResource.ResId(R.string.server_error_user_exist)
                    }
                    else -> {
                        StringResource.ResId(R.string.server_error_retry)
                    }
                }
            }
            else -> StringResource.ResId(R.string.server_error_retry)
        }

        if (BuildConfig.DEBUG) {
            return buildString {
                append(errorMessage.resolve(context))
                appendLine()
                appendLine()
                append("*****Debug Info*****")
                appendLine()
                append(throwable.toString())
                appendLine()
                append("**********")
            }
        }
        return errorMessage.resolve(context)
    }

    companion object {
        const val UNAUTHORIZED_ERROR_CODE = 401
        const val USER_ALREADY_EXIST_ERROR_CODE = 422
    }
}
