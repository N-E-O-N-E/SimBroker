package de.neone.simbroker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import de.neone.simbroker.ui.theme.SimBrokerTheme
import de.neone.simbroker.ui.theme.navBarColor
import de.neone.simbroker.ui.theme.topBarColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimBrokerTheme {
                Scaffold(
                    modifier = Modifier,
                    topBar = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(40.dp),
                            shape = RectangleShape,
                            color = topBarColor
                        ){

                        }
                    },
                    bottomBar = {
                        Box {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth(1f),
                                shape = RectangleShape,
                                color = topBarColor
                            ) {
                            }
                        }
                        NavigationBar(containerColor = navBarColor) {

                        }
                    }

                ) { innerPadding ->

                }
            }
        }
    }
}
