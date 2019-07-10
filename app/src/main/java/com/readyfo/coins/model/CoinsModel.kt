package com.readyfo.coins.model

import androidx.room.*
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
    val symbol: String?,
    val name: String?,
    @Embedded
    val quote: Quote?
)
{
    var favorites: Int = 0
}

data class Quote(
    @Embedded
    val USD: USD?
)

data class USD(
    val price: Double?,
    val volume_24h: Double?,
    val percent_change_1h: Double?,
    val percent_change_24h: Double?,
    val percent_change_7d: Double?,
    val market_cap: Double?,
    val last_updated: String?
)

// Entity(tableName = "favorites_table")
// ata class FavoritesModel(
//    @PrimaryKey
//    val id: Int?,
//    val favorites_symbol: String?,
//    val favorites_id: Int?
// )