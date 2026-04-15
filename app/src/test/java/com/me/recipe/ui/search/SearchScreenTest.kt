package com.me.recipe.ui.search

import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.utils.RobotTestRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import javax.inject.Inject
import kotlinx.coroutines.flow.flowOf
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
        robot(robotTestRule) {
            setSearchScreen { SearchState.testData() }
            checkScreenWhenStateIsLoaded(SearchState.testRecipes())
        }
    }

    @Test
    fun `when state change from loading to loaded show correctly loading and loaded screens`() {
        robot(robotTestRule) {
            mainClockAutoAdvance(false)
            setRecipeListScreenLoadingThenLoaded(
                loadingState = { SearchState.testData(pagingDataFlow = flowOf()) },
                loadedState = { SearchState.testData() },
            )

            checkScreenWhenStateIsLoading()
            mainClockAdvanceTimeBy(1100)
            robotTestRule.composeTestRule.waitForIdle()
            checkScreenWhenStateIsLoaded(SearchState.testRecipes())
        }
    }

    @Test
    fun `while loading data show shimmer correctly`() {
        robot(robotTestRule) {
            setSearchScreen {
                SearchState.testData(
                    pagingDataFlow = flowOf(),
                )
            }
            assertRecipeShimmerIsDisplay()
        }
    }

    @Test
    fun `while load more data show appending loading correctly`() {
        robot(robotTestRule) {
            setSearchScreen {
                SearchState.testData(
                    pagingDataFlow = SearchState.appendingTestData(),
                )
            }
            checkScreenWhenStateIsLoadedMore()
        }
    }

    @Test
    fun `when state is empty show empty state correctly`() {
        robot(robotTestRule) {
            setSearchScreen {
                SearchState.testData(
                    pagingDataFlow = SearchState.emptyTestData(),
                )
            }
            robotTestRule.composeTestRule.waitForIdle()
            checkScreenWhenStateIsEmpty()
        }
    }

    @Test
    fun `when has error check error dialog show correctly`() {
        val errors = GenericDialogInfo.testDate()
        robot(robotTestRule) {
            setSearchScreen {
                SearchState.testData().copy(
                    errors = errors,
                )
            }
            robotTestRule.composeTestRule.waitForIdle()
            checkScreenWhenStateIsError(errors)
        }
    }

    @Test
    fun `when refresh fails show full screen error view correctly`() {
        robot(robotTestRule) {
            setSearchScreen {
                SearchState.testData(
                    pagingDataFlow = SearchState.errorTestData(),
                )
            }
            robotTestRule.composeTestRule.waitForIdle()
            checkScreenWhenRefreshStateIsError()
        }
    }
}
