package de.neone.simbroker.data.repository

import android.util.Log
import de.neone.simbroker.data.local.ClosedTrade
import de.neone.simbroker.data.local.PortfolioPosition
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.local.Transaction
import de.neone.simbroker.data.local.TransactionType
import de.neone.simbroker.data.remote.APIService
import de.neone.simbroker.data.remote.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SimBrokerRepositoryImpl(
    private val apiService: APIService,
    private val simBrokerDAO: SimBrokerDAO
) : SimBrokerRepositoryInterface {

    // API
    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return try {
            apiService.getCoins(limit = limit, offset = offset).data.coins
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API Coins", e)
            emptyList()
        }
    }

    override suspend fun getCoinPrice(uuid: String): Double {
        return try {
            apiService.getCoinPrice(uuid)
        } catch (e: Exception) {
            Log.e("SimBrokerRepository", "Fehler beim Laden der API CoinPrice", e)
            0.0
        }
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        return when(timePeriod) {
            "1h" -> apiService.getCoin1h(uuid).data.coin
            "3h" -> apiService.getCoin3h(uuid).data.coin
            "24h" -> apiService.getCoin24h(uuid).data.coin
            "7d" -> apiService.getCoin7d(uuid).data.coin
            "30d" -> apiService.getCoin30d(uuid).data.coin
            "3m" -> apiService.getCoin3m(uuid).data.coin
            else -> {
                apiService.getCoin1h(uuid).data.coin
            }
        }
    }

    // ROOM - PortfolioDATA

    override suspend fun insertTransaction(transaction: Transaction) {
        simBrokerDAO.insertTransaction(transaction)
    }
    override suspend fun getTransactionByCoinUuid(coinUuid: String): List<Transaction> {
        return simBrokerDAO.getTransactionByCoinUuid(coinUuid)
    }
    override suspend fun updateTransaction(transactionId: Int, isClosed: Boolean) {
        simBrokerDAO.updateTransaction(transactionId, isClosed)
    }

    override suspend fun insertOrUpdatePortfolioPosition(portfolioPosition: PortfolioPosition) {
        simBrokerDAO.insertOrUpdatePortfolioPosition(portfolioPosition)
    }
    override suspend fun deletePortfolioPosition(portfolio: PortfolioPosition) {
        simBrokerDAO.deletePortfolioPosition(portfolio)
    }
    override suspend fun insertClosedTrade(closedTrade: ClosedTrade) {
        simBrokerDAO.insertClosedTrade(closedTrade)
    }

    override suspend fun insertSparklineDataEntity(sparklineDataEntity: SparklineDataEntity) {
        simBrokerDAO.insertSparklineDataEntity(sparklineDataEntity)
    }



    override fun getPortfolioPositionByCoinUuid(coinUuid: String): Flow<PortfolioPosition?> {
        return simBrokerDAO.getPortfolioPositionByCoinUuid(coinUuid)
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return simBrokerDAO.getAllTransactions()
    }

    override fun getAllPortfolioPositions(): Flow<List<PortfolioPosition>> {
        return simBrokerDAO.getAllPortfolioPositions()
    }

    override fun getAllClosedTrades(): Flow<List<ClosedTrade>> {
        return simBrokerDAO.getAllClosedTrades()
    }

    override fun getSparklineDataByCoinUuid(coinUuid: String): Flow<List<SparklineDataEntity>> {
        return simBrokerDAO.getSparklineDataByCoinUuid(coinUuid)
    }





    suspend fun buyCoin(coin: Coin, amount: Double, price: Double) {
        // Kaufen
        val transaction = Transaction(
            coinUuid = coin.uuid,
            name = coin.name,
            symbol = coin.symbol,
            iconUrl = coin.iconUrl,
            type = TransactionType.BUY,
            amount = amount,
            price = price
        )
        insertTransaction(transaction)
        updatePortfolioPosition(coin.uuid)
    }

    suspend fun sellCoin(coinUuid: String, amount: Double, price: Double) {
        // Verkaufen
        // Prüfe die Bestände

        val position = getPortfolioPositionByCoinUuid(coinUuid).first()
            ?: throw Exception("Keine Positionen vorhanden")

        if (position.totalAmount < amount) {
            throw Exception("Nicht genügend Coins oder PortfolioPositionen zum Verkaufen")
        }

        // Speichern der Transaktion
        val transaction = Transaction(
            coinUuid = position.coinUuid,
            name = position.name,
            symbol = position.symbol,
            iconUrl = position.iconUrl,
            type = TransactionType.SELL,
            amount = amount,
            price = price
        )
        insertTransaction(transaction)
        updatePortfolioPosition(coinUuid)

        // wenn verkauft, dann schließen
        if (position.totalAmount - amount <= 0 ) {
            closeTrade(coinUuid)
        }

    }

    private suspend fun updatePortfolioPosition(coinUuid: String) {
        // Portfolio aktualisieren
        val sparks = getSparklineDataByCoinUuid(coinUuid)
        val transaction = getTransactionByCoinUuid(coinUuid)
        val buys = transaction.filter { it.type == TransactionType.BUY }
        val sells = transaction.filter { it.type == TransactionType.SELL }

        val totalBuyAmount = buys.sumOf { it.amount }
        val totalSellAmount = sells.sumOf { it.amount }
        val remainingAmount = totalBuyAmount - totalSellAmount

        if (remainingAmount > 0) {
            val totalInvestment = buys.sumOf { it.amount * it.price }
            val totalReturn = sells.sumOf { it.amount * it.price }
            val weightedAverageBuyPrice = if (totalBuyAmount > 0) {
                totalInvestment / totalBuyAmount
            } else {
                0.0
            }

            val currentPrice = getCoinPrice(coinUuid)

            val newPortfolio = PortfolioPosition(
                coinUuid = coinUuid,
                name = buys.first().name,
                symbol = buys.first().symbol,
                totalAmount = remainingAmount,
                averageBuyPrice = weightedAverageBuyPrice,
                currentPrice = currentPrice,
                totalInvestment = totalInvestment - totalReturn,
                iconUrl = buys.first().iconUrl
            )

            insertOrUpdatePortfolioPosition(newPortfolio)
            val sparklinesForCoin = sparks.first()

            insertSparklineDataEntity(
                sparklineDataEntity = SparklineDataEntity(
                    coinUuid = coinUuid,
                    value = sparklinesForCoin.toString()
                )
            )


        } else {
            val currentPortfolio = getAllPortfolioPositions()
                .first()
                .find { it.coinUuid == coinUuid }
                .let {
                    it ?: return
                }
            deletePortfolioPosition(currentPortfolio)
        }
    }

    private suspend fun closeTrade(coinUuid: String) {
        // Trade schließen
        val transaction = getTransactionByCoinUuid(coinUuid)
        val buys = transaction.filter { it.type == TransactionType.BUY }
        val sells = transaction.filter { it.type == TransactionType.SELL }

        val totalBuyAmount = buys.sumOf { it.amount }
        val totalBuyValue = buys.sumOf { it.amount * it.price }
        val totalAverageBuyPrice = totalBuyValue / totalBuyAmount

        val totalSellAmount = sells.sumOf { it.amount }
        val totalSellValue = sells.sumOf { it.amount * it.price }
        val totalAverageSellPrice = totalSellValue - totalBuyValue

        val profit = totalSellValue - totalBuyValue
        val profitPercentage = (totalSellValue / totalBuyValue - 1) * 100

        val firstBuy = buys.minByOrNull { it.timestamp }!!
        val lastSell = sells.maxByOrNull { it.timestamp }!!

        val closedTrade = ClosedTrade(
            coinUuid = coinUuid,
            name = firstBuy.name,
            symbol = firstBuy.symbol,
            buyAmount = totalBuyAmount,
            buyPrice = totalAverageBuyPrice,
            buyTimestamp = firstBuy.timestamp,
            sellAmount = totalSellAmount,
            sellPrice = totalAverageSellPrice,
            sellTimestamp = lastSell.timestamp,
            profit = profit,
            profitPercentage = profitPercentage,
            iconUrl = firstBuy.iconUrl
        )

        insertClosedTrade(closedTrade)

        // Transaction abgeschlossen markieren
        transaction.forEach {
            updateTransaction(it.id, true)
        }


    }

}

