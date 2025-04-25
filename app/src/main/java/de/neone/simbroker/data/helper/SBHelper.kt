package de.neone.simbroker.data.helper

import android.annotation.SuppressLint
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Hilfsklasse mit nützlichen Funktionen für:
 * - Zeitstempel-Formatierung
 * - Rundungs- und Formatierungs-Extensions
 * - Normalisierung von Datenreihen
 */
object SBHelper {

    //==============================================================================================
    // 1) Zeitstempel in lesbare Strings umwandeln
    //==============================================================================================

    /**
     * Wandelt einen Unix-Timestamp (Millis) in einen kurzen Datums-String um.
     *
     * Format: "dd.MM.yy", z.B. "25.04.25"
     *
     * @param timestamp Zeit in Millisekunden seit 1. Januar 1970.
     * @return Formatiertes Datum.
     */
    @SuppressLint("SimpleDateFormat")
    fun timestampToString(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("dd.MM.yy")
        return format.format(date)
    }

    /**
     * Wandelt einen Unix-Timestamp (Millis) in einen ausführlichen Datums- und Zeit-String um.
     *
     * Format: "dd.MM.yyyy, HH:mm:ss", z.B. "25.04.2025, 14:30:45"
     *
     * @param timestamp Zeit in Millisekunden seit 1. Januar 1970.
     * @return Formatiertes Datum und Uhrzeit.
     */
    @SuppressLint("SimpleDateFormat")
    fun timestampToStringLong(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("dd.MM.yyyy, HH:mm:ss")
        return format.format(date)
    }

    //==============================================================================================
    // 2) Float-Erweiterungen für Rundung und Skalierung
    //==============================================================================================

    /**
     * Rundet einen Float-Wert auf die angegebene Anzahl Nachkommastellen.
     *
     * @receiver Wert, der gerundet werden soll.
     * @param decimals Anzahl der Nachkommastellen.
     * @return Gerundeter Wert.
     */
    fun Float.roundTo(decimals: Int): Float {
        val factor = 10.0.pow(decimals)
        return (this * factor).roundToInt() / factor.toFloat()
    }

    /**
     * Normalisiert eine Liste von Float-Werten in den Bereich 0..scaleMax.
     *
     * @param data Ursprungsdaten.
     * @param scaleMax Maximalwert nach der Normalisierung (Standard 5f).
     * @return Liste normierter Werte.
     */
    fun normalizeValues(data: List<Float>, scaleMax: Float = 5f): List<Float> {
        val min = data.minOrNull() ?: 0f
        val max = data.maxOrNull() ?: 1f
        if (max == min) {
            // Alle Werte gleich: Mittelwert zurückgeben
            return List(data.size) { scaleMax / 2f }
        }
        return data.map { ((it - min) / (max - min)) * scaleMax }
    }

    //==============================================================================================
    // 3) Double-Erweiterungen für präzises Runden
    //==============================================================================================

    /**
     * Rundet einen Double-Wert auf 2 Nachkommastellen.
     *
     * @receiver Wert, der gerundet werden soll.
     * @return Gerundeter Wert (2 Nachkommastellen).
     */
    fun Double.roundTo2(): Double =
        BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN).toDouble()

    /**
     * Rundet einen Double-Wert auf 6 Nachkommastellen.
     *
     * @receiver Wert, der gerundet werden soll.
     * @return Gerundeter Wert (6 Nachkommastellen).
     */
    fun Double.roundTo6(): Double =
        BigDecimal(this).setScale(6, RoundingMode.HALF_EVEN).toDouble()

    /**
     * Rundet einen Double-Wert auf 8 Nachkommastellen.
     *
     * @receiver Wert, der gerundet werden soll.
     * @return Gerundeter Wert (8 Nachkommastellen).
     */
    fun Double.roundTo8(): Double =
        BigDecimal(this).setScale(8, RoundingMode.HALF_EVEN).toDouble()

    //==============================================================================================
    // 4) Double-Erweiterungen für Formatierung
    //==============================================================================================

    /**
     * Formatiert einen Double-Wert als Euro-Betrag mit 2 Nachkommastellen.
     *
     * Beispiel: 12.3 -> "12.30 €"
     *
     * @receiver Betrag als Double.
     * @return Formatierter String mit " €".
     */
    fun Double.toEuroString(): String =
        "%.2f €".format(this)

    /**
     * Formatiert einen Double-Wert als Prozentwert mit 2 Nachkommastellen.
     *
     * Beispiel: 0.1234 -> "0.12 %"
     *
     * @receiver Wert als Double.
     * @return Formatierter String mit " %".
     */
    fun Double.toPercentString(): String =
        "%.2f %%".format(this)

    /**
     * Formatiert einen Double-Wert als Coin-Menge mit 6 Nachkommastellen.
     *
     * Beispiel: 0.12345678 -> "0.123457"
     *
     * @receiver Wert als Double.
     * @return Formatierter String ohne Währungssymbol.
     */
    fun Double.toCoinString(): String =
        "%.6f".format(this)
}
