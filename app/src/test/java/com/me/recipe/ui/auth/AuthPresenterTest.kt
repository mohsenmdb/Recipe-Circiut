package com.me.recipe.ui.auth

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.me.recipe.R
import com.me.recipe.domain.features.auth.model.Login
import com.me.recipe.domain.features.auth.model.User
import com.me.recipe.domain.features.auth.repository.AuthRepository
import com.me.recipe.domain.features.auth.usecase.LoginUseCase
import com.me.recipe.domain.features.auth.usecase.RegisterUseCase
import com.me.recipe.shared.datastore.LoginState
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.component.util.ErrorFormatterFake
import com.me.recipe.ui.profile.ProfileScreen
import com.me.recipe.ui.utils.MainDispatcherRule
import com.me.recipe.ui.utils.awaitState
import com.me.recipe.ui.utils.lazyOfDagger
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import recipe.app.core.errorformater.ErrorFormatter

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AuthPresenterTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() = runTest {
        createUserDataStore().logout()
        advanceUntilIdle()
    }

    @Test
    fun `present - initial state is login mode with empty fields`() = runTest {
        createAuthPresenter().test {
            val state = awaitItem()

            assertThat(state.authMode).isEqualTo(AuthMode.LOGIN)
            assertThat(state.username).isEqualTo("")
            assertThat(state.password).isEqualTo("")
            assertThat(state.retryPassword).isEqualTo("")
            assertThat(state.hasPasswordError).isFalse()
            assertThat(state.message).isNull()
            assertThat(state.isLoading).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when switch mode is clicked then auth mode changes and passwords are cleared`() = runTest {
        createAuthPresenter().test {
            val initialState = awaitItem()
            initialState.eventSink(AuthEvent.OnPasswordChange("secret"))
            val stateWithPassword = awaitItem()
            stateWithPassword.eventSink(AuthEvent.OnRetryPasswordChange("secret2"))
            val stateWithRetry = awaitItem()

            stateWithRetry.eventSink(AuthEvent.OnSwitchModeClicked)

            val switchedState = awaitState { it.authMode == AuthMode.REGISTER }
            assertThat(switchedState.authMode).isEqualTo(AuthMode.REGISTER)
            assertThat(switchedState.password).isEqualTo("")
            assertThat(switchedState.retryPassword).isEqualTo("")
            assertThat(switchedState.hasPasswordError).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when register passwords do not match then password error is shown`() = runTest {
        createAuthPresenter().test {
            val initialState = awaitItem()
            initialState.eventSink(AuthEvent.OnSwitchModeClicked)
            val registerState = awaitItem()

            registerState.eventSink(AuthEvent.OnUsernameChange("chef_user"))
            awaitItem().eventSink(AuthEvent.OnFirstNameChange("Recipe"))
            awaitItem().eventSink(AuthEvent.OnLastNameChange("Tester"))
            awaitItem().eventSink(AuthEvent.OnAgeChange("24"))
            awaitItem().eventSink(AuthEvent.OnPasswordChange("secret"))
            val readyState = awaitItem()
            readyState.eventSink(AuthEvent.OnRetryPasswordChange("different"))
            val mismatchState = awaitItem()

            mismatchState.eventSink(AuthEvent.OnSubmitClicked)

            val errorState = awaitItem()
            assertThat(errorState.hasPasswordError).isTrue()
            assertThat(errorState.isLoading).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when login succeeds then user is saved and root resets to profile`() = runTest {
        val navigator = FakeNavigator(AuthScreen)
        val userDataStore = createUserDataStore()
        val user = User(
            username = "chef_user",
            firstName = "Recipe",
            lastName = "Tester",
            age = 24,
        )
        val authRepository = AuthRepositoryFake(
            loginResult = Result.success(Login(accessToken = "token", user = user)),
        )

        createAuthPresenter(
            navigator = navigator,
            userDataStore = userDataStore,
            authRepository = authRepository,
        ).test {
            val initialState = awaitItem()
            initialState.eventSink(AuthEvent.OnUsernameChange("chef_user"))
            awaitItem().eventSink(AuthEvent.OnPasswordChange("secret"))
            val filledState = awaitItem()

            filledState.eventSink(AuthEvent.OnSubmitClicked)

            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            advanceTimeBy(1_000)
            advanceUntilIdle()

            val successState = awaitItem()
            assertThat(successState.message?.message?.textRes).isEqualTo(R.string.login_successful)

            val finishedState = awaitItem()
            assertThat(finishedState.isLoading).isFalse()
            assertThat(userDataStore.userFlow.value).isEqualTo(
                LoginState.LoggedIn(
                    UserInfo(
                        accessToken = "token",
                        username = "chef_user",
                        firstName = "Recipe",
                        lastName = "Tester",
                        age = "24",
                    ),
                ),
            )
            assertThat(navigator.awaitResetRoot().newRoot).isEqualTo(ProfileScreen)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when login fails and clear message is clicked then message is removed`() = runTest {
        val exception = IllegalStateException("Login failed")
        createAuthPresenter(
            authRepository = AuthRepositoryFake(
                loginResult = Result.failure(exception),
            ),
        ).test {
            val initialState = awaitItem()
            initialState.eventSink(AuthEvent.OnUsernameChange("chef_user"))
            awaitItem().eventSink(AuthEvent.OnPasswordChange("secret"))
            val filledState = awaitItem()

            filledState.eventSink(AuthEvent.OnSubmitClicked)

            awaitItem()
            advanceTimeBy(500)
            advanceUntilIdle()

            val stateWithMessage = awaitItem()
            assertThat(stateWithMessage.message?.message?.text).isEqualTo("Login failed")

            stateWithMessage.eventSink(AuthEvent.ClearMessage)

            val clearedState = awaitState { it.message == null }
            assertThat(clearedState.message).isNull()
            assertThat(clearedState.isLoading).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createAuthPresenter(
        navigator: Navigator = FakeNavigator(AuthScreen),
        userDataStore: UserDataStore = createUserDataStore(),
        authRepository: AuthRepository = AuthRepositoryFake(),
        errorFormatter: ErrorFormatter = ErrorFormatterFake(),
    ): AuthPresenter {
        return AuthPresenter(
            navigator = navigator,
            loginUseCase = lazyOfDagger(LoginUseCase(authRepository)),
            registerUseCase = lazyOfDagger(RegisterUseCase(authRepository)),
            userDataStore = lazyOfDagger(userDataStore),
            errorFormatter = lazyOfDagger(errorFormatter),
        )
    }

    private fun createUserDataStore(): UserDataStore {
        return UserDataStore(
            context = context,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
    }
}
