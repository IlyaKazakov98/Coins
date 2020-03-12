package com.readyfo.coins.repository

import androidx.paging.DataSource
import com.readyfo.coins.App
import com.readyfo.coins.model.MinimalCoinsModel

object SearchAndSortRepository {

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    // Поиск по имения криптовальты
    suspend fun searchByRepo(newText: String): DataSource.Factory<Int, MinimalCoinsModel> {
        return coinsDao.searchBy(newText)
    }

    // Сортировк по цене и изменению процента
    fun sortByRepo(value: Int): DataSource.Factory<Int, MinimalCoinsModel> {
        return when (value) {
            0 -> coinsDao.sortByPrice()
            else -> coinsDao.sortByPercent()
        }
    }
}