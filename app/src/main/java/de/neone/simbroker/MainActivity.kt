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
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> Log.i("MainActivity", if (isGranted) "Granted" else "Denied") }


    /**
     * Überprüft und fordert die Berechtigung zum Senden von Push-Benachrichtigungen (POST_NOTIFICATIONS) an.
     *
     * - Wenn die Berechtigung bereits erteilt wurde, wird dies geloggt.
     * - Falls eine Erläuterung nötig ist (z.B. weil der User zuvor abgelehnt hat), wird die angegebene
     *   `showExplanation`-Callback-Funktion ausgeführt.
     * - Ansonsten wird direkt der Berechtigungsdialog geöffnet.
     *
     * Diese Funktion ist ab Android 13 (TIRAMISU) relevant, da hier die Notification Permission eingeführt wurde.
     *
     * @param showExplanation Lambda-Funktion, die aufgerufen wird, wenn eine Erklärung angezeigt werden soll,
     *                         warum die App Benachrichtigungsrechte benötigt.
     */

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission(showExplanation: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED ->
                Log.i("MainActivity", "Permission granted")

            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) ->
                showExplanation()

            else -> requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                requestNotificationPermission {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

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

}
