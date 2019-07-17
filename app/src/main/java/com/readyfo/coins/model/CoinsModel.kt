package com.readyfo.coins.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION
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
    val local_id: Int?,
    val symbol: String?,
    val name: String?,
    @Embedded
    val quote: Quote?
//    @Relation(entity = FavoritesModel::class, parentColumn = "local_id", entityColumn = "coin_id", projection = ["favorites_id"])
//    val fav_id: List<FavoritesModel>
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

// @Entity(tableName = "favorites_table")
// data class FavoritesModel(
//    @PrimaryKey
//    val coin_id: Int?,
//    val favorites_symbol: String?,
//    val favorites_id: Int?
// )