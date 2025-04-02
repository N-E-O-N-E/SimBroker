package de.neone.simbroker.ui.views.account.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.anirban.charts.circular.charts.DonutColumnPlotStrategy
import dev.anirban.charts.circular.data.ListDataStrategy
import dev.anirban.charts.circular.decoration.CircularDecoration
import dev.anirban.charts.circular.foreground.DonutForegroundStrategy
import dev.anirban.charts.circular.legend.GridLegendStrategy

@Composable
fun AccountPieChartPlotter(
    modifier: Modifier = Modifier,
    creditValue: Double,
    investedValue: Double,
) {

    val dataSet1 = ListDataStrategy(
        itemsList = listOf(
            Pair("Credit", creditValue.toFloat()),
            Pair("Invested", investedValue.toFloat())
        ),
        unit = "€" // Unit
    )

    DonutColumnPlotStrategy.CustomDonutPlotColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        circularData = dataSet1,
        circularForeground = DonutForegroundStrategy(
            strokeWidth = 120f,
        ),
        circularDecoration = CircularDecoration.donutChartDecorations(
            textColor = MaterialTheme.colorScheme.onSurface,
        ),
        legend = GridLegendStrategy(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PieChartPlotterPreview() {
    AccountPieChartPlotter(
        creditValue = 100.0,
        investedValue = 80.0
    )
}