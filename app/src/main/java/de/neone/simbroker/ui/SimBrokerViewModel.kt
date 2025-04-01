package de.neone.simbroker.ui

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val DATASTORE_MOCKDATA = booleanPreferencesKey("mockData")
private val DATASTORE_ACCOUNTVALUE = doublePreferencesKey("accountValue")
private val DATASTORE_TOTALINVESTVALUE = doublePreferencesKey("totalInvested")

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
) : AndroidViewModel(application) {
    // Fee
    val fee = 1.5

    // Dialog States
    private var _showAccountMaxValueDialog = MutableStateFlow(false)
    var showAccountMaxValueDialog: StateFlow<Boolean> = _showAccountMaxValueDialog

    fun setShowAccountMaxValueDialog(value: Boolean) {
        _showAccountMaxValueDialog.value = value
    }


    // DataStore -----------------------------------------------------------------------------

    private val dataStore = application.dataStore

    private val mockDataFlow = dataStore.data
        .map {
            it[DATASTORE_MOCKDATA] ?: false
        }

    val mockDataState: StateFlow<Boolean> = mockDataFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun setMockData(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_MOCKDATA] = value
            }
            Log.d("simDebug", "DataStore Mockdata value updated: $value")
        }
    }

    private val accountValueFlow = dataStore.data
        .map {
            it[DATASTORE_ACCOUNTVALUE] ?: 0.0
        }

    val accountValueState: StateFlow<Double> = accountValueFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0.0
        )

    fun setAccountValue(accountValue: Double) {
        val newAccountValue = accountValueState.value + accountValue
        if (accountValueState.value >= 0.0 && accountValueState.value < 500.0) {
            viewModelScope.launch {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = newAccountValue
                }
                Log.d("simDebug", "DataStore Account Credit updated")
            }
            _showAccountMaxValueDialog.value = false
        } else {
            _showAccountMaxValueDialog.value = true
        }
    }

    private val investedValueFlow = dataStore.data
        .map {
            it[DATASTORE_TOTALINVESTVALUE] ?: 0.0
        }

    val investedValueState: StateFlow<Double> = investedValueFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0.0
        )

    fun setInvestedValue(newInvestedValue: Double) {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_TOTALINVESTVALUE] = newInvestedValue
            }
            Log.d("simDebug", "DataStore Account Credit updated")
        }
    }


    // Pagination ------------------------------------------------------------------------------
    private var isLoading = false
    private var offset = 0
    private val limit = 5
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
                _refreshTimer.value = 90
                for (i in _refreshTimer.value downTo 1) {
                    _refreshTimer.value = i
                    delay(1000)
                }
                refreshCoins()
            }
        }
    }

    // API Response -------------------------------------------------------------------------------

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    private val _coinDetails: MutableStateFlow<Coin?> = MutableStateFlow(null)
    val coinDetails: StateFlow<Coin?> = _coinDetails

    fun loadMoreCoins() {
        if (isLoading || !hasMoreData) return

        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newCoins = repository.getCoins(offset = offset, limit = limit)
                _coinList.value =
                    _coinList.value + newCoins // nicht mit += da sich die Referenz nicht mitändert!
                offset = offset + limit
                hasMoreData = newCoins.isNotEmpty()
            } catch (e: Exception) {
                Log.e("SimBrokerViewModel", "Fehler beim Laden der Coins", e)
            } finally {
                isLoading = false

            }
        }
    }

    fun getCoinDetails(uuid: String, timePeriod: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coinDetails = repository.getCoin(uuid, timePeriod)
                _coinDetails.value = coinDetails
            } catch (e: Exception) {
                Log.e("SimBrokerViewModel", "Fehler beim Laden der CoinDetails", e)
            }
        }
    }

    private fun refreshCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val refreshedCoins = repository.getCoins(offset = 0, limit = limit)
                if (refreshedCoins.isNotEmpty()) {
                    _coinList.value = refreshedCoins
                    offset = refreshedCoins.size
                }
                Log.d("simDebug", "Coins aktualisiert: ${refreshedCoins.size}")
            } catch (e: Exception) {
                Log.e("simDebug", "Fehler beim Aktualisieren der Coins", e)
            }
        }
    }

    // Room Database -----------------------------------------------------------------------------

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


    val allPortfolioPositions: StateFlow<List<PortfolioPositions>> =
        repository.getAllPortfolioPositions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )

    val allTransactionPositions: StateFlow<List<TransactionPositions>> =
        repository.getAllTransactionPositions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )

}
