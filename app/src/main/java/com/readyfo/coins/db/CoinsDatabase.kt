package com.readyfo.coins.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.readyfo.coins.db.dao.CoinsDao
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.FavoritesModel
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.model.LastUpdateModel

@Database(
    entities = [CoinsModel::class, FavoritesModel::class, GlobalMetricsModel::class, LastUpdateModel::class],
    exportSchema = false,
    version = 1
)

abstract class CoinsDatabase: RoomDatabase() {

    abstract fun getCoinsDao(): CoinsDao

    companion object {
        @Volatile
        private var instance: CoinsDatabase? = null

        private const val COINS_DATABASE_NAME = "coins_database.db"

        fun getInstance(context: Context): CoinsDatabase {
            if (instance == null) {
                synchronized(CoinsDatabase::class) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            CoinsDatabase::class.java,
                            COINS_DATABASE_NAME
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}