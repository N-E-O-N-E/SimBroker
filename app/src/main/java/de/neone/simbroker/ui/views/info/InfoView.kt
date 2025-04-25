package de.neone.simbroker.ui.views.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.neone.simbroker.R
import de.neone.simbroker.ui.theme.activity.ViewWallpaperImageBox

@Composable
fun InfoView() {
    val scrollState = rememberScrollState()

    // Screen-Pictures -----------------------------------------------------------------
    val screenPortfolioEmpty = R.drawable.screen_portfolioemptyscreen
    val screenAccount1 = R.drawable.screen_accountscreen1
    val screenAccount2 = R.drawable.screen_accountscreen2
    val screenAccount3 = R.drawable.screen_accountscreen3
    val screenCoins = R.drawable.screen_coinsscreen
    val screenCoinSearch = R.drawable.screen_coinssearchscreen

    val screenBuy = R.drawable.screen_buyscreen
    val screenSell = R.drawable.screen_sellscreen

    val screenPortfolio1 = R.drawable.screen_portfolioscreen1
    val screenPortfolio2 = R.drawable.screen_portfolioscreen2

    ViewWallpaperImageBox(
        imageLightTheme = R.drawable.simbroker_light_clear,
        imageDarkTheme = R.drawable.simbroker_dark_clear
    )
    Surface(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize()
            .padding(5.dp)
            .clip(shape = MaterialTheme.shapes.medium),
        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                Text("Basic instructions", style = MaterialTheme.typography.headlineSmall)
                Icon(painter = painterResource(id = R.drawable.sharp_chat_info_24), contentDescription = null)
            }
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(5.dp))

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenPortfolioEmpty),
                    contentDescription = "Start-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Der erste Start", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenAccount2),
                    contentDescription = "Account-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Account", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenCoins),
                    contentDescription = "Coins-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Die Crypto-Übersicht", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenCoinSearch),
                    contentDescription = "Suche-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Die Suchfunktion", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenBuy),
                    contentDescription = "Kaufen-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Crypto kaufen", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenSell),
                    contentDescription = "Verkaufen-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Crypto verkaufen", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenPortfolio1),
                    contentDescription = "Portfolio-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Portfolio", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenPortfolio2),
                    contentDescription = "PortfolioDetail-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Portfolio im Detail", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("dsdfsdsfdgfdgfdgfdgfgfgfdgsfgfdgjfdslkfdsfjdslkfjdslkfjlsdadsdfchjkdslfsa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }

    }

}

