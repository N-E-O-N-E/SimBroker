package de.neone.simbroker.data.helper


object coinCalc {
    fun calculateCoinAmount(amountInEur: Double, pricePerCoin: Double): Double {
        return amountInEur / pricePerCoin // Betrag durch Preiseingabe
        // 500 / 70000 = 0.7142857142857143
    }
    fun calculateEurAmount(amountInCoins: Double, pricePerCoin: Double): Double {
        return amountInCoins * pricePerCoin // Betrag durch Anteileingabe
        // 0.7142857142857143 * 70000 = 500

    }
}