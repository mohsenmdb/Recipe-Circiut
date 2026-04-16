package com.me.recipe.ui.profile

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.me.recipe.R
import com.me.recipe.domain.features.auth.usecase.GetLoginStateUseCase
import com.me.recipe.shared.datastore.LoginState
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.auth.AuthScreen
import com.me.recipe.ui.utils.MainDispatcherRule
import com.me.recipe.ui.utils.lazyOfDagger
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ProfilePresenterTest {

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
    fun `present - initial state is logged out`() = runTest {
        val navigator = FakeNavigator(ProfileScreen)
        val userDataStore = createUserDataStore()
        userDataStore.logout()
        advanceUntilIdle()

        createProfilePresenter(
            navigator = navigator,
            userDataStore = userDataStore,
        ).test {
            val state = awaitItem()

            assertThat(state.profile).isEqualTo(UserInfo())
            assertThat(state.message).isNull()
            navigator.expectNoResetRootEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when user is logged in then profile is updated`() = runTest {
        val user = UserInfo(
            accessToken = "token",
            username = "chef_user",
            firstName = "Recipe",
            lastName = "Tester",
            age = "24",
        )
        val userDataStore = createUserDataStore()
        userDataStore.setUser(user)
        userDataStore.userFlow.first { it is LoginState.LoggedIn }

        createProfilePresenter(
            userDataStore = userDataStore,
        ).test {
            val initialState = awaitItem()
            assertThat(initialState.profile).isEqualTo(UserInfo())
            assertThat(initialState.message).isNull()

            val loadedState = awaitItem()
            assertThat(loadedState.profile).isEqualTo(user)
            assertThat(loadedState.message).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when logout is clicked then emit toast and reset root to auth`() = runTest {
        val user = UserInfo(
            accessToken = "token",
            username = "chef_user",
            firstName = "Recipe",
            lastName = "Tester",
            age = "24",
        )
        val navigator = FakeNavigator(ProfileScreen)
        val userDataStore = createUserDataStore()
        userDataStore.setUser(user)
        userDataStore.userFlow.first { it is LoginState.LoggedIn }

        createProfilePresenter(
            navigator = navigator,
            userDataStore = userDataStore,
        ).test {
            skipItems(1)
            val loadedState = awaitItem()
            loadedState.eventSink(ProfileEvent.OnLogoutClicked)

            val logoutState = awaitItem()
            assertThat(logoutState.message?.message?.textRes).isEqualTo(R.string.logout)
            assertThat(userDataStore.userFlow.value).isEqualTo(LoginState.LoggedOut)
            assertThat(navigator.awaitResetRoot().newRoot).isEqualTo(AuthScreen)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when clear message is clicked then message is removed`() = runTest {
        val user = UserInfo(
            accessToken = "token",
            username = "chef_user",
            firstName = "Recipe",
            lastName = "Tester",
            age = "24",
        )
        val userDataStore = createUserDataStore()
        userDataStore.setUser(user)
        userDataStore.userFlow.first { it is LoginState.LoggedIn }

        createProfilePresenter(
            userDataStore = userDataStore,
        ).test {
            awaitItem()
            val loadedState = awaitItem()

            loadedState.eventSink(ProfileEvent.OnLogoutClicked)

            val stateWithMessage = awaitItem()
            assertThat(stateWithMessage.message?.message?.textRes).isEqualTo(R.string.logout)

            stateWithMessage.eventSink(ProfileEvent.ClearMessage)

            val clearedState = awaitItem()
            assertThat(clearedState.message).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createProfilePresenter(
        navigator: Navigator = FakeNavigator(ProfileScreen),
        userDataStore: UserDataStore = createUserDataStore(),
    ): ProfilePresenter {
        return ProfilePresenter(
            navigator = navigator,
            getLoginStateUseCase = lazyOfDagger(GetLoginStateUseCase(userDataStore)),
            userDataStore = lazyOfDagger(userDataStore),
        )
    }

    private fun createUserDataStore(): UserDataStore {
        return UserDataStore(
            context = context,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
    }
}
