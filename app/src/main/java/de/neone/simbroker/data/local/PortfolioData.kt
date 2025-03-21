package de.neone.simbroker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "portfolioData", indices = [Index(value = ["coinUuid"], unique = false)])
data class PortfolioData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coinUuid: String,
    val name: String,
    val symbol: String,
    val amount: Double,
    val averageBuyPrice: Double,
    val iconUrl: String,
)

@Entity(
    tableName = "portfolioDataSparklines",
    foreignKeys = [
        ForeignKey(
            entity = PortfolioData::class,
            parentColumns = ["id"],
            childColumns = ["coinUuid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("coinUuid")]
)

data class SparklineDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coinUuid: String,
    val value: String,
)

