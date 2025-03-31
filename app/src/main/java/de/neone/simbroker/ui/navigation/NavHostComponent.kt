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
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.views.account.KontoView
import de.neone.simbroker.ui.views.portfolio.PortfolioView
import de.neone.simbroker.ui.views.search.SuchenView
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavHostComponent(
    innerPadding: PaddingValues,
    navController: NavHostController,
    action: () -> Unit,
    viewModel: SimBrokerViewModel = koinViewModel(),
    ) {

    NavHost(
        navController = navController,
        startDestination = SucheRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<PortfolioRoute> {
            PortfolioView(viewModel)
        }
        composable<SucheRoute> {
            SuchenView(viewModel)
        }
        composable<KontoRoute> {
            KontoView(viewModel)
        }
        composable<LogoutRoute> {
            action()
        }
    }
}