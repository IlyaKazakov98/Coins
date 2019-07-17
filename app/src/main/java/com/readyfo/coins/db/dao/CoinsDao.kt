package com.readyfo.coins.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.room.*
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.GlobalMetricsModel

@Dao
abstract class CoinsDao {
    // Сохранение в бд
    @Transaction
    open suspend fun insertCoinsAndGM(listCoinsModel: List<CoinsModel>, globalMetricsModel: GlobalMetricsModel){
        insert(listCoinsModel)
        insertGM(globalMetricsModel)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(listCoinsModel: List<CoinsModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGM(globalMetricsModel: GlobalMetricsModel)

    // Обновление бд
    @Transaction
    open suspend fun updateCoinsAndGM(listCoinsModel: List<CoinsModel>, globalMetricsModel: GlobalMetricsModel){
        update(listCoinsModel)
        updateGM(globalMetricsModel)
    }

    @Update
    abstract suspend fun update(listCoinsModel: List<CoinsModel>)

    @Update
    abstract suspend fun updateGM(globalMetricsModel: GlobalMetricsModel)

    @Query("UPDATE coins_table SET favorites = :value WHERE symbol = :symbolCoin" )
    abstract suspend fun setFavorites(symbolCoin: String, value: Int)

//    @Query("UPDATE favorites_table SET favorites_id = :value WHERE favorites_symbol = :symbolCoin" )
//    abstract suspend fun setFavorites(symbolCoin: String, value: Int)

    // Проверка на наличие данных
    @Query("SELECT * from coins_table  ORDER BY local_id DESC LIMIT 1")
    abstract fun lastInsertRowid(): CoinsModel

    @Query("SELECT id from global_metrics_table  ORDER BY id ASC LIMIT 1")
    abstract fun dataAvailabilityCheckDB(): Int

    // Запрос на получение данных для DataSource

    @Query("SELECT * from coins_table ORDER BY favorites DESC, local_id ASC")
    abstract fun getCoins(): DataSource.Factory<Int, CoinsModel>

//    @Query("SELECT * from coins_table ORDER BY (SELECT favorites_table.favorites_id FROM favorites_table INNER JOIN coins_table ON coins_table.symbol = favorites_symbol)  DESC, local_id ASC")
//    abstract fun getCoins(): DataSource.Factory<Int, CoinsModel>

    @Query("SELECT * from global_metrics_table")
    abstract fun getGlobalMetrics(): LiveData<GlobalMetricsModel>

    @Query("SELECT * FROM coins_table WHERE name LIKE :newText || '%'")
    abstract fun searchBy(newText: String): DataSource.Factory<Int, CoinsModel>

    @Query("SELECT * FROM coins_table ORDER BY price DESC")
    abstract fun sortByPrice(): DataSource.Factory<Int, CoinsModel>

    @Query("SELECT * FROM coins_table ORDER BY percent_change_1h DESC")
    abstract fun sortByPercent(): DataSource.Factory<Int, CoinsModel>

    // Тестовые запросы для логов
    @Query("SELECT * FROM coins_table ORDER BY favorites DESC")
    abstract suspend fun testSortBy(): List<CoinsModel>

    @Query("SELECT * FROM coins_table  WHERE name LIKE :newText || '%'")
    abstract suspend fun testSearchBy(newText: String): List<CoinsModel>


    // Удаление из бд
    @Query("DELETE FROM coins_table")
    abstract suspend fun deleteAll()
}