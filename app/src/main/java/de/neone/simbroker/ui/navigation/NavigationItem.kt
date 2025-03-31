package de.neone.simbroker.ui.navigation

import de.neone.simbroker.MainActivity
import de.neone.simbroker.R

enum class NavigationItem(
    val label: String,
    val icon: Int,
    val route: Any,
) {
    Portfolio(
        label = "Portfolio",
        icon = R.drawable.baseline_account_balance_wallet_48,
        route = MainActivity.PortfolioRoute
    ),
    Suche(
        label = "Search",
        icon = R.drawable.baseline_search_48,
        route = MainActivity.SearchRoute

    ),
    Konto(
        label = "Account",
        icon = R.drawable.baseline_account_circle_48,
        route = MainActivity.AccountRoute
    ),
    Logout(
        label = "Logout",
        icon = R.drawable.baseline_logout_48,
        route = MainActivity.LogoutRoute
    )
}



