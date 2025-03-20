package de.neone.simbroker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.SimBrokerDatabase
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
) : AndroidViewModel(application) {

    private val _refreshTimer = MutableStateFlow(60)
    val refreshTimer: StateFlow<Int> = _refreshTimer

    init {
        viewModelScope.launch {
            loadMoreCoins()

            while (true) {
                delay(1000)
                _refreshTimer.value -= 1
                if (_refreshTimer.value <= 0) {
                    refreshCoins()
                    _refreshTimer.value = 60
                }
            }
        }
    }

    // Pagination
    private var isLoading = false
    private var offset = 0
    private val limit = 50
    private var hasMoreData = true


    // Room
    private val portfolioDao =
        SimBrokerDatabase.getDatabase(application.applicationContext).portfolioDao()

    val portfolioCoins = portfolioDao.getAllPortfolioData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    // API Response
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    fun loadMoreCoins() {
        if (isLoading || !hasMoreData) return

        isLoading = true
        viewModelScope.launch {
            try {
                val newCoins = repository.getCoins(offset = offset, limit = limit)
                _coinList.value += newCoins
                offset += limit
                hasMoreData = newCoins.isNotEmpty()
            } catch (e: Exception) {
                Log.e("SimBrokerViewModel", "Fehler beim Laden der Coins", e)
            } finally {
                isLoading = false

            }
        }
    }

    private fun refreshCoins() {
        if (isLoading) {
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val refreshedCoins = repository.getCoins(offset = 0, limit = _coinList.value.size)
                if (refreshedCoins.isNotEmpty()) {
                    _coinList.value = refreshedCoins
                }
                Log.d("simDebug", "Coins aktualisiert: ${refreshedCoins.size}")
            } catch (e: Exception) {
                Log.e("simDebug", "Fehler beim Aktualisieren der Coins", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun addCoinToPortfolio(portfolioData: PortfolioData) {
        viewModelScope.launch {
            portfolioDao.insertPortfolioData(portfolioData)
        }
    }
}
