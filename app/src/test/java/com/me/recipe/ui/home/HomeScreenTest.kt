package com.me.recipe.ui.home

import com.me.recipe.ui.utils.RobotTestRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import javax.inject.Inject
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
class HomeScreenTest {

    @get:Rule
    val robotTestRule = RobotTestRule(this)

    @Inject
    lateinit var robot: HomeScreenRobot

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun `when all data is available then show recipe correctly`() {
        val state = HomeUiState.testData()
        robot(robotTestRule) {
            setHomeScreen(state)
            assertAppNameTextIsDisplayed()
            assertChangeThemeIconIsDisplayed()
            assertFirstSliderImageIsDisplayed()
            assertFirstSliderTextIsDisplayed()
            assertCategoryVerticalLazyRowIsDisplayed()
            assertCategoryHorizontalLazyRowIsDisplayed()
            assertCategoryTitleRowTextIsDisplayed(state.categoriesRecipes!!.first().category.value)
            assertCategoryTitleRowTextIsDisplayed(state.categoriesRecipes!!.last().category.value)
        }
    }
}
