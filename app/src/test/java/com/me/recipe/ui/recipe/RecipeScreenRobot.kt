package com.me.recipe.ui.recipe

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.utils.RobotTestRule
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
class RecipeScreenRobot @Inject constructor() {

    context(rule: RobotTestRule)
    fun setRecipeScreen(
        state: RecipeState,
    ) {
        rule.composeTestRule.setContent {
            RecipeUi(state = state)
        }
    }

    context(rule: RobotTestRule)
    fun setRecipeScreenLoadingThenLoaded(
        data: RecipeState,
    ) {
        rule.composeTestRule.setContent {
            var state by remember {
                mutableStateOf(data)
            }
            RecipeUi(state = state)
            LaunchedEffect(Unit) {
                delay(1000)
                state = data.copy(isLoading = false, recipe = Recipe.testData())
            }
        }
    }

    context(rule: RobotTestRule)
    fun checkScreenWhenStateIsLoaded(
        ingredients: ImmutableList<String>,
    ) {
        assertRecipeImageIsDisplayed()
        assertTitleRowTextIsDisplayed()
        assertTitleRowIsDisplayed()
        assertRankChipIsDisplayed()
        assertRecipeInfoViewIsDisplayed()
        assertRecipeInfoViewTextIsDisplayed()
        assertRecipeInfoViewHorizontalDividerIsDisplayed()
        ingredients.forEach {
            assertIngredientIsDisplayed(it)
        }
        assertLoadingRankChipIsNotDisplayed()
        assertLoadingRecipeShimmerIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    fun checkScreenWhenStateIsLoading() {
        assertLoadingRankChipIsDisplayed()
        assertLoadingRecipeShimmerIsDisplayed()
        assertRecipeImageShimmerIsDisplayed()

        assertTitleRowTextIsNotDisplayed()
        assertRankChipIsNotDisplayed()
        assertRecipeInfoViewIsNotDisplayed()
        assertRecipeInfoViewTextIsNotDisplayed()
        assertRecipeInfoViewHorizontalDividerIsNotDisplayed()
        assertIngredientTextIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeImageIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeImage", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeImageShimmerIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_recipeImage_shimmer", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertTitleRowTextIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_TitleRow_Text", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertTitleRowTextIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_TitleRow_Text", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertTitleRowIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_TitleRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertLoadingRankChipIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_LoadingRankChip", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertLoadingRankChipIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_LoadingRankChip", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRankChipIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RankChip", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRankChipIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RankChip", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeInfoViewIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeInfoView", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeInfoViewIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeInfoView", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeInfoViewTextIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeInfoView_Text", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeInfoViewTextIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeInfoView_Text", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeInfoViewHorizontalDividerIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeInfoView_HorizontalDivider", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertRecipeInfoViewHorizontalDividerIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_RecipeInfoView_HorizontalDivider", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertLoadingRecipeShimmerIsDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_LoadingRecipeShimmer", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertLoadingRecipeShimmerIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_LoadingRecipeShimmer", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertIngredientTextIsNotDisplayed() {
        rule.composeTestRule.onNodeWithTag("testTag_ingredient_Text", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context(rule: RobotTestRule)
    private fun assertIngredientIsDisplayed(
        text: String,
    ) {
        rule.composeTestRule.onNodeWithText(text, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context(rule: RobotTestRule)
    fun mainClockAutoAdvance(
        autoAdvance: Boolean,
    ) {
        rule.composeTestRule.mainClock.autoAdvance = autoAdvance
    }

    context(rule: RobotTestRule)
    fun mainClockAdvanceTimeBy(
        milliseconds: Long,
        ignoreFrameDuration: Boolean = false,
    ) {
        rule.composeTestRule.mainClock.advanceTimeBy(milliseconds, ignoreFrameDuration)
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule)
        RecipeScreenRobot.() -> Unit,
    ) {
        function(robotTestRule, this@RecipeScreenRobot)
    }
}
