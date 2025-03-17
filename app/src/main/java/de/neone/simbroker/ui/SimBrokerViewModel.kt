package de.neone.simbroker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.SimBrokerRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SimBrokerViewModel(
    application: Application,
    private val repository: SimBrokerRepositoryInterface,
    ) : AndroidViewModel(application) {

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    suspend fun fetchCoins() {
        try {
            val coins = repository.getCoins()
            _coinList.value = coins

        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun fetchCoin(uuid: String) {
        try {
            val coin = repository.getCoin(uuid)
            _coinList.value = listOf(coin)

        } catch (e: Exception) {
            // Handle error
        }
    }



}