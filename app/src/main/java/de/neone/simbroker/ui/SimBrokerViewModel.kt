package de.neone.simbroker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.SimBrokerDatabase
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import kotlinx.coroutines.Dispatchers
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

    // Room Database
    private val simBrokerDatabase =
        SimBrokerDatabase.getDatabase(application.applicationContext).simBrokerDAO()

    // Pagination
    private var isLoading = false
    private var offset = 0
    private val limit = 50
    private var hasMoreData = true

    private val _refreshTimer = MutableStateFlow(0)
    val refreshTimer: StateFlow<Int> = _refreshTimer

    init {
        viewModelScope.launch {
            loadMoreCoins()
                startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                _refreshTimer.value = 60
                for (i in 60 downTo 1) {
                    _refreshTimer.value = i
                    delay(1000)
                }
                refreshCoins()
            }
        }
    }

    fun buyCoin(coin: Coin, amount: Double, price: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.buyCoin(
                    coin = coin,
                    amount = amount,
                    price = price
                )
            } catch (e: Exception) {
                Log.e("SimBrokerViewModel", "Fehler beim Kauf", e)
            }
        }
    }

    fun sellCoin(coin: Coin, amount: Double, price: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.sellCoin(
                    coin = coin,
                    amount = amount,
                    price = price
                )
            } catch (e: Exception) {
                Log.e("SimBrokerViewModel", "Fehler beim Verkaufen", e)
            }
        }
    }

    // Room Datenströme Flow

    val allPortfolioPositions = repository.getAllPortfolioPositions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val sparklineDataByCoinUuid = { coinUuid: String ->
        repository.getSparklineDataByCoinUuid(coinUuid)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )
    }


    // API Response
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    fun loadMoreCoins() {
        if (isLoading || !hasMoreData) return

        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val refreshedCoins =
                    repository.getCoins(offset = 0, limit = limit)
                if (refreshedCoins.isNotEmpty()) {
                    _coinList.value = refreshedCoins
                }
                Log.d("simDebug", "Coins aktualisiert: ${refreshedCoins.size}")
            } catch (e: Exception) {
                Log.e("simDebug", "Fehler beim Aktualisieren der Coins", e)
            }
        }
    }
}
