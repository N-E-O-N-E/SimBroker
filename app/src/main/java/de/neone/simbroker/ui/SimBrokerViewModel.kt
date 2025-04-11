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
import de.neone.simbroker.data.local.models.TransactionType
import de.neone.simbroker.data.remote.models.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import de.neone.simbroker.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * ViewModel für die SimBroker Krypto-Trading-App.
 *
 * Steuert zentrale Spiellogik, hält UI-State über StateFlows bereit und koordiniert:
 * - Account- und Investitionswerte via DataStore
 * - Transaktionen und Portfolio über Repository
 * - API-Aufrufe für Coins und deren Details
 * - Benutzerinteraktionen wie Kaufen/Verkaufen/Favorisieren
 * - Dialogzustände (Alerts, Hinweise)
 * - Timer für automatische Preisupdates
 *
 * Die Repository-Auswahl (Mock vs. Echt) wird zur Laufzeit via DataStore geregelt.
 *
 * @property realRepo Echtzeit-Repository (z.B. mit Retrofit)
 * @property mockRepo Offline-/Mock-Repository für Testdaten
 */

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

    // Zugriff auf aktives Repository basierend auf DataStore-Flag
    private val repository: SimBrokerRepositoryInterface
        get() = if (mockDataState.value) {
            realRepo
        } else {
            mockRepo
        }


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

    private var _showAccountNotEnoughCoins = MutableStateFlow(false)
    var showAccountNotEnoughCoins: StateFlow<Boolean> = _showAccountNotEnoughCoins

    fun setAccountNotEnoughCoins(value: Boolean) {
        _showAccountNotEnoughCoins.value = value
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

    private var _showAccountCashIn = MutableStateFlow(false)
    var showAccountCashIn: StateFlow<Boolean> = _showAccountCashIn

    fun setAccountCashIn(value: Boolean) {
        _showAccountCashIn.value = value
    }


    // DataStore -----------------------------------------------------------------------------
    private val dataStore = application.dataStore

    // Mockdata -----------------------------------------------------------------------------
    // MOCKDATEN AKTIV (true/false)
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
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit {
                it[DATASTORE_MOCKDATA] = value
            }
            Log.d("simDebug", "DataStore Mockdata value updated: $value")
        }
    }

    // Game Difficulty -----------------------------------------------------------------------------
    // SCHWIERIGKEIT: Easy / Medium / Pro / Custom
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
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit {
                it[DATASTORE_GAMEDIFFICULTY] = value
            }
        }
    }

    // Game Fee -----------------------------------------------------------------------------
    // FEE-WERT in EUR
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
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit {
                it[DATASTORE_FEE] = value
            }
        }
    }

    // Firstgame -----------------------------------------------------------------------------
    // ERSTES SPIEL? (true wenn Schwierigkeitswahl erlaubt)
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
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit {
                it[DATASTORE_FIRSTGAME] = value
            }
        }
    }

    // SETZE KONTOSTAND
    fun setFirstGameAccountValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            if (firstGameState.value) {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = value
                }
                Log.d("simDebug", "DataStore First Account Credit increased $value")

                _showFirstGameAccountValueDialog.value = false
            } else {
                _showFirstGameAccountValueDialog.value = true
            }
        }
    }

    // Account Value -----------------------------------------------------------------------------
    // KONTOSTAND
    val accountValueState: StateFlow<Double> = dataStore.data
        .map { it[DATASTORE_ACCOUNTVALUE] ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    fun resetAccountValue() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit {
                it[DATASTORE_ACCOUNTVALUE] = 0.0
            }

            Log.d("simDebug", "DataStore Account Credit reset done")
        }
    }

    fun setAccountValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValue = dataStore.data.first()[DATASTORE_ACCOUNTVALUE] ?: 0.0
            val newValue = currentValue.plus(value).coerceAtLeast(0.0)
            if (currentValue in 0.0..450.0) {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = newValue
                }
                Log.d("simDebug", "DataStore Account Credit manual increased")

                _showAccountMaxValueDialog.value = false
            } else {
                _showAccountMaxValueDialog.value = true
            }
        }
    }

    private fun updateAccountValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValue = dataStore.data.first()[DATASTORE_ACCOUNTVALUE] ?: 0.0
            val newValue = currentValue.plus(value).coerceAtLeast(0.0)
            dataStore.edit {
                it[DATASTORE_ACCOUNTVALUE] = newValue
            }
            Log.d("simDebug", "DataStore Account Credit increased $newValue")
        }
    }


    // Invested Value -----------------------------------------------------------------------------
    // GESAMT INVESTIERT
    val investedValueState: StateFlow<Double> = dataStore.data
        .map { it[DATASTORE_TOTALINVESTVALUE] ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)


    fun resetInvestedValue() {
        viewModelScope.launch {
            dataStore.edit {
                it[DATASTORE_TOTALINVESTVALUE] = 0.0
            }
            Log.d("simDebug", "DataStore invested value reset. New Value: 0.0 €")
        }
    }

    private fun setInvestedValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValue = dataStore.data.first()[DATASTORE_TOTALINVESTVALUE] ?: 0.0
            val newValue = (currentValue + value).coerceAtLeast(0.0)

            dataStore.edit {
                it[DATASTORE_TOTALINVESTVALUE] = newValue
            }

            Log.d("simDebug", "DataStore invested value increased. New Value: $newValue")
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
    private val limit = 100
    private var hasMoreData = true

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
                Log.e("simBroker", "Error loading more coins", e)
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
                Log.e("SimBrokerViewModel", "Error loading coin details", e)
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
                Log.d("simDebug", "Coins refreshed: ${refreshedCoins.size}")
            } catch (e: Exception) {
                Log.e("simDebug", "Error refreshing coins", e)
            }
        }
    }

    fun loadAllPortfolioCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            val portfolioUuids = allPortfolioPositions.value.map { it.coinUuid }.distinct()
            val existingUuids = _coinList.value.map { it.uuid }

            val missingUuids = portfolioUuids.filter { it !in existingUuids }

            val loadedPortfolioCoins = missingUuids.mapNotNull { uuid ->
                try {
                    repository.getCoin(uuid, timePeriod = "3h")
                } catch (e: Exception) {
                    Log.e("simDebug", "Error loading portfolio coin $uuid", e)
                    null
                }
            }
            _coinList.value = (_coinList.value + loadedPortfolioCoins).distinctBy { it.uuid }
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

    private fun addTransaction(transaction: TransactionPositions) {
        Log.d("simDebug", "addTransaction over ViewModel started")
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    private fun addPortfolio(portfolio: PortfolioPositions) {
        Log.d("simDebug", "addPosition over ViewModel started")
        viewModelScope.launch {
            repository.insertPortfolio(portfolio)
        }
    }

    private fun deletePortfolioById(entryId: Int) {
        Log.d("simDebug", "deletePosition over ViewModel started")
        viewModelScope.launch {
            repository.deletePortfolioById(entryId)
        }
    }

    fun buyCoin(selectedCoin: Coin, amount: Double, feeValue: Double, totalValue: Double) {
        viewModelScope.launch(Dispatchers.IO) {

            setInvestedValue(totalValue)
            updateAccountValue(-totalValue - feeValue)

            Log.d("simDebug", "setInvestedValue: $totalValue")
            Log.d("simDebug", "setAccountValue: ${(-totalValue - feeValue)}")


            addPortfolio(
                PortfolioPositions(
                    coinUuid = selectedCoin.uuid,
                    symbol = selectedCoin.symbol,
                    iconUrl = selectedCoin.iconUrl,
                    name = selectedCoin.name,
                    amountBought = amount,
                    amountRemaining = amount,
                    pricePerUnit = selectedCoin.price.toDouble(),
                    totalValue = totalValue
                )
            )

            delay(1000)

            addTransaction(
                TransactionPositions(
                    fee = feeValue,
                    coinUuid = selectedCoin.uuid,
                    symbol = selectedCoin.symbol,
                    iconUrl = selectedCoin.iconUrl,
                    name = selectedCoin.name,
                    price = selectedCoin.price.toDouble(),
                    amount = amount,
                    type = TransactionType.BUY,
                    totalValue = totalValue,
                    portfolioCoinID = allPortfolioPositions.value.last().id
                )
            )
        }
    }

    // Update Data -----------------------------------------------------------------------------

    fun updatePortfolio(coinId: String, isFavorite: Boolean) {
        Log.d("simDebug", "updatePosition over ViewModel started")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePortfolioFavorite(
                coinId = coinId,
                isFavorite = isFavorite
            )
        }
    }

    private fun updateTransactionClosed(transactionId: Int) {
        Log.d("simDebug", "updateTransaction over ViewModel started")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransactionClosed(
                transactionId = transactionId,
                isClosed = true
            )
        }
    }

    private fun updatePortfolioClosed(portfolioId: Int) {
        Log.d("simDebug", "updatePortfolio over ViewModel started")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePortfolioClosed(
                portfolioId = portfolioId,
                isClosed = true
            )
        }
    }



    // Kaufen und Verkaufen -----------------------------------------------------------------------------

    fun sellCoin(coinUuid: String, amountToSell: Double, currentPrice: Double, fee: Double) {
        viewModelScope.launch(Dispatchers.IO) {

            // Alle offenen Kauf-Transaktionen
            val openBuysRaw = repository.getOpenBuyTransactionsByCoin(coinUuid)
                .filter { !it.isClosed }

            // Alle Portfolio-Positionen zum Coin > 0
            val portfolioPositions = repository.getAllPortfolioPositions().first()
                .filter { it.coinUuid == coinUuid && it.amountRemaining > 0.0000001 }
                .associateBy { it.id } // zum späteren schnellen Zugriff via portfolioCoinID

            // Offene Käufe, die tatsächlich noch Restmenge haben
            val openBuys = openBuysRaw.filter {
                (portfolioPositions[it.portfolioCoinID]?.amountRemaining ?: 0.0) > 0.0000001
            }.sortedBy { it.timestamp }

            var remainingToSell = amountToSell
            var isFirstSell = true
            var totalCashIn = 0.0
            var totalInvestReduction = 0.0

            // Passende SELL-Transaktionen aus offenen Käufen
            for (buy in openBuys) {
                Log.d("simDebug", "openBuy: $remainingToSell")
                if (remainingToSell <= 0.0000001) break

                val sellAmount = minOf(remainingToSell, buy.amount) // wie viel vom Kauf verwendet wird
                val usedFee = if (isFirstSell) fee else 0.0
                val value = sellAmount * currentPrice

                totalCashIn += value
                remainingToSell -= sellAmount
                isFirstSell = false

                addTransaction(
                    TransactionPositions(
                        coinUuid = buy.coinUuid,
                        symbol = buy.symbol,
                        iconUrl = buy.iconUrl,
                        name = buy.name,
                        price = currentPrice,
                        amount = sellAmount,
                        fee = usedFee,
                        type = TransactionType.SELL,
                        totalValue = value,
                        portfolioCoinID = buy.portfolioCoinID
                    )
                )

                if (abs(sellAmount - buy.amount) < 0.0000001) {
                    updateTransactionClosed(buy.id)
                }
            }

            // Portfolio-Positionen nochmal zum Reduzieren der Restmenge
            val portfolioEntries = repository.getAllPortfolioPositions().first()
                .filter { it.coinUuid == coinUuid && it.amountRemaining > 0 }
                .sortedBy { it.timestamp }

            var localRemaining = amountToSell

            for (entry in portfolioEntries) {
                if (localRemaining <= 0.0000001) break

                val reduceAmount = minOf(localRemaining, entry.amountRemaining)
                val newRemaining = entry.amountRemaining - reduceAmount
                val valueReduction = reduceAmount * entry.pricePerUnit

                totalInvestReduction += valueReduction

                if (newRemaining <= 0.0000001) {
//                    deletePortfolioById(entry.id)
//                    // Position leer dann löschen
                    Log.d("simDebug", "portfolioPosition ${entry.id} set isClosed over ViewModel started")
                    updatePortfolioClosed(entry.id)

                } else {
                    // Restmenge aktualisieren
                    addPortfolio(
                        entry.copy(
                            amountRemaining = newRemaining,
                            totalValue = newRemaining * entry.pricePerUnit
                        )
                    )
                }

                localRemaining -= reduceAmount
            }

            // Account- und Investitionswerte im DataStore updaten
            val currentAccount = dataStore.data.first()[DATASTORE_ACCOUNTVALUE] ?: 0.0
            val currentInvested = dataStore.data.first()[DATASTORE_TOTALINVESTVALUE] ?: 0.0

            val updatedAccount = (currentAccount + totalCashIn - fee).coerceAtLeast(0.0)
            val updatedInvested = (currentInvested - totalInvestReduction).coerceAtLeast(0.0)

            dataStore.edit {
                it[DATASTORE_ACCOUNTVALUE] = updatedAccount
                it[DATASTORE_TOTALINVESTVALUE] = updatedInvested
            }
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


    fun getRemainingCoinAmount(coinUuid: String): Double {
        val positions = allPortfolioPositions.value
        return positions
            .filter { it.coinUuid == coinUuid }
            .sumOf { it.amountRemaining }
    }


// Init -----------------------------------------------------------------------------

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mockDataState.collect {
                Log.d("simDebug", "DataStore Mockdata value: $it")
                startTimer()
                refreshCoins()
                loadMoreCoins()
                loadAllPortfolioCoins()
            }
        }
    }
}
