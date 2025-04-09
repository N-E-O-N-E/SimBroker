package de.neone.simbroker.data.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MyNotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val channel = NotificationChannel(
            "MyNotificationChannel",
            "Important Messages",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for important messages"
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showImportantMessageNotification() {
        Toast.makeText(context, "Developer Mode is switched!", Toast.LENGTH_SHORT).show()

        val notification = NotificationCompat.Builder(context, "MyNotificationChannel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Developer Mode")
            .setContentText("Developer Mode is switched!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        notificationManager.notify(1, notification)
    }

}