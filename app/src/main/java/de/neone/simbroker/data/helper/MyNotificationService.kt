package de.neone.simbroker.data.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat

/**
 * Service zum Anzeigen wichtiger Nachrichten als System-Notifications und Toasts.
 *
 * - Erstellt bei Initialisierung einen NotificationChannel.
 * - Bietet eine Methode zum Anzeigen einer „Developer Mode“-Benachrichtigung.
 *
 * @param context Context zum Erstellen von Toasts und Notifications.
 */
class MyNotificationService(private val context: Context) {

    //==============================================================================================
    // 1) NotificationManager und Channel-Erstellung
    //==============================================================================================
    /** System NotificationManager, abgeleitet aus dem Context. */
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Definition des Channels für wichtige Nachrichten
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }
        notificationManager.createNotificationChannel(channel)
    }

    //==============================================================================================
    // 2) Benachrichtigung anzeigen
    //==============================================================================================
    /**
     * Zeigt einen kurzen Toast und eine System-Notification an,
     * um den Wechsel in den Developer Mode zu signalisieren.
     */
    fun showImportantMessageNotification() {
        // Kurzer Hinweis als Toast
        Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_SHORT).show()

        // Aufbau und Versand der Notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_TEXT)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "MyNotificationChannel"
        private const val CHANNEL_NAME = "Important Messages"
        private const val CHANNEL_DESCRIPTION = "Channel for important messages"
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_TITLE = "Developer Mode"
        private const val NOTIFICATION_TEXT = "Developer Mode is switched!"
        private const val TOAST_MESSAGE = "Developer Mode is switched!"
    }
}
