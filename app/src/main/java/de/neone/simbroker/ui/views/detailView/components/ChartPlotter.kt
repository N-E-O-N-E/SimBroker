package de.neone.simbroker.ui.views.detailView.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.neone.simbroker.data.helper.Helper
import de.neone.simbroker.data.helper.Helper.roundTo
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata
import de.neone.simbroker.ui.theme.chartDark
import de.neone.simbroker.ui.theme.chartLight
import dev.anirban.charts.linear.BasicLinearStrategy
import dev.anirban.charts.linear.data.BasicDataStrategy
import dev.anirban.charts.linear.data.Coordinate
import dev.anirban.charts.linear.data.Coordinate.Companion.toCoordinateSet
import dev.anirban.charts.linear.data.LinearDataSet
import dev.anirban.charts.linear.decoration.LinearDecoration
import dev.anirban.charts.linear.plots.GradientPlotStrategy


@Composable
fun ChartPlotter(
    modifier: Modifier = Modifier,
    coinSparklineData: List<String> = emptyList(),
) {

    val xAxisLabels = List(coinSparklineData.size) {
        // Coordinate("${ it + 1 }")
        Coordinate("")
    }

    val coinDataFloat = coinSparklineData
        .filterNotNull()
        .mapNotNull { it.toFloatOrNull() }

    val sparkRangeMin = coinDataFloat.minOrNull()?.roundTo(2) ?: 0f
    val sparkRangeMax = coinDataFloat.maxOrNull()?.roundTo(2) ?: 1f
    val sparkRangeMid = ((sparkRangeMin + sparkRangeMax) / 2f).roundTo(2)

    // Y Achsen festlegen
    val yAxisLabels = listOf(
        Coordinate(sparkRangeMin),
        Coordinate("Bear"),
        Coordinate("Down"),
        Coordinate("Up"),
        Coordinate("Bull"),
        Coordinate(sparkRangeMax),
    )

    val normalizedData = Helper.normalizeValues(coinDataFloat, 5f)
    val linearDataSet: List<LinearDataSet> = listOf(
        LinearDataSet(
            title = "Chart",
            markers = normalizedData.toCoordinateSet()
        )
    )

    BasicLinearStrategy.GradientPlot(
        modifier = Modifier.fillMaxWidth().height(450.dp).padding(15.dp),
        plot = GradientPlotStrategy(
            lineStroke = 6.0f,
            circleRadius = 0.0f
        ),

        decoration = LinearDecoration(
            textColor = MaterialTheme.colorScheme.onBackground,
            plotPrimaryColor = listOf(if(isSystemInDarkTheme()) chartDark else chartLight)
        ),

        linearData = BasicDataStrategy(
            linearDataSets = linearDataSet,
            yAxisLabels = yAxisLabels.toMutableList(),
            xAxisLabels = xAxisLabels,
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChartPlotterPreview() {
    ChartPlotter(
        coinSparklineData = coins_Mockdata.first().sparkline
    )
}