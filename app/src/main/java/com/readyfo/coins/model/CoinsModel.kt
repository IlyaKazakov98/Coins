package com.readyfo.coins.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Модель ответа с сервера
data class Response(
    val data: List<CoinsModel>
)

// Модель для сохранения в бд
@Entity (tableName = "coins_table")
data class CoinsModel(
    @PrimaryKey
    @SerializedName("cmc_rank")
    val localId: Int?,
    val id: Int?,
    val symbol: String?,
    val name: String?,
    @Embedded
    val quote: Quote?
)

data class Quote(
    @Embedded
    val USD: USD?
)

data class USD(
    val last_updated: String?,
    val percent_change_1h: Double?,
    val price: Double?
)