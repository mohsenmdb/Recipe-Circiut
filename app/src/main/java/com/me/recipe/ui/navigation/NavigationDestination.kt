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

val bottomNavigationScreens = listOf(HomeDestination, SearchDestination)
