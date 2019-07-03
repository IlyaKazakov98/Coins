package com.readyfo.coins.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

// Модель ответа с сервера
data class Response(
    val data: List<CoinsModel>
)

// Модель для сохранения в бд
@Entity (tableName = "coins_table")
data class CoinsModel(
    val symbol: String,
    val name: String,
    @Embedded
    val quote: Quote
)
{
    @PrimaryKey(autoGenerate = true)
    var localId: Int = 0
}

data class Quote(
    @Embedded
    val USD: USD
)

data class USD(
    val last_updated: String,
    val percent_change_1h: Double,
    val price: Double
)