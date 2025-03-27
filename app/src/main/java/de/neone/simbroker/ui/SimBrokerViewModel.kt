package de.neone.simbroker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.PortfolioPositions
import de.neone.simbroker.data.local.SimBrokerDatabase
import de.neone.simbroker.data.local.TransactionPositions
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
) : AndroidViewModel(application) {

    // Room Database
    private val simBrokerDatabase = SimBrokerDatabase.getDatabase(application.applicationContext).simBrokerDAO()

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

    // API Response
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    fun loadMoreCoins() {
        if (isLoading || !hasMoreData) return

        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newCoins = repository.getCoins(offset = offset, limit = limit)
                _coinList.value = _coinList.value + newCoins // nicht mit += da sich die Referenz nicht mitändert!
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


    fun addTransaction(transaction: TransactionPositions) {
        Log.d("simDebug", "addTransaction over ViewModel started")
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun addPortfolio(portfolio: PortfolioPositions) {
        Log.d("simDebug", "addPosition over ViewModel started")
        viewModelScope.launch {
            repository.insertPortfolio(portfolio)
        }
    }

    private val _allPortfolioPositions = MutableStateFlow<List<PortfolioPositions>>(emptyList())
    val allPortfolioPositions: StateFlow<List<PortfolioPositions>> = _allPortfolioPositions

    fun getAllPortfolioPositions() {
        viewModelScope.launch {
            _allPortfolioPositions.value = repository.getAllPortfolioPositions()
        }
    }

    private val _allTransactionPositions = MutableStateFlow<List<TransactionPositions>>(emptyList())
    val allTransactionPositions: StateFlow<List<TransactionPositions>> = _allTransactionPositions

    fun getAllTransactionPositions() {
        viewModelScope.launch {
            _allTransactionPositions.value = repository.getAllTransactionPositions()
        }
    }




}
