package de.neone.simbroker.di

import de.neone.simbroker.data.remote.CoinbaseAPI
import de.neone.simbroker.data.repository.SimBrokerRepositoryImpl
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.ui.SimBrokerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {

    viewModelOf(::SimBrokerViewModel)

    single {
        CoinbaseAPI.retrofitService
    }

    single<SimBrokerRepositoryInterface> {
        SimBrokerRepositoryImpl(get())
    }




}