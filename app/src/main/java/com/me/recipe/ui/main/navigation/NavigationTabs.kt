package com.me.recipe.ui.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.me.recipe.R

interface NavigationTabs {
    val route: String
    val titleRes: Int
    val icon: ImageVector?
}

object HomeTab : NavigationTabs {
    override val route = "Home"
    override val titleRes = R.string.navigate_home_title
    override val icon = Icons.Filled.Home
}

object SearchTab : NavigationTabs {
    override val route = "Search"
    override val titleRes = R.string.navigate_search_title
    override val icon = Icons.Filled.Search
}

object ProfileTab : NavigationTabs {
    override val route = "Profile"
    override val titleRes = R.string.navigate_profile_title
    override val icon = Icons.Filled.Person
}

object AddRecipeTab : NavigationTabs {
    override val route = "AddRecipe"
    override val titleRes = R.string.navigate_add_recipe
    override val icon = Icons.Filled.Add
}

val bottomNavigationScreens = listOf(HomeTab, SearchTab, ProfileTab)
val bottomNavigationScreensWithLogin = listOf(HomeTab, SearchTab, AddRecipeTab, ProfileTab)
