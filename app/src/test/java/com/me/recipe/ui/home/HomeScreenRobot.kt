package com.me.recipe.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.me.recipe.R
import com.me.recipe.ui.utils.RobotTestRule
import javax.inject.Inject

class HomeScreenRobot @Inject constructor() {

    context (RobotTestRule)
    fun setHomeScreen(
        state: HomeUiState,
    ) {
        composeTestRule.setContent {
            HomeScreen(state = state)
        }
    }

    context (RobotTestRule)
    fun assertAppNameTextIsDisplayed() {
        val context = InstrumentationRegistry.getInstrumentation().context
        composeTestRule.onNodeWithText(context.getString(R.string.app_name), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertChangeThemeIconIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_changeThemeIcon", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context (RobotTestRule)
    fun assertFirstSliderImageIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_sliderCard_image", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context (RobotTestRule)
    fun assertFirstSliderTextIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_sliderCard_text", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context (RobotTestRule)
    fun assertCategoryVerticalLazyRowIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_category_vertical_lazyRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context (RobotTestRule)
    fun assertCategoryHorizontalLazyRowIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_category_horizontal_lazyRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertCategoryTitleRowTextIsDisplayed(
        text: String,
    ) {
        composeTestRule.onNodeWithText(text, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule)
        HomeScreenRobot.() -> Unit,
    ) {
        function(robotTestRule, this@HomeScreenRobot)
    }
}
