package de.neone.simbroker.ui.views.detailView.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata
import dev.anirban.charts.linear.BasicLinearStrategy
import dev.anirban.charts.linear.data.BasicDataStrategy
import dev.anirban.charts.linear.data.Coordinate
import dev.anirban.charts.linear.data.Coordinate.Companion.toCoordinateSet
import dev.anirban.charts.linear.data.LinearDataSet
import dev.anirban.charts.linear.decoration.LinearDecoration
import dev.anirban.charts.linear.legends.NoLegendStrategy
import dev.anirban.charts.linear.plots.GradientPlotStrategy


@Composable
fun ChartPlotter(
    modifier: Modifier = Modifier,
    coinSparklineData: List<String>,
) {
    // Daten für Y Achse mit 3 Werten
    val sparkRangeMin = coinSparklineData.minOrNull()?.toFloat() ?: 0f
    val sparkRangeMid = (sparkRangeMin + coinSparklineData.maxOrNull()!!.toFloat()) / 2f
    val sparkRangeMax = coinSparklineData.maxOrNull()?.toFloat() ?: 0f

    // Daten für X Achse mit der Länge der SparklineDaten
    val xAxisLabels = List(coinSparklineData.size) {
        Coordinate("${it}")
    }

    // Y Achsen festlegen
    val yAxisLabels = mutableListOf(
        Coordinate(sparkRangeMin),
        Coordinate(sparkRangeMid),
        Coordinate(sparkRangeMax)
    )

    // X Achsen festlegen
    val coinDataFloat = coinSparklineData.map {
        it.toFloat()
    }

    val linearDataSet: List<LinearDataSet> = listOf(
        LinearDataSet(
            title = "Chart",
            markers = coinDataFloat.toCoordinateSet(),
        )
    )


    BasicLinearStrategy.GradientPlot(

        modifier = modifier.fillMaxWidth(),

        legendDrawer = NoLegendStrategy,

        plot = GradientPlotStrategy(lineStroke = 5.0f, circleRadius = 15.0f),

        decoration = LinearDecoration(textColor = MaterialTheme.colorScheme.onBackground, plotPrimaryColor = listOf(Color.Yellow, Color.Black)),

        linearData = BasicDataStrategy(
            linearDataSets = linearDataSet,
            yAxisLabels = yAxisLabels.toMutableList(),
            xAxisLabels = xAxisLabels
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