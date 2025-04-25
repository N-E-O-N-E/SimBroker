package de.neone.simbroker.ui.views.coinDetailView.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.neone.simbroker.data.helper.SBHelper
import de.neone.simbroker.data.helper.SBHelper.roundTo
import de.neone.simbroker.data.local.mockdata.coins_Mockdata
import de.neone.simbroker.ui.theme.chartDark
import de.neone.simbroker.ui.theme.chartLight
import dev.anirban.charts.linear.BasicLinearStrategy
import dev.anirban.charts.linear.data.BasicDataStrategy
import dev.anirban.charts.linear.data.Coordinate
import dev.anirban.charts.linear.data.Coordinate.Companion.toCoordinateSet
import dev.anirban.charts.linear.data.LinearDataSet
import dev.anirban.charts.linear.decoration.LinearDecoration
import dev.anirban.charts.linear.plots.GradientPlotStrategy

/**
 * Zeichnet das Sparkline-Diagramm für die Coin-Detail-Ansicht.
 *
 * - Wandelt die übergebenen Strings in Float-Werte um.
 * - Bestimmt Min-, Max- und Mittelwerte für die Y-Achse.
 * - Normalisiert die Daten auf einen 0–5-Bereich.
 * - Rendert einen Gradient-Plot, dessen Farben sich am System-Theme orientieren.
 *
 * @param modifier Modifier zur Anpassung von Größe und Position des Plots.
 * @param coinSparklineData Liste von Kurswerten als Strings.
 */
@Composable
fun CoinDetailChartPlotter(
    modifier: Modifier = Modifier,
    coinSparklineData: List<String> = emptyList(),
) {
    //==============================================================================================
    // 1) X-Achsen-Beschriftungen (Platzhalter)
    //==============================================================================================
    val xAxisLabels = List(coinSparklineData.size) {
        Coordinate("")
    }

    //==============================================================================================
    // 2) Umwandlung der Sparkline-Daten in Floats
    //==============================================================================================
    val coinDataFloat = coinSparklineData
        .filterNotNull()
        .mapNotNull { it.toFloatOrNull() }

    //==============================================================================================
    // 3) Bestimmung von Min, Max und Mittelwert
    //==============================================================================================
    val sparkRangeMin = coinDataFloat.minOrNull()?.roundTo(2) ?: 0f
    val sparkRangeMax = coinDataFloat.maxOrNull()?.roundTo(2) ?: 1f
    val sparkRangeMid = ((sparkRangeMin + sparkRangeMax) / 2f).roundTo(2)

    //==============================================================================================
    // 4) Y-Achsen-Beschriftungen festlegen
    //==============================================================================================
    val yAxisLabels = listOf(
        Coordinate(sparkRangeMin),
        Coordinate("Bear"),
        Coordinate("Down"),
        Coordinate("Up"),
        Coordinate("Bull"),
        Coordinate(sparkRangeMax),
    )

    //==============================================================================================
    // 5) Daten normalisieren und DataSet erstellen
    //==============================================================================================
    val normalizedData = SBHelper.normalizeValues(coinDataFloat, 5f)
    val linearDataSet: List<LinearDataSet> = listOf(
        LinearDataSet(
            title = "Chart",
            markers = normalizedData.toCoordinateSet()
        )
    )

    //==============================================================================================
    // 6) Gradient-Plot rendern
    //==============================================================================================
    BasicLinearStrategy.GradientPlot(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.30f)
            .padding(5.dp),
        plot = GradientPlotStrategy(
            lineStroke = 4.0f,
            circleRadius = 0.0f
        ),
        decoration = LinearDecoration(
            textColor = MaterialTheme.colorScheme.onBackground,
            plotPrimaryColor = listOf(
                if (isSystemInDarkTheme()) chartDark else chartLight
            )
        ),
        linearData = BasicDataStrategy(
            linearDataSets = linearDataSet,
            yAxisLabels = yAxisLabels.toMutableList(),
            xAxisLabels = xAxisLabels,
        )
    )
}

/**
 * Preview für [CoinDetailChartPlotter] mit Mock-Daten.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChartPlotterPreview() {
    CoinDetailChartPlotter(
        coinSparklineData = coins_Mockdata.first().sparkline
    )
}
