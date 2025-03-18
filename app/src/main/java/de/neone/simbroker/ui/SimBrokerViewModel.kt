package de.neone.simbroker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.PortfolioDatabase
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
) : AndroidViewModel(application) {

    // Room
    private val portfolioDao =
        PortfolioDatabase.getDatabase(application.applicationContext).portfolioDao()
    val portfolioCoins = portfolioDao.getAllPortfolioData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    // API Response
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    suspend fun fetchCoins() {
        try {
            val coins = repository.getCoins()
            _coinList.value = coins

        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun fetchCoin(uuid: String) {
        try {
            val coin = repository.getCoin(uuid)
            _coinList.value = listOf(coin)

        } catch (e: Exception) {
            // Handle error
        }
    }

    fun buyCoin(portfolioData: PortfolioData) {
        viewModelScope.launch {
            portfolioDao.insertPortfolioData(portfolioData)
        }
    }
}
