package com.readyfo.coins.model

import androidx.room.Embedded

data class MinimalCoinsModel(
    val coin_id: Int?,
    val symbol: String?,
    val name: String?,
    @Embedded
    val favorites: FavoritesModel?,
    @Embedded
    val quote: DetailedInfoQuote?
)

data class DetailedInfoQuote(
    @Embedded
    val USD: DetailedInfoUSD?
)

data class DetailedInfoUSD(
    val price: Double?,
    val percent_change_1h: Double?
)