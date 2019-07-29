package com.readyfo.coins.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_update_table")
data class LastUpdateModel (
    @PrimaryKey
    val id: Int?,
    val last_time_coins_update: Long?,
    val last_time_gm_update: Long?
    )