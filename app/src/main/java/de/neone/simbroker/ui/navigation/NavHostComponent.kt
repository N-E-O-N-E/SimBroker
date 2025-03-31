package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.neone.simbroker.MainActivity
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.views.account.AccountView
import de.neone.simbroker.ui.views.portfolio.PortfolioView
import de.neone.simbroker.ui.views.search.SearchView
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
        startDestination = MainActivity.SearchRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<MainActivity.PortfolioRoute> {
            PortfolioView(viewModel)
        }
        composable<MainActivity.SearchRoute> {
            SearchView(viewModel)
        }
        composable<MainActivity.AccountRoute> {
            AccountView(viewModel)
        }
        composable<MainActivity.LogoutRoute> {
            action()
        }
    }
}