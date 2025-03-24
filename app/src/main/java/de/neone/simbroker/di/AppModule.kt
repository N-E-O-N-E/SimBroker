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


val appModule = module {

    viewModelOf(::SimBrokerViewModel)

    single<SimBrokerDAO> {
        get<SimBrokerDatabase>().simBrokerDAO()
    }

    single<SimBrokerDatabase> {
        SimBrokerDatabase.getDatabase(androidContext())
    }

    single {
        CoinbaseAPI.retrofitService
    }

//    single<SimBrokerRepositoryInterface> {
//        SimBrokerRepositoryImpl(get(), get())
//    }

    single<SimBrokerRepositoryInterface> {
        SimBrokerRepositoryMock(get())
    }

}