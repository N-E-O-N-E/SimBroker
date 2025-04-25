package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import de.neone.simbroker.ui.theme.tertiaryLightMediumContrast

/**
 * Bottom Navigation Bar für die MainActivity.
 *
 * - Zeigt Einträge aus [NavigationItem] an.
 * - Wechselt zwischen den Routen bei Klick.
 * - Hebt den aktuell ausgewählten Eintrag visuell hervor.
 *
 * @param bottomBarColorLight Farbwert für den Bar-Hintergrund im Light-Theme.
 * @param bottomBarColorDark  Farbwert für den Bar-Hintergrund im Dark-Theme.
 * @param navController       Controller zur Steuerung der Navigation.
 * @param currentDestination  Aktuelle Ziel-Route, um die Selektion zu bestimmen.
 */
@Composable
fun MainActivityBottomBar(
    bottomBarColorLight: Color,
    bottomBarColorDark: Color,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    //==============================================================================================
    // 1) NavigationBar Container
    //==============================================================================================
    NavigationBar(
        containerColor = if (isSystemInDarkTheme()) bottomBarColorDark else bottomBarColorLight,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        //==========================================================================================
        // 2) Navigation Items iterieren
        //==========================================================================================
        NavigationItem.entries.forEach { item ->
            // Bestimme, ob dieser Eintrag aktuell selektiert ist
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            //======================================================================================
            // 3) Ein einzelner NavigationBarItem
            //======================================================================================
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // Poppe zurück zum Start und erhalte Zustand
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.height(40.dp),
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor      = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast,
                    selectedTextColor      = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast,
                    indicatorColor         = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.15f) else tertiaryLightMediumContrast.copy(alpha = 0.15f),
                    unselectedIconColor    = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast,
                    unselectedTextColor    = if (isSystemInDarkTheme()) Color.White else tertiaryLightMediumContrast
                )
            )
        }
    }
}
