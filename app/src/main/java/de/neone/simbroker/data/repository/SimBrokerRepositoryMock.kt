package de.neone.simbroker.data.repository

import android.util.Log
import de.neone.simbroker.data.local.ClosedTrade
import de.neone.simbroker.data.local.PortfolioPosition
import de.neone.simbroker.data.local.SimBrokerDAO
import de.neone.simbroker.data.local.SparklineDataEntity
import de.neone.simbroker.data.local.Transaction
import de.neone.simbroker.data.local.TransactionType
import de.neone.simbroker.data.remote.Coin
import de.neone.simbroker.data.repository.mockdata.coins_Mockdata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SimBrokerRepositoryMock(
    private val simBrokerDAO: SimBrokerDAO,
) : SimBrokerRepositoryInterface {

    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return coins_Mockdata
    }

    override suspend fun getCoin(uuid: String, timePeriod: String): Coin {
        val result = coins_Mockdata.first { it.uuid == uuid }
        return result
    }

    override suspend fun getCoinPrice(uuid: String): Double {
        val coinPrice = coins_Mockdata.first { it.uuid == uuid }.price
        return coinPrice.toDouble()
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

    override fun getPortfolioPositionByCoinUuid(coinUuid: String): PortfolioPosition? {
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



    override suspend fun buyCoin(coin: Coin, amount: Double, price: Double) {
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
            updatePortfolioPosition(coin)

    }

    private suspend fun updatePortfolioPosition(coin: Coin) {
        val transaction = getTransactionByCoinUuid(coin.uuid)

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

            val currentPrice = getCoinPrice(coin.uuid)

            val newPortfolio = PortfolioPosition(
                coinUuid = coin.uuid,
                name = buys.first().name,
                symbol = buys.first().symbol,
                totalAmount = remainingAmount,
                averageBuyPrice = weightedAverageBuyPrice,
                currentPrice = currentPrice,
                totalInvestment = totalInvestment - totalReturn,
                iconUrl = buys.first().iconUrl
            )

            insertOrUpdatePortfolioPosition(newPortfolio)

            val sparklinesForCoin = coin.sparkline
            Log.d("Sparklines", sparklinesForCoin.toString())

            sparklinesForCoin.forEach { match ->
                val sparklineDataEntity = SparklineDataEntity(
                    coinUuid = coin.uuid ,
                    value = match.toString()
                )
                insertSparklineDataEntity(sparklineDataEntity)
            }

        } else {
            val currentPortfolio = getAllPortfolioPositions()
                .first()
                .find { it.coinUuid == coin.uuid }
                .let {
                    it ?: return
                }
            deletePortfolioPosition(currentPortfolio)
        }
    }


    override suspend fun sellCoin(coin: Coin, amount: Double, price: Double) {

        val position = getPortfolioPositionByCoinUuid(coin.uuid)
        if (position == null || position.totalAmount < amount) {
            throw Exception("Not enough coins in portfolio")
        }

        val transaction = Transaction(
            coinUuid = coin.uuid,
            name = position.name,
            symbol = position.symbol,
            iconUrl = position.iconUrl,
            type = TransactionType.SELL,
            amount = amount,
            price = price
        )
        insertTransaction(transaction)
        updatePortfolioPosition(coin)

        val updatedPosition = getPortfolioPositionByCoinUuid(coin.uuid)
        if (updatedPosition == null || updatedPosition.totalAmount == 0.0) {
            createClosedTrade(coin)
        }

    }

    private suspend fun createClosedTrade(coin: Coin) {
        val transactions = getTransactionByCoinUuid(coin.uuid)

        val buyTransactions = transactions.filter { it.type == TransactionType.BUY }
        val sellTransactions = transactions.filter { it.type == TransactionType.SELL }

        val totalBought = buyTransactions.sumOf { it.amount }
        val totalSpent = buyTransactions.sumOf { it.amount * it.price }
        val totalSold = sellTransactions.sumOf { it.amount }
        val totalReceived = sellTransactions.sumOf { it.amount * it.price }

        val profit = totalReceived - totalSpent
        val profitPercentage = (totalReceived / totalSpent - 1) * 100

        val firstBuy = buyTransactions.minByOrNull { it.timestamp }!!
        val lastSell = sellTransactions.maxByOrNull { it.timestamp }!!

        val closedTrade = ClosedTrade(
            coinUuid = coin.uuid,
            name = firstBuy.name,
            symbol = firstBuy.symbol,
            buyAmount = totalBought,
            buyPrice = totalSpent / totalBought,  // Durchschnittskaufpreis
            buyTimestamp = firstBuy.timestamp,
            sellAmount = totalSold,
            sellPrice = totalReceived / totalSold,  // Durchschnittsverkaufspreis
            sellTimestamp = lastSell.timestamp,
            profit = profit,
            profitPercentage = profitPercentage,
            iconUrl = firstBuy.iconUrl
        )

        insertClosedTrade(closedTrade)

        transactions.forEach { transaction ->
            updateTransaction(transaction.id, true)
        }
    }

}