package de.neone.simbroker.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.neone.simbroker.ui.SimBrokerViewModel
import de.neone.simbroker.ui.views.account.AccountView
import de.neone.simbroker.ui.views.coins.CoinsView
import de.neone.simbroker.ui.views.info.InfoView
import de.neone.simbroker.ui.views.portfolio.PortfolioView
import org.koin.androidx.compose.koinViewModel

/**
 * Beinhaltet den Navigation Host, der zwischen den Bildschirmen
 * Portfolio, Coins, Account und Logout wechselt.
 *
 * @param innerPadding PaddingValues, das vom Scaffold vorgegeben wird.
 * @param navController NavHostController zum Steuern der Navigation.
 * @param action Lambda, das bei Auswahl der Logout-Route ausgeführt wird.
 * @param viewModel Instanz von [SimBrokerViewModel], bereitgestellt via Koin.
 */
@Composable
fun NavHostComponent(
    innerPadding: PaddingValues,
    navController: NavHostController,
    action: () -> Unit,
    viewModel: SimBrokerViewModel = koinViewModel(),
) {
    //==============================================================================================
    // Navigation Host Setup
    //==============================================================================================
    NavHost(
        navController = navController,
        startDestination = Routes.PORTFOLIO,
        modifier = Modifier.padding(innerPadding)
    ) {
        //------------------------------------------------------------------------------------------
        // Ziel: Portfolio-Bildschirm
        //------------------------------------------------------------------------------------------
        composable(Routes.PORTFOLIO) {
            PortfolioView(viewModel)
        }

        //------------------------------------------------------------------------------------------
        // Ziel: Coins-Bildschirm
        //------------------------------------------------------------------------------------------
        composable(Routes.COINS) {
            CoinsView(viewModel)
        }

        //------------------------------------------------------------------------------------------
        // Ziel: Account-Bildschirm
        //------------------------------------------------------------------------------------------
        composable(Routes.ACCOUNT) {
            AccountView(viewModel)
        }

        //------------------------------------------------------------------------------------------
        // Ziel: Info-Bildschirm
        //------------------------------------------------------------------------------------------
        composable(Routes.INFO) {
            InfoView()
        }

        //------------------------------------------------------------------------------------------
        // Ziel: Logout-Aktion
        //------------------------------------------------------------------------------------------
        composable(Routes.LOGOUT) {
            action()
        }
    }
}
