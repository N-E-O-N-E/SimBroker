package de.neone.simbroker.data.helper

import android.annotation.SuppressLint

object Helper {

    @SuppressLint("SimpleDateFormat")
    fun timestampToString(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("dd.MM.yy")
        return format.format(date)
    }

}