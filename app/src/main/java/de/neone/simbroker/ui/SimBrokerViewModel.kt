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
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    // API Response
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    fun fetchCoins() {
        viewModelScope.launch {
            try {
                val newCoins = repository.getCoins()
                _coinList.value += newCoins
            } catch (e: Exception) {

            }
        }
    }

    suspend fun fetchCoin(uuid: String, timePeriod: String) {
        try {
            val coin = repository.getCoin(uuid, timePeriod = timePeriod)
            _coinList.value = listOf(coin)

        } catch (e: Exception) {

        }
    }

    fun buyCoin(portfolioData: PortfolioData) {
        viewModelScope.launch {
            portfolioDao.insertPortfolioData(portfolioData)
        }
    }
}
