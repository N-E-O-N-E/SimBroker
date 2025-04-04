package de.neone.simbroker.ui.navigation

import de.neone.simbroker.R

enum class NavigationItem(
    val route: String,
    val label: String,
    val icon: Int,
) {

    Portfolio(
        route = Routes.PORTFOLIO,
        label = "Portfolio",
        icon = R.drawable.baseline_account_balance_wallet_48
    ),
    Coins(
        route = Routes.COINS,
        label = "Coins",
        icon = R.drawable.baseline_search_48

    ),
    Account(
        route = Routes.ACCOUNT,
        label = "Account",
        icon = R.drawable.baseline_account_circle_48
    ),
    Logout(
        route = Routes.LOGOUT,
        label = "Logout",
        icon = R.drawable.baseline_logout_48
    )
}




