package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.neone.simbroker.MainActivity.KontoRoute
import de.neone.simbroker.MainActivity.LogoutRoute
import de.neone.simbroker.MainActivity.PortfolioRoute
import de.neone.simbroker.MainActivity.SucheRoute
import de.neone.simbroker.ui.views.KontoView
import de.neone.simbroker.ui.views.PortfolioView
import de.neone.simbroker.ui.views.SuchenView

@Composable
fun NavHostComponent(
    innerPadding: PaddingValues,
    navController: NavHostController,
    action: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = PortfolioRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<PortfolioRoute> {
            PortfolioView()
        }
        composable<SucheRoute> {
            SuchenView()
        }
        composable<KontoRoute> {
            KontoView()
        }
        composable<LogoutRoute> {
            action()
        }
    }
}