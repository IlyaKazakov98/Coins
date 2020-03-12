package com.readyfo.coins.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.FavoritesModel
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.model.MinimalCoinsModel

@Dao
abstract class CoinsDao {
    // CoinsRepository:
    @Transaction
    open suspend fun insertCoinsTrans(listCoinsModel: List<CoinsModel>, lastTimeUpdate: Long) {
        insertCoins(listCoinsModel)
        insertFavorites()
        insertLastCoinsUpdate(lastTimeUpdate)
    }

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertCoins(listCoinsModel: List<CoinsModel>)

    @Query("INSERT OR REPLACE INTO favorites_table(fav_coin_id, fav_symbol, favorites_id) SELECT coin_id, symbol, 0 FROM coins_table")
    abstract suspend fun insertFavorites()

    @Query("INSERT OR REPLACE INTO last_update_table(id, last_time_coins_update) VALUES (0, :lastTimeUpdate)")
    abstract suspend fun insertLastCoinsUpdate(lastTimeUpdate: Long)

    @Transaction
    open suspend fun updateCoinsTrans(listCoinsModel: List<CoinsModel>, lastTimeUpdate: Long) {
        updateCoins(listCoinsModel)
        insertLastCoinsUpdate(lastTimeUpdate)
    }

    @Update
    abstract suspend fun updateCoins(listCoinsModel: List<CoinsModel>)

    @Query("SELECT * FROM coins_table LEFT JOIN favorites_table ON favorites_table.fav_coin_id = coins_table.coin_id ORDER BY favorites_table.favorites_id DESC, coin_id ASC")
    abstract fun getCoins(): DataSource.Factory<Int, MinimalCoinsModel>

    @Query("SELECT * from coins_table  ORDER BY coin_id DESC LIMIT 1")
    abstract fun checkLastCoin(): CoinsModel

    @Query("SELECT last_time_coins_update FROM last_update_table ORDER BY id LIMIT 1")
    abstract fun checkLastCoinsUpdate(): Long

    @Query("UPDATE favorites_table SET favorites_id = :value WHERE fav_symbol = :symbolCoin")
    abstract suspend fun setFavorites(symbolCoin: String, value: Int)

    @Query("SELECT favorites_id FROM favorites_table WHERE fav_symbol = :symbolCoin")
    abstract suspend fun getFavorites(symbolCoin: String): Int

    // GlobalMetricsRepository:
    @Insert(onConflict = REPLACE)
    abstract suspend fun insertGM(globalMetricsModel: GlobalMetricsModel): Long

    @Update
    abstract suspend fun updateGM(globalMetricsModel: GlobalMetricsModel)

    @Query("UPDATE last_update_table SET last_time_gm_update = :lastTimeUpdate WHERE id = 0")
    abstract suspend fun updateLastGMUpdate(lastTimeUpdate: Long)

    @Query("SELECT * FROM global_metrics_table")
    abstract fun loadGM(): LiveData<GlobalMetricsModel>

    @Query("SELECT btc_dominance FROM global_metrics_table ORDER BY id LIMIT 1")
    abstract fun checkHaveData(): Double

    @Query("SELECT last_time_gm_update FROM last_update_table ORDER BY id LIMIT 1")
    abstract fun checkLastGMUpdate(): Long

    //DetailedInfoRepository:
    @Query("SELECT * FROM coins_table WHERE coin_id LIKE :coinId")
    abstract fun loadDetailedInfo(coinId: Int): LiveData<CoinsModel>

    @Query("SELECT * FROM favorites_table WHERE fav_coin_id LIKE :coinId")
    abstract fun loadFavoritesDetailed(coinId: Int): LiveData<FavoritesModel>

    // SearchAndSortRepository
    @Query("SELECT * FROM coins_table WHERE name LIKE '%' || :newText || '%'")
    abstract fun searchBy(newText: String): DataSource.Factory<Int, MinimalCoinsModel>

    @Query("SELECT * FROM coins_table LEFT JOIN favorites_table ON favorites_table.fav_coin_id = coins_table.coin_id ORDER BY price DESC")
    abstract fun sortByPrice(): DataSource.Factory<Int, MinimalCoinsModel>

    @Query("SELECT * FROM coins_table LEFT JOIN favorites_table ON favorites_table.fav_coin_id = coins_table.coin_id ORDER BY percent_change_1h DESC")
    abstract fun sortByPercent(): DataSource.Factory<Int, MinimalCoinsModel>

    // Тестовые запросы для логов
    @Query("SELECT * FROM coins_table LEFT JOIN favorites_table ON favorites_table.fav_coin_id = coins_table.coin_id ORDER BY favorites_table.favorites_id DESC, price DESC")
    abstract suspend fun testSortBy(): List<MinimalCoinsModel>

    @Query("SELECT * FROM coins_table  WHERE name LIKE '%' || :newText || '%'")
    abstract suspend fun testSearchBy(newText: String): List<CoinsModel>
}