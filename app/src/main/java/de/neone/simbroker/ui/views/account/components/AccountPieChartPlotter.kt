package de.neone.simbroker.ui.views.account.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.anirban.charts.circular.charts.DonutRowPlotStrategy
import dev.anirban.charts.circular.data.ListDataStrategy
import dev.anirban.charts.circular.decoration.CircularDecoration
import dev.anirban.charts.circular.foreground.DonutForegroundStrategy

@Composable
fun AccountPieChartPlotter(
    modifier: Modifier = Modifier,
    creditValue: Double,
    investedValue: Double,
    fees: Double,
) {

    val dataSet1 = ListDataStrategy(
        itemsList = listOf(
            Pair("Your Credit", creditValue.toFloat()),
            Pair("Investments", investedValue.toFloat())
        ),
        unit = "€" // Unit
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        DonutRowPlotStrategy.DonutPlotRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            circularData = dataSet1,
            circularForeground = DonutForegroundStrategy(
                strokeWidth = 120f,
                radiusMultiplier = 1.4f
            ),
            circularDecoration = CircularDecoration.donutChartDecorations(
                textColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        Text(
            "Fees over all time: ${fees.toInt()} €",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PieChartPlotterPreview() {
    AccountPieChartPlotter(
        creditValue = 100.0,
        investedValue = 80.0,
        fees = 10.0
    )
}