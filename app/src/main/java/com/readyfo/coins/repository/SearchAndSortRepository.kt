package com.readyfo.coins.repository

import android.util.Log
import androidx.paging.DataSource
import com.readyfo.coins.App
import com.readyfo.coins.model.MinimalCoinsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object SearchAndSortRepository {

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    // Поиск по имения криптовальты
    fun searchByRepo(newText: String): DataSource.Factory<Int, MinimalCoinsModel>  {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("CoinsLog", "searchByCoins: ${coinsDao.testSearchBy(newText)}")
        }
        return coinsDao.searchBy(newText)
    }

    // Сортировк по цене и изменению процента
    fun sortByRepo(value: Int): DataSource.Factory<Int, MinimalCoinsModel> {
        return when (value) {
                0 -> coinsDao.sortByPrice()
                else -> coinsDao.sortByPercent()
            }
    }}