package de.neone.simbroker.ui.navigation

import de.neone.simbroker.R

/**
 * Repräsentiert die Elemente der Bottom-Navigation.
 *
 * Jeder Eintrag definiert:
 * - die zugehörige Route (Screen-Pfad),
 * - die Anzeigebezeichnung (Label),
 * - das Icon, das in der Navigation angezeigt wird.
 *
 * Wird genutzt in der Navigation Composable, um
 * einheitlich über alle Tabs zu iterieren.
 */
enum class NavigationItem(
    /** Route, die beim Navigieren verwendet wird. */
    val route: String,
    /** Label, das unter dem Icon angezeigt wird. */
    val label: String,
    /** Drawable-Resource für das Icon. */
    val icon: Int,
) {

    //==============================================================================================
    // Haupt-Bildschirme
    //==============================================================================================

    /** Portfolio-Bildschirm mit Übersicht über alle Positionen. */
    Portfolio(
        route = Routes.PORTFOLIO,
        label = "Portfolio",
        icon = R.drawable.baseline_account_balance_wallet_48
    ),

    /** Coins-Bildschirm zum Durchsuchen und Handeln von Kryptowährungen. */
    Coins(
        route = Routes.COINS,
        label = "Coins",
        icon = R.drawable.baseline_search_48
    ),

    /** Account-Bildschirm für Einstellungen, Statistiken und Charts. */
    Account(
        route = Routes.ACCOUNT,
        label = "Account",
        icon = R.drawable.baseline_account_circle_48
    ),

    //==============================================================================================
    // Sonstige Aktionen
    //==============================================================================================

    /** Logout-Aktion zum Abmelden des Benutzers. */
    Logout(
        route = Routes.LOGOUT,
        label = "Logout",
        icon = R.drawable.baseline_logout_48
    )
}
