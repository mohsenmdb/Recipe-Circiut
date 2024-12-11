package com.me.recipe.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.me.recipe.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
    val icon: ImageVector?
}

object SplashDestination : NavigationDestination {
    override val route = "splash"
    override val titleRes = R.string.navigate_splash_title
    override val icon = null
}

object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val titleRes = R.string.navigate_home_title
    override val icon = Icons.Filled.Home
}

object SearchDestination : NavigationDestination {
    override val route = "Search"
    override val titleRes = R.string.navigate_search_title
    override val icon = Icons.Filled.Search
}

object RecipeListDestination : NavigationDestination {
    override val route = "RecipeList"
    override val titleRes = R.string.navigate_recipe_list_title
    override val icon = null
    const val CATEGORY_TITLE_ARG = "categoryTitleArg"
    val routeWithArgs =
        "$route/{$CATEGORY_TITLE_ARG}"
    val arguments = listOf(
        navArgument(CATEGORY_TITLE_ARG) { type = NavType.StringType },
    )
}

object RecipeDestination : NavigationDestination {
    override val route = "Recipe"
    override val titleRes = R.string.navigate_recipe_title
    override val icon = null
    const val ITEM_ID_ARG = "itemId"
    const val ITEM_TITLE_ARG = "itemTitle"
    const val ITEM_IMAGE_ARG = "itemImage"
    const val ITEM_UID_ARG = "itemUid"
    val routeWithArgs =
        "$route/{$ITEM_ID_ARG}/{$ITEM_TITLE_ARG}/{$ITEM_IMAGE_ARG}/{$ITEM_UID_ARG}"
    val arguments = listOf(
        navArgument(ITEM_ID_ARG) { type = NavType.IntType },
        navArgument(ITEM_TITLE_ARG) { type = NavType.StringType },
        navArgument(ITEM_IMAGE_ARG) { type = NavType.StringType },
        navArgument(ITEM_UID_ARG) {
            type = NavType.StringType
            nullable = true
            defaultValue = ""
        },
    )
    val deepLinks = listOf(
        navDeepLink {
            uriPattern =
                "recipe://composables.com/{$ITEM_ID_ARG}/{$ITEM_TITLE_ARG}/{$ITEM_IMAGE_ARG}"
        },
    )
}
