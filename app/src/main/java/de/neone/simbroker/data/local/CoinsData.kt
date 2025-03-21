package de.neone.simbroker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "coinsDto")
data class CoinEntityDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uuid: String,
    val symbol: String,
    val name: String,
    val color: String?,
    val iconUrl: String,
    val price: String,
    val marketCap: String,
    val listedAt: Long,
    val tier: Int,
    val change: String,
    val rank: Int,
    val lowVolume: Boolean,
    val coinrankingUrl: String,
    val h24Volume: String,
    val description: String?,
    val priceAt: Long?
)

@Entity(
    tableName = "sparklinesDto",
    foreignKeys = [
        ForeignKey(
            entity = CoinEntityDto::class,
            parentColumns = ["id"],
            childColumns = ["coinUuid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("coinUuid")]
)

data class SparklineEntityDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coinUuid: String,
    val value: String,
    val position: Int
)