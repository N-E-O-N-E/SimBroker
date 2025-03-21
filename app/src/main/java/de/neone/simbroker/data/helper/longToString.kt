package de.neone.simbroker.data.helper

import android.annotation.SuppressLint

@SuppressLint("SimpleDateFormat")
object timeConverter {
    fun convertTimestampToReadableDate(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        return format.format(date)
    }
}
