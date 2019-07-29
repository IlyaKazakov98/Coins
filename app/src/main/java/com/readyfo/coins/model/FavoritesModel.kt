package com.readyfo.coins.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
 data class FavoritesModel(
    @PrimaryKey
    val fav_coin_id: Int?,
    val fav_symbol: String?
 ){
    var favorites_id: Int? = 0
}