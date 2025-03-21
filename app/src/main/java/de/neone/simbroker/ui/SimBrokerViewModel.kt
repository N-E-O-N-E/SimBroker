package de.neone.simbroker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.neone.simbroker.data.local.CoinEntityDto
import de.neone.simbroker.data.local.PortfolioData
import de.neone.simbroker.data.local.SimBrokerDatabase
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.local.SparklineEntityDto
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
) : AndroidViewModel(application) {

    // Timer Steuerung
    private var timerStartet = false

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
            if (!timerStartet) {
                startTimer()
                timerStartet = true
            }
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


    // Room
    private val simBrokerDatabase =
        SimBrokerDatabase.getDatabase(application.applicationContext).portfolioDao()

    private val _coinsListData = MutableStateFlow<List<PortfolioData>>(emptyList())
    val coinsListData: StateFlow<List<PortfolioData>> = _coinsListData
    private val _sparklineData = MutableStateFlow<List<SparklineDataEntity>>(emptyList())
    val sparklineData: StateFlow<List<SparklineDataEntity>> = _sparklineData

    private val _coinsListDto = MutableStateFlow<List<CoinEntityDto>>(emptyList())
    val coinsListDto: StateFlow<List<CoinEntityDto>> = _coinsListDto
    private val _sparklineListDto = MutableStateFlow<List<SparklineEntityDto>>(emptyList())
    val sparklineListDto: StateFlow<List<SparklineEntityDto>> = _sparklineListDto

    // Room - PortfolioDATA
    fun insertPortfolioData(portfolioData: PortfolioData) {
        viewModelScope.launch {
            simBrokerDatabase.insertPortfolioData(portfolioData)
        }
    }

    fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity) {
        viewModelScope.launch {
            simBrokerDatabase.insertSparklineDataEntity(sparklineDataEntity)
        }
    }

    fun getAllPortfolioData() {
        viewModelScope.launch {
            val portfolioData = simBrokerDatabase.getAllPortfolioData()
            _coinsListData.value = portfolioData
        }
    }

    fun getCoinSparklines(coinUuid: String) {
        viewModelScope.launch {
            val sparklineData = simBrokerDatabase.getCoinSparklines(coinUuid)
            _sparklineData.value = sparklineData
        }
    }

    // Room - CoinEntityDto
    fun insertCoinEntityDto(coinEntityDto: CoinEntityDto) {
        viewModelScope.launch {
            simBrokerDatabase.insertCoinEntityDto(coinEntityDto)
        }
    }

    fun insertSparklineEntityDto(sparklineEntityDto: SparklineEntityDto) {
        viewModelScope.launch {
            simBrokerDatabase.insertSparklineEntityDto(sparklineEntityDto)
        }
    }

    fun getAllCoinsDto() {
        viewModelScope.launch {
            val coinsDto = simBrokerDatabase.getAllCoinsDto()
            _coinsListDto.value = coinsDto
        }
    }

    fun getCoinSparklinesDto() {
        viewModelScope.launch {
            val sparklinesDto = simBrokerDatabase.getCoinSparklinesDto()
            _sparklineListDto.value = sparklinesDto
        }
    }


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
        viewModelScope.launch {
            try {
                val refreshedCoins = repository.getCoins(offset = 0, limit = _coinList.value.size)
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
