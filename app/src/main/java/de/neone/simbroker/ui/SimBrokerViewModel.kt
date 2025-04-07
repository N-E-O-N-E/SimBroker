package de.neone.simbroker.ui

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.models.PortfolioPositions
import de.neone.simbroker.data.local.models.TransactionPositions
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val DATASTORE_MOCKDATA = booleanPreferencesKey("mockData")
private val DATASTORE_FIRSTGAME = booleanPreferencesKey("firstGame")
private val DATASTORE_ACCOUNTVALUE = doublePreferencesKey("accountValue")
private val DATASTORE_TOTALINVESTVALUE = doublePreferencesKey("totalInvested")
private val DATASTORE_FEE = doublePreferencesKey("fee")
private val DATASTORE_GAMEDIFFICULTY = stringPreferencesKey("gameDifficulty")

class SimBrokerViewModel(
    application: Application,
    private val realRepo: SimBrokerRepositoryInterface,
    private val mockRepo: SimBrokerRepositoryInterface,

    ) : AndroidViewModel(application) {

    private val repository: SimBrokerRepositoryInterface
        get() = if (mockDataState.value) realRepo else mockRepo


    // Dialog States -------------------------------------------------------------------------------


    private var _showAccountMaxValueDialog = MutableStateFlow(false)
    var showAccountMaxValueDialog: StateFlow<Boolean> = _showAccountMaxValueDialog

    fun setShowAccountMaxValueDialog(value: Boolean) {
        _showAccountMaxValueDialog.value = value
    }

    private var _showAccountNotEnoughMoney = MutableStateFlow(false)
    var showAccountNotEnoughMoney: StateFlow<Boolean> = _showAccountNotEnoughMoney

    fun setShowAccountNotEnoughMoney(value: Boolean) {
        _showAccountNotEnoughMoney.value = value
    }

    private var _showGameDifficultDialog = MutableStateFlow(false)
    var showGameDifficultDialog: StateFlow<Boolean> = _showGameDifficultDialog

    fun setShowGameDifficultDialog(value: Boolean) {
        _showGameDifficultDialog.value = value
    }

    private var _showMockOrRealdataDialog = MutableStateFlow(false)
    var showMockOrRealdataDialog: StateFlow<Boolean> = _showMockOrRealdataDialog

    fun setShowMockOrRealdataDialog(value: Boolean) {
        _showMockOrRealdataDialog.value = value
    }

    private var _showFirstGameAccountValueDialog = MutableStateFlow(false)
    var showFirstGameAccountValueDialog: StateFlow<Boolean> = _showFirstGameAccountValueDialog

    fun setShowFirstGameAccountValueDialog(value: Boolean) {
        _showFirstGameAccountValueDialog.value = value
    }

    private var _showEraseDialog = MutableStateFlow(false)
    var showEraseDialog: StateFlow<Boolean> = _showEraseDialog

    fun setShowEraseDialog(value: Boolean) {
        _showEraseDialog.value = value
    }


    // DataStore -----------------------------------------------------------------------------


    private val dataStore = application.dataStore

    // Mockdata -----------------------------------------------------------------------------

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

    // Game Difficulty -----------------------------------------------------------------------------

    private val gameDifficultFlow = dataStore.data
        .map {
            it[DATASTORE_GAMEDIFFICULTY] ?: "-"
        }

    val gameDifficultState: StateFlow<String> = gameDifficultFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "-"
        )

    fun setGameDifficult(value: String) {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_GAMEDIFFICULTY] = value
            }
        }
    }

    // Game Fee -----------------------------------------------------------------------------

    private val feeFlow = dataStore.data
        .map {
            it[DATASTORE_FEE] ?: 0.0
        }

    val feeValueState: StateFlow<Double> = feeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0.0
        )

    fun setFeeValue(value: Double) {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_FEE] = value
            }
        }
    }

    // Firstgame -----------------------------------------------------------------------------

    private val firstGame = dataStore.data
        .map {
            it[DATASTORE_FIRSTGAME] ?: true
        }

    val firstGameState: StateFlow<Boolean> = firstGame
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = true
        )

    fun setFirstGameState(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_FIRSTGAME] = value
            }
        }
    }

    fun setFirstGameAccountValue(value: Double) {
        if (firstGameState.value) {
            val newValue = accountValueState.value + value
            viewModelScope.launch {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = newValue
                }
                Log.d("simDebug", "DataStore First Account Credit increased")
            }
            _showFirstGameAccountValueDialog.value = false
        } else {
            _showFirstGameAccountValueDialog.value = true
        }
    }

    // Account Value -----------------------------------------------------------------------------

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

    fun resetAccountValue() {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_ACCOUNTVALUE] = 0.0
            }
            Log.d("simDebug", "DataStore Account Credit reset done")
        }
    }

    fun setAccountValue(value: Double) {
        if (accountValueState.value in 0.0..450.0) {
            val newValue = accountValueState.value + value
            viewModelScope.launch {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = newValue
                }
                Log.d("simDebug", "DataStore Account Credit increased")
            }
            _showAccountMaxValueDialog.value = false
        } else {
            _showAccountMaxValueDialog.value = true
        }
    }

    fun reduceAccountValue(value: Double) {
        val newAccountValue = accountValueState.value - value
        if (accountValueState.value >= value) {
            viewModelScope.launch {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = newAccountValue
                }
                setInvestedValue(value)
                _showAccountNotEnoughMoney.value = false
                Log.d(
                    "simDebug",
                    "DataStore Account Credit reduce. Current Credit: ${accountValueState.value}. InputValue: $value. Save Value: $newAccountValue"
                )
            }
        } else {
            _showAccountNotEnoughMoney.value = true
        }
    }

    // Invested Value -----------------------------------------------------------------------------


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

    fun resetInvestedValue() {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_TOTALINVESTVALUE] = 0.0
            }
            Log.d("simDebug", "DataStore invested value increased. New Value: 0.0 €")
        }
    }

    private fun setInvestedValue(value: Double) {
        val newValue = investedValueState.value + value
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_TOTALINVESTVALUE] = newValue
            }
            Log.d("simDebug", "DataStore invested value increased. New Value: $newValue")
        }

    }

    private fun reduceInvestedValue(value: Double) {
        val newValue = investedValueState.value - value
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_TOTALINVESTVALUE] = newValue
            }
            Log.d("simDebug", "DataStore invested value reduce. New Value: $newValue")
        }
    }


    // Timer -----------------------------------------------------------------------

    private var timerJob: Job? = null

    private val _refreshTimer = MutableStateFlow(0)
    val refreshTimer: StateFlow<Int> = _refreshTimer

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
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


    // Pagination ---------------------------------------------------------------------------------

    private var isLoading = false
    private var offset = 0
    private val limit = 5
    private var hasMoreData = true

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


    // API Response -------------------------------------------------------------------------------

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    private val _coinDetails: MutableStateFlow<Coin?> = MutableStateFlow(null)
    val coinDetails: StateFlow<Coin?> = _coinDetails

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

    // Delete all Data -----------------------------------------------------------------------------
    fun deleteAllTransactions() {
        viewModelScope.launch {
            repository.deleteAllTransactions()
        }
    }

    fun deleteAllPortfolioPositions() {
        viewModelScope.launch {
            repository.deleteAllPortfolioPositions()
        }
    }

    // Insert Data -----------------------------------------------------------------------------

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

    // Update Data -----------------------------------------------------------------------------

    fun updatePortfolio(coinId: String, isFavorite: Boolean) {
        Log.d("simDebug", "updatePosition over ViewModel started")
        viewModelScope.launch {
            repository.updatePortfolioFavorite(
                coinId = coinId,
                isFavorite = isFavorite
            )
        }
    }

    // Get Data -----------------------------------------------------------------------------

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

    fun getAllOpenBuyTransactionsByCoin(coinUuid: String): MutableList<TransactionPositions> {
        var result = mutableListOf<TransactionPositions>()

        viewModelScope.launch(Dispatchers.IO) {
            result = repository.getOpenBuyTransactionsByCoin(coinUuid).toMutableList()
        }
        return result
    }



// Init -----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            mockDataState.collect {
                Log.d("simDebug", "DataStore Mockdata value: $it")
                startTimer()
                refreshCoins()
                loadMoreCoins()
            }
        }
    }
}
