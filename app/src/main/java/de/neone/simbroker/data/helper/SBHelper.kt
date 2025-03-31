package de.neone.simbroker.data.helper

import android.annotation.SuppressLint
import kotlin.math.pow
import kotlin.math.roundToInt

object SBHelper {

    @SuppressLint("SimpleDateFormat")
    fun timestampToString(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("dd.MM.yy")
        return format.format(date)
    }

    fun Float.roundTo(decimals: Int): Float {
        val factor = 10.0.pow(decimals) // 10^x
        return (this * factor).roundToInt() / factor.toFloat() // Wert x mal Faktor und aufrunden / Faktor
    }

    fun normalizeValues(data: List<Float>, scaleMax: Float = 5f): List<Float> {
        val min = data.minOrNull() ?: 0f
        val max = data.maxOrNull() ?: 1f
        if (max == min) return List(data.size) { scaleMax / 2f } // alle Werte gleich
        return data.map { ((it - min) / (max - min)) * scaleMax }
    }


}