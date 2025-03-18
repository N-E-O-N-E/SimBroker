package de.neone.simbroker.di

import de.neone.simbroker.data.local.PortfolioDao
import de.neone.simbroker.data.local.PortfolioDatabase
import de.neone.simbroker.data.remote.CoinbaseAPI
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.data.repository.SimBrokerRepositoryMock
import de.neone.simbroker.ui.SimBrokerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {

    viewModelOf(::SimBrokerViewModel)

    single<PortfolioDao> {
        get<PortfolioDatabase>().portfolioDao()
    }

    single<PortfolioDatabase> {
        PortfolioDatabase.getDatabase(get())
    }

    single {
        CoinbaseAPI.retrofitService
    }

//    single<SimBrokerRepositoryInterface> {
//        SimBrokerRepositoryImpl(get())
//    }

    single<SimBrokerRepositoryInterface> {
        SimBrokerRepositoryMock()
    }
}