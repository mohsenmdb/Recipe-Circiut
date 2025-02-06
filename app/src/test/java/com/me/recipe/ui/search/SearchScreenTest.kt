package com.me.recipe.ui.search

import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.utils.RobotTestRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, qualifiers = "w1080dp-h2400dp-xhdpi")
class SearchScreenTest {

    @get:Rule
    val robotTestRule = RobotTestRule(this)

    @Inject
    lateinit var robot: SearchScreenRobot

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun `when all data is available then show recipe correctly`() {
        val state = SearchUiState.testData()
        robot(robotTestRule) {
            setSearchScreen(state)
            checkScreenWhenStateIsLoaded(state)
        }
    }

    @Test
    fun `when state change from loading to loaded show correctly loading and loaded screens`() {
        val loadedState = SearchUiState.testData()
        val loadingState = SearchUiState.testData().copy(
            recipes = persistentListOf(),
            loading = true,
        )
        robot(robotTestRule) {
            setRecipeListScreenLoadingThenLoaded(loadingState, loadedState)

            checkScreenWhenStateIsLoading()
            mainClockAutoAdvance(false)
            mainClockAdvanceTimeBy(1100)
            checkScreenWhenStateIsLoaded(loadedState)
        }
    }

    @Test
    fun `while loading data show shimmer correctly`() {
        val state = SearchUiState.testData().copy(loading = true)
        robot(robotTestRule) {
            setSearchScreen(state)
            assertRecipeShimmerIsDisplay()
        }
    }

    @Test
    fun `while load more data show appending loading correctly`() {
        val state = SearchUiState.testData().copy(appendingLoading = true)
        robot(robotTestRule) {
            setSearchScreen(state)
            checkScreenWhenStateIsLoadedMore()
        }
    }

    @Test
    fun `when has error check error dialog show correctly`() {
        val state = SearchUiState.testData().copy(
            errors = GenericDialogInfo.testDate(),
        )
        robot(robotTestRule) {
            setSearchScreen(state)
            checkScreenWhenStateIsError(state.errors!!)
        }
    }
}
