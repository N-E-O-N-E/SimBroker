package de.neone.simbroker.di

import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SimBrokerDatabase
import de.neone.simbroker.data.remote.CoinbaseAPI
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.data.repository.SimBrokerRepositoryMock
import de.neone.simbroker.ui.SimBrokerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin Dependency Injection Module für die SimBroker-App.
 *
 * Stellt folgende Komponenten bereit:
 * 1) ViewModel
 * 2) Remote-API-Service
 * 3) Room-Datenbank & DAO
 * 4) Repository-Implementierung (Mock oder Real)
 */
val appModule = module {

    //==============================================================================================
    // 1) ViewModel
    //==============================================================================================
    /**
     * Registriert [SimBrokerViewModel] als Koin ViewModel.
     */
    viewModelOf(::SimBrokerViewModel)

    //==============================================================================================
    // 2) Remote API Service
    //==============================================================================================
    /**
     * Singleton für das Retrofit-Service-Interface [CoinbaseAPI.retrofitService].
     */
    single {
        CoinbaseAPI.retrofitService
    }

    //==============================================================================================
    // 3) Room Datenbank & DAO
    //==============================================================================================
    /**
     * Singleton für die Room-Datenbank [SimBrokerDatabase].
     */
    single<SimBrokerDatabase> {
        SimBrokerDatabase.getDatabase(androidContext())
    }

    /**
     * Singleton für das Data Access Object [SimBrokerDAO].
     */
    single<SimBrokerDAO> {
        get<SimBrokerDatabase>().simBrokerDAO()
    }

    //==============================================================================================
    // 4) Repository-Implementierung
    //==============================================================================================

    // RealData-Implementation (auskommentiert)

    // single<SimBrokerRepositoryInterface> {
    //     SimBrokerRepositoryImpl(get(), get())
    // }

    /**
     * Mock-Repository für Tests und einfache Demo-Daten.
     *
     * Implementiert [SimBrokerRepositoryInterface] auf Basis lokaler Mock-Daten.
     */

    single<SimBrokerRepositoryInterface> {
        SimBrokerRepositoryMock(get())
    }
}
