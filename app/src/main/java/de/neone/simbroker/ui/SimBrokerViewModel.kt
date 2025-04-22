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

private val DATASTORE_FIRSTGAME = booleanPreferencesKey("firstGame")
private val DATASTORE_ACCOUNTVALUE = doublePreferencesKey("accountValue")
private val DATASTORE_TOTALINVESTVALUE = doublePreferencesKey("totalInvested")
private val DATASTORE_FEE = doublePreferencesKey("fee")
private val DATASTORE_GAMEDIFFICULTY = stringPreferencesKey("gameDifficulty")

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,

    ) : AndroidViewModel(application) {

    // DataStore -----------------------------------------------------------------------------
    private val dataStore = application.dataStore

    // Proof Value for Sell-------------------------------------------------------------------------
    private val proofValue = 0.000001
    private fun isEffectivelyZero(value: Double): Boolean = abs(value) < proofValue


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
            val currentValue = accountValueState.value
            if (currentValue in 0.0..450.0) {
                dataStore.edit {
                    it[DATASTORE_ACCOUNTVALUE] = accountValueState.value + value
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
                _refreshTimer.value = 60
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

    fun refreshCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val refreshedCoins = repository.getCoins()
                if (refreshedCoins.isNotEmpty()) {
                    _coinList.value = refreshedCoins
                }
                Log.d("simDebug", "Coins refreshed: ${refreshedCoins.size}")
            } catch (e: Exception) {
                Log.e("simDebug", "Error refreshing coins", e)
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

    private fun updatePortfolio(portfolio: PortfolioPositions) {
        viewModelScope.launch {
            repository.updatePortfolio(portfolio)
        }
    }

    // Kaufen und Verkaufen -----------------------------------------------------------------------------
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

            delay(500)

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

    fun sellCoin(coinUuid: String, amountToSell: Double, currentPrice: Double, fee: Double) {
        viewModelScope.launch(Dispatchers.IO) {

            // Offene BUY-Transaktionen nach FIFO sortieren
            val openBuys = repository.getOpenBuyTransactionsByCoin(coinUuid)
                .filter { !it.isClosed }
                .sortedBy { it.timestamp }

            // Portfolio-Positionen
            val portfolioMap = repository.getAllPortfolioPositions()
                .first()
                .filter { it.coinUuid == coinUuid && !isEffectivelyZero(it.amountRemaining) }
                .associateBy { it.id }

            var remainingToSell = amountToSell
            var isFirstSell = true
            var totalCashIn = 0.0
            var totalInvestReduction = 0.0

            for (buy in openBuys) {
                if (isEffectivelyZero(remainingToSell)) break

                val portfolio = portfolioMap[buy.portfolioCoinID] ?: continue
                val availableAmount = portfolio.amountRemaining
                if (isEffectivelyZero(availableAmount)) continue

                // Verkaufe so viel wie noch da ist
                val sellAmount = minOf(remainingToSell, availableAmount)
                val sellValue = sellAmount * currentPrice
                val appliedFee = if (isFirstSell) fee else 0.0


                addTransaction(
                    TransactionPositions(
                        coinUuid = buy.coinUuid,
                        symbol = buy.symbol,
                        iconUrl = buy.iconUrl,
                        name = buy.name,
                        price = currentPrice,
                        amount = sellAmount,
                        fee = appliedFee,
                        type = TransactionType.SELL,
                        totalValue = sellValue,
                        portfolioCoinID = buy.portfolioCoinID
                    )
                )


                val newRemaining = portfolio.amountRemaining - sellAmount
                val correctedRemaining = if (isEffectivelyZero(newRemaining)) 0.0 else newRemaining
                val newTotalValue = correctedRemaining * portfolio.pricePerUnit

                totalInvestReduction += sellAmount * portfolio.pricePerUnit
                totalCashIn += sellValue
                remainingToSell -= sellAmount
                isFirstSell = false

                updatePortfolio(
                    portfolio.copy(
                        amountRemaining = correctedRemaining,
                        totalValue = newTotalValue,
                        isClosed = isEffectivelyZero(correctedRemaining)
                    )
                )

                if (isEffectivelyZero(correctedRemaining)) {
                    updatePortfolioClosed(portfolio.id)
                    updateTransactionClosed(buy.id)
                }
            }

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
            .filter { it.coinUuid == coinUuid && !isEffectivelyZero(it.amountRemaining) }
            .sumOf { it.amountRemaining }
    }


    // Init -----------------------------------------------------------------------------
    init {
        viewModelScope.launch(Dispatchers.IO) {
            startTimer()
            refreshCoins()
        }
    }

}