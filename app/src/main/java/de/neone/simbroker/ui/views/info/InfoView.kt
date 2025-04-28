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
    val screenLoading = R.drawable.screen_loadingscreen

    val screenPortfolioEmpty = R.drawable.screen_portfolioemptyscreen
    val screenAccount = R.drawable.screen_accountscreen
    val screenCoins = R.drawable.screen_coinsscreen
    val screenCoinSearch = R.drawable.screen_coinssearchscreen

    val screenBuy = R.drawable.screen_buyscreen

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

                Text("SimBroker instructions", style = MaterialTheme.typography.headlineSmall)
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
                    painter = painterResource(id = screenLoading),
                    contentDescription = "Load-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Welcome to SimBroker!", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Discover the thrill of trading with SimBroker!\n\n" +
                            "SimBroker is a gamified trading platform developed as part of a final project.\n\n" +
                            "Trade real cryptocurrencies — with zero risk and maximum excitement!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

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
                        "Empty Portfolio", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("When you start the game, your portfolio will be empty.\n\n" +
                            "Go to \"Coins\" to browse the available cryptocurrencies. \n\n" +
                            "Or visit your \"Account\" to set up the game settings and make your first crypto purchase.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row() {
                Image(
                    modifier = Modifier
                        .height(350.dp)
                        .padding(10.dp),
                    painter = painterResource(id = screenAccount),
                    contentDescription = "Account-Screen",
                    contentScale = ContentScale.FillHeight,
                )
                Column {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Account", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("In your account, you can see how much available credit you have, as well as your invested capital.\n\n" +
                            "You can set the game difficulty and transaction fees, or restart the game at any time. " +
                            "A ranking system tracks your achievements. " +
                            "Once you have earned €1,000, you win the game and playing in Free-Play mode.",
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
                        "The Crypto Overview", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Under \"Coins,\" you'll find 100 current cryptocurrencies available for trading.\n\n" +
                            "Minute-by-minute price updates directly impact your profits and losses.",
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
                        "Search", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Use the quick search to find coins by clicking on the magnifying glass.",
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
                        "Buy/Sell Crypto", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Trade cryptocurrencies at live market prices by simply entering an amount or quantity.\n\n" +
                            "As you level up, a powerful multiplier boosts your potential profits — or increases your risk of losses.\n\n" +
                            "Choose wisely and master the market!",
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

                    Text("View and manage all your crypto holdings at a glance.\n\n" +
                            "Track your current portfolio value, monitor your ranking, and easily create a list of your favorite coins for faster access.\n\n" +
                            "Stay organized and keep an eye on your overall trading performance!",
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
                        "Portfolio Details", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Dive deeper into each coin in your portfolio.\n\n" +
                            "Expand detailed views to see interactive price charts, review your individual transactions, and analyze your trading history per coin.\n\n" +
                            "Gain the insights you need to optimize your strategy!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }

    }

}

