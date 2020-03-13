package com.readyfo.coins.repository

import androidx.paging.DataSource
import com.readyfo.coins.App
import com.readyfo.coins.model.MinimalCoinsModel

object SearchAndSortRepository {

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    // Сортировк по цене и изменению процента
    fun sortByRepo(value: Int): DataSource.Factory<Int, MinimalCoinsModel> {
        return when (value) {
            0 -> coinsDao.sortByPrice()
            else -> coinsDao.sortByPercent()
        }
    }
}