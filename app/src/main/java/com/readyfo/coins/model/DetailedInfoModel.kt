//package com.readyfo.coins.model
//
//import androidx.room.Embedded
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import com.google.gson.annotations.SerializedName
//
//// Модель ответа с сервера
//data class TabResponse(
//    val data: List<TabModel>
//)
//
//// Модель для сохранения в бд
//@Entity(tableName = "tab_table")
//data class DetailedInfoModel(
//    @PrimaryKey
//    @SerializedName("cmc_rank")
//    val local_id: Int?,
//    val id: Int?,
//    @Embedded
//    val quote: TabQuote?
//)
//
//data class TabQuote(
//    @Embedded
//    val USD: TabUSD?
//)
//
//data class TabUSD(
//    val volume_24h: Double?,
//    val percent_change_24h: Double?,
//    val percent_change_7d: Double?,
//    val market_cap: Double?,
//    val last_updated: String?
//)