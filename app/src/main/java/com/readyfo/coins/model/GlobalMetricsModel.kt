package com.readyfo.coins.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GMResponse(
    @SerializedName("data")
    val `data` : GlobalMetricsModel
)

@Entity(tableName = "global_metrics_table")
data class GlobalMetricsModel(
    val btc_dominance: Double,
    val eth_dominance: Double,
    val active_cryptocurrencies: Int,
    val active_market_pairs: Int,
    val active_exchanges: Int,
    @Embedded
    val quote: GlobalMetricsQuote
)
{
    @PrimaryKey(autoGenerate = false)
    var id = 0
}

data class GlobalMetricsQuote(
    @Embedded
    @SerializedName("USD")
    val uSD: GlobalMetricsUSD
)

data class GlobalMetricsUSD(
    val total_market_cap: Double,
    val total_volume_24h: Double
)