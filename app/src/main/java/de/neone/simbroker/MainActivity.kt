package de.neone.simbroker
/*
    ███╗   ██╗    ███████╗     ██████╗     ███╗   ██╗    ███████╗
    ████╗  ██║    ██╔════╝    ██╔═══██╗    ████╗  ██║    ██╔════╝
    ██╔██╗ ██║    █████╗      ██║   ██║    ██╔██╗ ██║    █████╗
    ██║╚██╗██║    ██╔══╝      ██║   ██║    ██║╚██╗██║    ██╔══╝
    ██║ ╚████║    ███████╗    ╚██████╔╝    ██║ ╚████║    ███████╗
    ╚═╝  ╚═══╝    ╚══════╝     ╚═════╝     ╚═╝  ╚═══╝    ╚══════╝
*/
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.neone.simbroker.ui.components.MainActivityTopBar
import de.neone.simbroker.ui.navigation.MainActivityBottomBar
import de.neone.simbroker.ui.theme.SimBrokerTheme
import de.neone.simbroker.ui.theme.bottomBarColorDark
import de.neone.simbroker.ui.theme.bottomBarColorLight
import de.neone.simbroker.ui.theme.topBarColorDark
import de.neone.simbroker.ui.theme.topBarColorLight
import de.neone.simbroker.ui.views.KontoView
import de.neone.simbroker.ui.views.PortfolioView
import de.neone.simbroker.ui.views.SuchenView
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimBrokerTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier,
                    topBar = {
                        MainActivityTopBar(
                            topBarColorLight = topBarColorLight,
                            topBarColorDark = topBarColorDark
                        )
                    },
                    bottomBar = {
                        MainActivityBottomBar(
                            bottomBarColorLight = bottomBarColorLight,
                            bottomBarColorDark = bottomBarColorDark,
                            navController = navController,
                            currentDestination = currentDestination,
                        )
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = SucheRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<PortfolioRoute> {
                            PortfolioView(innerPadding = innerPadding)
                        }
                        composable<SucheRoute> {
                            SuchenView(innerPadding = innerPadding)
                        }
                        composable<KontoRoute> {
                            KontoView(innerPadding = innerPadding)
                        }
                    }
                }
            }
        }
    }

    private fun startStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Serializable
    object PortfolioRoute

    @Serializable
    object SucheRoute

    @Serializable
    object KontoRoute

}
