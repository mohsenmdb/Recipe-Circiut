package com.me.recipe.ui.main

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.me.recipe.domain.features.auth.usecase.GetLoginStateUseCase
import com.me.recipe.shared.datastore.LoginState
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.main.navigation.HomeTab
import com.me.recipe.ui.main.navigation.ProfileTab
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
class MainPresenterTest {

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
    fun `present - initial state is logged out and home tab is selected`() = runTest {
        val userDataStore = createUserDataStore()
        userDataStore.logout()
        advanceUntilIdle()

        createMainPresenter(
            userDataStore = userDataStore,
        ).test {
            val state = awaitItem()

            assertThat(state.isUserLoggedIn).isEqualTo(false)
            assertThat(state.selectedTab).isEqualTo(HomeTab)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when user is logged in then logged in state is updated`() = runTest {
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

        createMainPresenter(
            userDataStore = userDataStore,
        ).test {
            val initialState = awaitItem()
            assertThat(initialState.isUserLoggedIn).isEqualTo(false)
            assertThat(initialState.selectedTab).isEqualTo(HomeTab)

            val loggedInState = awaitItem()
            assertThat(loggedInState.isUserLoggedIn).isEqualTo(true)
            assertThat(loggedInState.selectedTab).isEqualTo(HomeTab)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when selected tab changes then selected tab is updated`() = runTest {
        createMainPresenter().test {
            val initialState = awaitItem()
            assertThat(initialState.selectedTab).isEqualTo(HomeTab)

            initialState.eventSink(MainEvent.OnSelectedTabChanged(ProfileTab))

            val updatedState = awaitItem()
            assertThat(updatedState.selectedTab).isEqualTo(ProfileTab)
            assertThat(updatedState.isUserLoggedIn).isEqualTo(false)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createMainPresenter(
        navigator: Navigator = FakeNavigator(MainScreen),
        userDataStore: UserDataStore = createUserDataStore(),
    ): MainPresenter {
        return MainPresenter(
            navigator = navigator,
            getLoginStateUseCase = lazyOfDagger(GetLoginStateUseCase(userDataStore)),
        )
    }

    private fun createUserDataStore(): UserDataStore {
        return UserDataStore(
            context = context,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
    }
}
