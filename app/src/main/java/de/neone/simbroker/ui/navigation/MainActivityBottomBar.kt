package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@Composable
fun MainActivityBottomBar(
    bottomBarColorLight: Color,
    bottomBarColorDark: Color,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    NavigationBar(
        containerColor = if (!isSystemInDarkTheme()) {
            bottomBarColorLight
        } else {
            bottomBarColorDark
        },
        contentColor = Color.White
    ) {

        NavigationItem.entries.forEach { route ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == route.route } == true,
                onClick = { navController.navigate(route.route) },
                icon = { Icon(painter = painterResource(id = route.icon), contentDescription = route.label) },
                label = { Text(text = route.label) },
                alwaysShowLabel = true
            )
        }
    }
}