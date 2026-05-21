package com.me.recipe.ui.profile

import android.app.Application
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class ProfilePresenterTest {

    @Test
    fun `present - initial state selects user info tab`() = runTest {
        val navigator = FakeNavigator(ProfileScreen)

        createProfilePresenter(navigator = navigator).test {
            val state = awaitItem()

            assertThat(state.selectedTab).isEqualTo(UserInfoTab)
            assertThat(state.tabs).isEqualTo(ProfileState.profileTabs)
            assertThat(state.navigator).isEqualTo(navigator)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when tab is clicked then selected tab is updated`() = runTest {
        createProfilePresenter().test {
            val initialState = awaitItem()
            assertThat(initialState.selectedTab).isEqualTo(UserInfoTab)

            initialState.eventSink(ProfileEvent.OnTabClick(MyRecipesTab))

            val updatedState = awaitItem()
            assertThat(updatedState.selectedTab).isEqualTo(MyRecipesTab)
            assertThat(updatedState.tabs).isEqualTo(ProfileState.profileTabs)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createProfilePresenter(
        navigator: Navigator = FakeNavigator(ProfileScreen),
    ): ProfilePresenter {
        return ProfilePresenter(navigator = navigator)
    }
}
