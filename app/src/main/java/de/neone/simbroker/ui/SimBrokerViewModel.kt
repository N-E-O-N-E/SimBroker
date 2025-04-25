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

// Preference Keys für DataStore
private val DATASTORE_FIRSTGAME = booleanPreferencesKey("firstGame")
private val DATASTORE_ACCOUNTVALUE = doublePreferencesKey("accountValue")
private val DATASTORE_TOTALINVESTVALUE = doublePreferencesKey("totalInvested")
private val DATASTORE_FEE = doublePreferencesKey("fee")
private val DATASTORE_GAMEDIFFICULTY = stringPreferencesKey("gameDifficulty")

/**
 * ViewModel für die SimBroker-App.
 *
 * Verwaltet:
 * 1) DataStore-Zustände (Konto, Schwierigkeit, Gebühren, erster Spielstart)
 * 2) Dialog-Status-Flows
 * 3) Timer zur Auffrischung der Coin-Daten
 * 4) API-Aufrufe und Repository-Interaktionen
 * 5) Portfolio- und Transaktions-Operationen (Kauf/Verkauf)
 *
 * @param application Android Application Context für DataStore-Zugriff.
 * @param repository Repository zur Abstraktion von Remote und lokaler DB.
 */
class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
) : AndroidViewModel(application) {

    //==============================================================================================
    // DataStore
    //==============================================================================================

    /** DataStore-Instanz für persistente Einstellungen. */
    private val dataStore = application.dataStore

    /** Toleranzgrenze für Double-Vergleiche nahe Null. */
    private val proofValue = 0.000001

    /**
     * Prüft, ob ein Double-Wert effektiv Null ist (< proofValue).
     *
     * @param value Zu prüfender Wert.
     * @return true, wenn |value| < proofValue.
     */
    private fun isEffectivelyZero(value: Double): Boolean = abs(value) < proofValue


    //==============================================================================================
    // Dialog States
    //==============================================================================================

    private var _showAccountMaxValueDialog = MutableStateFlow(false)
    val showAccountMaxValueDialog: StateFlow<Boolean> = _showAccountMaxValueDialog
    /**
     * Setzt den Sichtbarkeitsstatus für das Max-Wert-Dialog.
     *
     * @param value true = sichtbar, false = ausblenden.
     */
    fun setShowAccountMaxValueDialog(value: Boolean) {
        _showAccountMaxValueDialog.value = value
    }

    private var _showAccountNotEnoughMoney = MutableStateFlow(false)
    val showAccountNotEnoughMoney: StateFlow<Boolean> = _showAccountNotEnoughMoney
    /**
     * Setzt den Status für das „Nicht genug Guthaben“-Dialog.
     */
    fun setShowAccountNotEnoughMoney(value: Boolean) {
        _showAccountNotEnoughMoney.value = value
    }

    private var _showAccountNotEnoughCoins = MutableStateFlow(false)
    val showAccountNotEnoughCoins: StateFlow<Boolean> = _showAccountNotEnoughCoins
    /**
     * Setzt den Status für das „Nicht genug Coins“-Dialog.
     */
    fun setAccountNotEnoughCoins(value: Boolean) {
        _showAccountNotEnoughCoins.value = value
    }

    private var _showGameDifficultDialog = MutableStateFlow(false)
    val showGameDifficultDialog: StateFlow<Boolean> = _showGameDifficultDialog
    /** Setzt den Status für das Schwierigkeit-geändert-Dialog. */
    fun setShowGameDifficultDialog(value: Boolean) {
        _showGameDifficultDialog.value = value
    }

    private var _showGameWinDialog = MutableStateFlow(false)
    val showGameWinDialog: StateFlow<Boolean> = _showGameWinDialog
    /** Setzt den Status für das Spielgewinn-Dialog. */
    fun setShowGameWinDialog(value: Boolean) {
        _showGameWinDialog.value = value
    }

    private var _showFirstGameAccountValueDialog = MutableStateFlow(false)
    val showFirstGameAccountValueDialog: StateFlow<Boolean> = _showFirstGameAccountValueDialog
    /** Setzt den Status für das FirstGameAccountValue-Dialog. */
    fun setShowFirstGameAccountValueDialog(value: Boolean) {
        _showFirstGameAccountValueDialog.value = value
    }

    private var _showEraseDialog = MutableStateFlow(false)
    val showEraseDialog: StateFlow<Boolean> = _showEraseDialog
    /** Setzt den Status für das Reset-Dialog. */
    fun setShowEraseDialog(value: Boolean) {
        _showEraseDialog.value = value
    }

    private var _showAccountCashIn = MutableStateFlow(false)
    val showAccountCashIn: StateFlow<Boolean> = _showAccountCashIn
    /** Setzt den Status für das CashIn-Dialog. */
    fun setAccountCashIn(value: Boolean) {
        _showAccountCashIn.value = value
    }


    //==============================================================================================
    // Game Difficulty
    //==============================================================================================

    // Schwierigkeitsgrad: Easy / Medium / Pro / Custom
    private val gameDifficultFlow = dataStore.data
        .map { it[DATASTORE_GAMEDIFFICULTY] ?: "-" }

    /** Aktueller Schwierigkeitsgrad als StateFlow. */
    val gameDifficultState: StateFlow<String> = gameDifficultFlow
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = "-")

    /**
     * Speichert den Schwierigkeitsgrad im DataStore.
     *
     * @param value "Easy", "Medium", "Pro" oder "Custom"
     */
    fun setGameDifficult(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { it[DATASTORE_GAMEDIFFICULTY] = value }
        }
    }


    //==============================================================================================
    // Game Fee
    //==============================================================================================

    // Gebühr in EUR
    private val feeFlow = dataStore.data
        .map { it[DATASTORE_FEE] ?: 0.0 }

    /** Aktueller Gebührwert als StateFlow. */
    val feeValueState: StateFlow<Double> = feeFlow
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = 0.0)

    /**
     * Speichert den Gebührwert im DataStore.
     *
     * @param value Gebühr in EUR.
     */
    fun setFeeValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { it[DATASTORE_FEE] = value }
        }
    }


    //==============================================================================================
    // Firstgame
    //==============================================================================================

    // Erstes Spiel? (true = Schwierigkeit wählbar)
    private val firstGame = dataStore.data
        .map { it[DATASTORE_FIRSTGAME] ?: true }

    /** StateFlow, ob erstes Spiel. */
    val firstGameState: StateFlow<Boolean> = firstGame
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = true)

    /**
     * Speichert den Status, ob noch das erste Spiel ist.
     *
     * @param value true = erstes Spiel, false = danach.
     */
    fun setFirstGameState(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { it[DATASTORE_FIRSTGAME] = value }
        }
    }

    /**
     * Setzt den Kontostand beim ersten Spiel abweichend.
     *
     * @param value Startguthaben in EUR.
     */
    fun setFirstGameAccountValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            if (firstGameState.value) {
                dataStore.edit { it[DATASTORE_ACCOUNTVALUE] = value }
                Log.d("simDebug", "DataStore First Account Credit increased $value")
                _showFirstGameAccountValueDialog.value = false
            } else {
                _showFirstGameAccountValueDialog.value = true
            }
        }
    }


    //==============================================================================================
    // Account Value
    //==============================================================================================

    /** StateFlow des aktuellen Kontostands. */
    val accountValueState: StateFlow<Double> = dataStore.data
        .map { it[DATASTORE_ACCOUNTVALUE] ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    /** Setzt den Kontostand zurück auf 0. */
    fun resetAccountValue() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { it[DATASTORE_ACCOUNTVALUE] = 0.0 }
            Log.d("simDebug", "DataStore Account Credit reset done")
        }
    }

    /**
     * Erhöht den Kontostand manuell, wenn das Maximum nicht überschritten ist.
     *
     * @param value Betrag in EUR.
     */
    fun setAccountValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValue = accountValueState.value + investedValueState.value
            if (currentValue in 0.0..5900.0) {
                dataStore.edit { it[DATASTORE_ACCOUNTVALUE] = accountValueState.value + value }
                Log.d("simDebug", "DataStore Account Credit manual increased")
                _showAccountMaxValueDialog.value = false
            } else {
                _showAccountMaxValueDialog.value = true
            }
        }
    }

    /** Setzt den Kontostand am Spielende auf 10.000 €. */
    fun setGameEndAccountValue() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { it[DATASTORE_ACCOUNTVALUE] = 10000.0 }
            setShowGameWinDialog(true)
            Log.d("simDebug", "DataStore Account Credit increased to 10.000 €")
        }
    }

    /**
     * Intern: Aktualisiert den Kontostand um einen Wert und verhindert Negativwerte.
     *
     * @param value Delta in EUR (positiv/negativ).
     */
    private fun updateAccountValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValue = dataStore.data.first()[DATASTORE_ACCOUNTVALUE] ?: 0.0
            val newValue = currentValue.plus(value).coerceAtLeast(0.0)
            dataStore.edit { it[DATASTORE_ACCOUNTVALUE] = newValue }
            Log.d("simDebug", "DataStore Account Credit increased $newValue")
        }
    }


    //==============================================================================================
    // Invested Value
    //==============================================================================================

    /** StateFlow des insgesamt investierten Betrags. */
    val investedValueState: StateFlow<Double> = dataStore.data
        .map { it[DATASTORE_TOTALINVESTVALUE] ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    /** Setzt den investierten Wert zurück auf 0. */
    fun resetInvestedValue() {
        viewModelScope.launch {
            dataStore.edit { it[DATASTORE_TOTALINVESTVALUE] = 0.0 }
            Log.d("simDebug", "DataStore invested value reset. New Value: 0.0 €")
        }
    }

    /**
     * Intern: Erhöht den investierten Gesamtwert und verhindert Negativwerte.
     *
     * @param value Delta in EUR.
     */
    private fun setInvestedValue(value: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValue = dataStore.data.first()[DATASTORE_TOTALINVESTVALUE] ?: 0.0
            val newValue = (currentValue + value).coerceAtLeast(0.0)
            dataStore.edit { it[DATASTORE_TOTALINVESTVALUE] = newValue }
            Log.d("simDebug", "DataStore invested value increased. New Value: $newValue")
        }
    }


    //==============================================================================================
    // Timer
    //==============================================================================================

    /** Hintergrund-Job für den Countdown-Timer. */
    private var timerJob: Job? = null

    /** StateFlow für den 60 s-Refresh-Timer. */
    private val _refreshTimer = MutableStateFlow(0)
    val refreshTimer: StateFlow<Int> = _refreshTimer

    /**
     * Startet einen endlosen Countdown (60 s) zum periodischen Auffrischen.
     */
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


    //==============================================================================================
    // API Response
    //==============================================================================================

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    private val _coinDetails: MutableStateFlow<Coin?> = MutableStateFlow(null)
    val coinDetails: StateFlow<Coin?> = _coinDetails

    /**
     * Lädt Detaildaten eines Coins aus dem Repository.
     *
     * @param uuid UUID des Coins.
     * @param timePeriod Zeitraum für Sparkline.
     */
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

    /**
     * Frischt die Coin-Liste aus dem Repository auf.
     */
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


    //==============================================================================================
    // Room Database
    //==============================================================================================

    /** Löscht alle Transaktionen aus der lokalen DB. */
    fun deleteAllTransactions() {
        viewModelScope.launch {
            repository.deleteAllTransactions()
        }
    }

    /** Löscht alle Portfolio-Positionen aus der lokalen DB. */
    fun deleteAllPortfolioPositions() {
        viewModelScope.launch {
            repository.deleteAllPortfolioPositions()
        }
    }

    /** Intern: Fügt eine neue Transaktion hinzu. */
    private fun addTransaction(transaction: TransactionPositions) {
        Log.d("simDebug", "addTransaction over ViewModel started")
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    /** Intern: Fügt eine neue Portfolio-Position hinzu. */
    private fun addPortfolio(portfolio: PortfolioPositions) {
        Log.d("simDebug", "addPosition over ViewModel started")
        viewModelScope.launch {
            repository.insertPortfolio(portfolio)
        }
    }

    /** Intern: Aktualisiert eine Portfolio-Position. */
    private fun updatePortfolio(portfolio: PortfolioPositions) {
        viewModelScope.launch {
            repository.updatePortfolio(portfolio)
        }
    }


    //==============================================================================================
    // Kaufen und Verkaufen
    //==============================================================================================

    /**
     * Führt den Kauf eines Coins durch:
     * - Erhöht den investierten Gesamtwert.
     * - Verringert den Kontostand inkl. Gebühr.
     * - Legt Portfolio-Position und Transaktion an.
     *
     * @param selectedCoin Coin-Objekt.
     * @param amount Menge an Coins.
     * @param feeValue Gebühr in EUR.
     * @param totalValue Gesamtwert ohne Gebühr.
     */
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

    /**
     * Führt den Verkauf eines Coins nach FIFO durch:
     * - Bearbeitet offene BUY-Transaktionen.
     * - Aktualisiert Portfolio-Mengen und Kontostand.
     * - Zieht Gebühren ab und schließt Transaktionen.
     *
     * @param coinUuid UUID des zu verkaufenden Coins.
     * @param amountToSell Menge in Coins.
     * @param currentPrice Aktueller Preis pro Coin.
     * @param fee Verkaufsgebühr beim ersten Verkauf.
     */
    fun sellCoin(coinUuid: String, amountToSell: Double, currentPrice: Double, fee: Double) {
        viewModelScope.launch(Dispatchers.IO) {

            // Offene BUY-Transaktionen nach FIFO sortieren
            val openBuys = repository.getOpenBuyTransactionsByCoin(coinUuid)
                .filter { !it.isClosed }
                .sortedBy { it.timestamp }

            // Portfolio-Positionen laden und nach ID mappen
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

                // Berechne Verkaufsmengen und Werte
                val sellAmount = minOf(remainingToSell, availableAmount)
                val sellValue = sellAmount * currentPrice
                val appliedFee = if (isFirstSell) fee else 0.0

                // Transaktion hinzufügen
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

                // Portfolio aktualisieren
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

            // Aktualisiere Kontostand und investierten Wert im DataStore
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


    //==============================================================================================
    // Update Data
    //==============================================================================================

    /**
     * Aktualisiert den Favoriten-Status einer Portfolio-Position.
     *
     * @param coinId    UUID des Coins.
     * @param isFavorite true = Favorit, false = nicht Favorit.
     */
    fun updatePortfolio(coinId: String, isFavorite: Boolean) {
        Log.d("simDebug", "updatePosition over ViewModel started")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePortfolioFavorite(coinId = coinId, isFavorite = isFavorite)
        }
    }

    /** Intern: Markiert eine Transaktion als geschlossen. */
    private fun updateTransactionClosed(transactionId: Int) {
        Log.d("simDebug", "updateTransaction over ViewModel started")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransactionClosed(transactionId = transactionId, isClosed = true)
        }
    }

    /** Intern: Markiert eine Portfolio-Position als geschlossen. */
    private fun updatePortfolioClosed(portfolioId: Int) {
        Log.d("simDebug", "updatePortfolio over ViewModel started")
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePortfolioClosed(portfolioId = portfolioId, isClosed = true)
        }
    }


    //==============================================================================================
    // Get Data
    //==============================================================================================

    /** Flow aller Portfolio-Positionen (Live-Daten). */
    val allPortfolioPositions: StateFlow<List<PortfolioPositions>> =
        repository.getAllPortfolioPositions()
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())

    /** Flow aller Transaktionen (Live-Daten). */
    val allTransactionPositions: StateFlow<List<TransactionPositions>> =
        repository.getAllTransactionPositions()
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())

    /**
     * Berechnet die verbleibende Menge eines Coins im Portfolio.
     *
     * @param coinUuid UUID des Coins.
     * @return Summe aller amountRemaining.
     */
    fun getRemainingCoinAmount(coinUuid: String): Double {
        val positions = allPortfolioPositions.value
        return positions
            .filter { it.coinUuid == coinUuid && !isEffectivelyZero(it.amountRemaining) }
            .sumOf { it.amountRemaining }
    }


    //==============================================================================================
    // Init
    //==============================================================================================

    /**
     * Initialisierung:
     * - Startet den Countdown-Timer.
     * - Lädt initial die Coin-Liste.
     */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            startTimer()
            refreshCoins()
        }
    }
}
