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
import de.neone.simbroker.data.helper.SBHelper.roundTo
import dev.anirban.charts.circular.charts.DonutRowPlotStrategy
import dev.anirban.charts.circular.data.ListDataStrategy
import dev.anirban.charts.circular.decoration.CircularDecoration
import dev.anirban.charts.circular.foreground.DonutForegroundStrategy

/**
 * Zeichnet ein Donut-Diagramm mit den Werten für Wallet-Kredit und Investitionen
 * sowie eine Textanzeige für die gesamten Gebühren.
 *
 * @param modifier Modifier zur Anpassung von Größe und Layout.
 * @param creditValue Aktueller Wallet-Kreditbetrag.
 * @param investedValue Aktuell investierter Gesamtbetrag.
 * @param fees Gesamtsumme aller Gebühren.
 */
@Composable
fun AccountPieChartPlotter(
    modifier: Modifier = Modifier,
    creditValue: Double,
    investedValue: Double,
    fees: Double,
) {
    //==============================================================================================
    // 1) Daten-Strategie erstellen
    //==============================================================================================
    val dataSet1 = ListDataStrategy(
        itemsList = listOf(
            Pair("Wallet", creditValue.toFloat().roundTo(2)),
            Pair("Investments", investedValue.toFloat())
        ),
        unit = "€" // Einheit für die Beschriftung
    )

    //==============================================================================================
    // 2) UI-Aufbau: Donut-Chart und Gebühren-Text
    //==============================================================================================
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        //------------------------------------------------------------------------------------------
        // 2.1) Donut-Chart-Reihe
        //------------------------------------------------------------------------------------------
        DonutRowPlotStrategy.DonutPlotRow(
            modifier = modifier.fillMaxWidth(),
            circularData = dataSet1,
            circularForeground = DonutForegroundStrategy(
                strokeWidth = 80f,
                radiusMultiplier = 1.3f
            ),
            circularDecoration = CircularDecoration.donutChartDecorations(
                textColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        //------------------------------------------------------------------------------------------
        // 2.2) Textanzeige der Gebühren
        //------------------------------------------------------------------------------------------
        Text(
            text = "Fees over all time: ${fees.toInt()} €",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(horizontal = 65.dp)
                .padding(bottom = 10.dp)
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
