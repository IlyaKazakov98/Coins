package com.readyfo.coins.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Response(
    val data: List<CoinsModel>
)

@Entity (tableName = "coins_table")
data class CoinsModel(
    @PrimaryKey
    val id: Int,
    val symbol: String,
    val name: String
    //val quote: Quote
)

data class Quote(
    val USD: USD
)

data class USD(
    val last_updated: String,
    val percent_change_1h: Double,
    val price: Double
)