package com.me.recipe.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.me.recipe.R
import com.me.recipe.ui.utils.RobotTestRule
import javax.inject.Inject

class HomeScreenRobot @Inject constructor() {

    context(rule: RobotTestRule)
    fun setHomeScreen(
        state: HomeState,
    ) {
        rule.composeTestRule.setContent {
            HomeUi(state = state)
        }
    }

    context(rule: RobotTestRule)
    fun assertAppNameTextIsDisplayed() {
        val context = InstrumentationRegistry.getInstrumentation().context
        rule.composeTestRule.onNodeWithText(context.getString(R.string.app_name), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    fun assertChangeThemeIconIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_changeThemeIcon", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context(rule: RobotTestRule)
    fun assertFirstSliderImageIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_sliderCard_image", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context(rule: RobotTestRule)
    fun assertFirstSliderTextIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_sliderCard_text", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context(rule: RobotTestRule)
    fun assertCategoryVerticalLazyRowIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_category_vertical_lazyRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context(rule: RobotTestRule)
    fun assertCategoryHorizontalLazyRowIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_category_horizontal_lazyRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    fun assertCategoryTitleRowTextIsDisplayed(
        text: String,
    ) {
        rule.composeTestRule.onNodeWithText(text, useUnmergedTree = true)
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
