package com.readyfo.coins.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.readyfo.coins.model.CoinsModel

@Dao
interface CoinsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listCoinsModel: List<CoinsModel>)

    // Добавить параметры запроса: startId, endId
    @Query("SELECT * from coins_table")
    fun getFirst30Coins(): LiveData<List<CoinsModel>>
}