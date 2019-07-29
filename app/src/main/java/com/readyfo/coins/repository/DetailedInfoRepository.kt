package com.readyfo.coins.repository

import androidx.lifecycle.LiveData
import com.readyfo.coins.App
import com.readyfo.coins.model.CoinsModel

object DetailedInfoRepository {

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    fun loadDetailedInfoRepo(coinId: Int): LiveData<CoinsModel> {
        return coinsDao.loadDetailedInfo(coinId)
    }
}