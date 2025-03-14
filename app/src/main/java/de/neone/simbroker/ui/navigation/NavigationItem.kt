package de.neone.simbroker.ui.navigation

import de.neone.simbroker.MainActivity
import de.neone.simbroker.R

enum class NavigationItem (
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
        label = "Suche",
        icon = R.drawable.baseline_search_48,
        route = MainActivity.SucheRoute

    ),
    Konto(
        label = "Konto",
        icon = R.drawable.baseline_account_circle_48,
        route = MainActivity.KontoRoute
    )
}




