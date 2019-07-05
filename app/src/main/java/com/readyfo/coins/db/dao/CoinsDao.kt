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

    @Update
    suspend fun update(listCoinsModel: List<CoinsModel>)

    // Запрос на получение данных для DataSource
    @Query("SELECT * from coins_table")
    fun loadInitialCoins(): DataSource.Factory<Int, CoinsModel>

    @Query("SELECT * FROM coins_table WHERE name LIKE :newText || '%'")
    fun searchBy(newText: String): DataSource.Factory<Int, CoinsModel>

    @Query("SELECT * FROM coins_table ORDER BY :value ASC")
    fun sortBy(value: String): DataSource.Factory<Int, CoinsModel>

    // Удаление из бд
    @Query("DELETE FROM coins_table")
    suspend fun deleteAll()

    @Query("SELECT * from coins_table  ORDER BY localId DESC LIMIT 1")
    fun lastInsertRowid(): CoinsModel
}