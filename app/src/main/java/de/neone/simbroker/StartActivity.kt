package de.neone.simbroker

/*
    ███╗   ██╗    ███████╗     ██████╗     ███╗   ██╗    ███████╗
    ████╗  ██║    ██╔════╝    ██╔═══██╗    ████╗  ██║    ██╔════╝
    ██╔██╗ ██║    █████╗      ██║   ██║    ██╔██╗ ██║    █████╗
    ██║╚██╗██║    ██╔══╝      ██║   ██║    ██║╚██╗██║    ██╔══╝
    ██║ ╚████║    ███████╗    ╚██████╔╝    ██║ ╚████║    ███████╗
    ╚═╝  ╚═══╝    ╚══════╝     ╚═════╝     ╚═╝  ╚═══╝    ╚══════╝
*/

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import de.neone.simbroker.ui.theme.SimBrokerTheme
import de.neone.simbroker.ui.theme.activity.StartActivityImageBox

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "simBroker")

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimBrokerTheme {
                Scaffold { innerPadding ->
                    StartActivityImageBox(
                        innerPadding,
                        toMainActivity = {
                            startMainActivity()
                        },
                        buttonText = "Handel starten",
                        imageLightTheme = R.drawable.simbroker_light,
                        imageDarkTheme = R.drawable.simbroker_dark,
                    )
                }
            }
        }
    }

    private fun startMainActivity() {
        Log.d("simDebug", "MainActivity aufgerufen")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}