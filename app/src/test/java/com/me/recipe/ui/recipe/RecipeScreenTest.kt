package com.me.recipe.ui.recipe

import com.me.recipe.domain.features.recipe.model.Recipe
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
@Config(application = HiltTestApplication::class)
class RecipeScreenTest {

    @get:Rule
    val robotTestRule = RobotTestRule(this)

    @Inject
    lateinit var robot: RecipeScreenRobot

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun `when all data is available then show recipe correctly`() {
        val data = RecipeUiState.testData()
        robot(robotTestRule) {
            setRecipeScreen(data)
            checkScreenWhenStateIsLoaded(data.recipe?.ingredients!!)
        }
    }

    @Test
    fun `while loading just show shimmer and not show recipe view`() {
        val data = RecipeUiState.testData().copy(
            recipesLoading = true,
            recipe = null
        )
        robot(robotTestRule) {
            setRecipeScreen(data)
            checkScreenWhenStateIsLoading()
        }
    }

    @Test
    fun `when state change from loading to loaded show correctly loading and loaded screens`() {
        val data = RecipeUiState.testData().copy(
            recipesLoading = true,
            recipe = Recipe.EMPTY,
            )
        robot(robotTestRule) {
            setRecipeScreenLoadingThenLoaded(data)

            checkScreenWhenStateIsLoading()
            mainClockAutoAdvance(false)
            mainClockAdvanceTimeBy(1100)
            checkScreenWhenStateIsLoaded(data.recipe?.ingredients!!)
        }
    }
}
