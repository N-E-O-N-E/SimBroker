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
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.neone.simbroker.ui.navigation.MainActivityBottomBar
import de.neone.simbroker.ui.navigation.MainActivityTopBar
import de.neone.simbroker.ui.navigation.NavHostComponent
import de.neone.simbroker.ui.theme.SimBrokerTheme
import de.neone.simbroker.ui.theme.bottomBarColorDark
import de.neone.simbroker.ui.theme.bottomBarColorLight
import de.neone.simbroker.ui.theme.topBarColorDark
import de.neone.simbroker.ui.theme.topBarColorLight
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

                    NavHostComponent(
                        innerPadding = innerPadding,
                        navController = navController,
                        action = {
                            startStartActivity()
                        }
                    )
                }
            }
        }
    }

    private fun startStartActivity() {
        Log.d("simDebug", "StartActivity aufgerufen")
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Serializable
    object PortfolioRoute

    @Serializable
    object SearchRoute

    @Serializable
    object AccountRoute

    @Serializable
    object LogoutRoute
}
