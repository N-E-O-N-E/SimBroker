package de.neone.simbroker.di

import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SimBrokerDatabase
import de.neone.simbroker.data.remote.CoinbaseAPI
import de.neone.simbroker.data.repository.SimBrokerRepositoryImpl
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.data.repository.SimBrokerRepositoryMock
import de.neone.simbroker.ui.SimBrokerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single {
        CoinbaseAPI.retrofitService
    }

    single<SimBrokerDatabase> {
        SimBrokerDatabase.getDatabase(androidContext())
    }

    single<SimBrokerDAO> {
        get<SimBrokerDatabase>().simBrokerDAO()
    }


    single<SimBrokerRepositoryInterface>(qualifier = named("real")) {
        SimBrokerRepositoryImpl(get(), get())
    }

    single<SimBrokerRepositoryInterface>(qualifier = named("mock")) {
        SimBrokerRepositoryMock(get())
    }

    viewModel {
        SimBrokerViewModel(
            application = androidApplication(),
            realRepo = get(named("real")),
            mockRepo = get(named("mock"))
        )
    }

}