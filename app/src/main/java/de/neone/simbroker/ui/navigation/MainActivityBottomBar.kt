package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import de.neone.simbroker.ui.theme.tertiaryLightMediumContrast

@Composable
fun MainActivityBottomBar(
    bottomBarColorLight: Color,
    bottomBarColorDark: Color,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {

    NavigationBar(
        containerColor = if (isSystemInDarkTheme()) bottomBarColorDark else bottomBarColorLight,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {

        NavigationItem.entries.forEach { route ->
            val selected = currentDestination?.hierarchy?.any { it.route == route.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = route.icon),
                        contentDescription = route.label
                    )
                },
                label = { Text(text = route.label, style = MaterialTheme.typography.bodySmall) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast,
                    selectedTextColor = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast,
                    indicatorColor = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.15f) else tertiaryLightMediumContrast.copy(alpha = 0.15f),
                    unselectedIconColor = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast,
                    unselectedTextColor = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast
                )
            )
        }
    }
}

