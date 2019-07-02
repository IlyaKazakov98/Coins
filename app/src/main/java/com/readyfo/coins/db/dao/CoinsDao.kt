package com.readyfo.coins.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.readyfo.coins.model.CoinsModel

@Dao
interface CoinsDao {
    // Сохранение в бд
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listCoinsModel: List<CoinsModel>)

    // Запрос на получение данных для DataSource
    @Query("SELECT * from coins_table")
    fun loadInitialCoins(): DataSource.Factory<Int, CoinsModel>


    // Удаление из бд
    @Query("DELETE FROM coins_table")
    suspend fun deleteAll()

    @Query("SELECT * from coins_table")
    fun logLoadInitialCoins(): List<CoinsModel>

    @Query("SELECT * from coins_table WHERE id = 1")
    fun logLoadInitialCoin(): CoinsModel

    @Query("SELECT * from coins_table WHERE symbol = :eth")
    fun logLoadETH(eth: String): CoinsModel
}