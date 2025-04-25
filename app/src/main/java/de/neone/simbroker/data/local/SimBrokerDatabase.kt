package de.neone.simbroker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions

/**
 * Room-Datenbank für die SimBroker-App.
 *
 * Definiert Tabellen für Transaktionen und Portfolio-Positionen.
 * Version 1, Schema nicht exportiert.
 */
@Database(
    entities = [
        TransactionPositions::class,
        PortfolioPositions::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SimBrokerDatabase : RoomDatabase() {

    //==============================================================================================
    // 1) DAO-Zugriff
    //==============================================================================================

    /**
     * Liefert das Data Access Object für Portfolio- und Transaktions-Operationen.
     */
    abstract fun simBrokerDAO(): SimBrokerDAO

    //==============================================================================================
    // 2) Singleton-Instanzverwaltung
    //==============================================================================================

    companion object {
        /** Volatile-Variable zur sicheren, mehrfachen Initialisierung in Multithread-Umgebungen. */
        @Volatile
        private var Instance: SimBrokerDatabase? = null

        /**
         * Gibt die Singleton-Instanz der Datenbank zurück.
         * Erzeugt sie bei erstem Aufruf thread-sicher.
         *
         * @param context Context zum Aufbau der Room-Datenbank.
         * @return Instanz von [SimBrokerDatabase].
         */
        fun getDatabase(context: Context): SimBrokerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SimBrokerDatabase::class.java,
                    "simBrokerDatabase"
                )
                    // Bei Schema-Änderungen wird destruktiv migriert (Datenverlust).
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
