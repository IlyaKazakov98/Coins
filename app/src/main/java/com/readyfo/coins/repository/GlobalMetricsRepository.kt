package com.readyfo.coins.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.readyfo.coins.App
import com.readyfo.coins.Common.CONVERT_VALET
import com.readyfo.coins.Common.ONE_HOUR_IN_SECOND
import com.readyfo.coins.Common.TIME_IS_NOW
import com.readyfo.coins.http.Api
import com.readyfo.coins.model.GlobalMetricsModel
import kotlinx.coroutines.*

object GlobalMetricsRepository {
    private var updateBool = true
    private var checkLastUpdateBool = true

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    fun loadGlobalMetricsRepo(): LiveData<GlobalMetricsModel> {
        GlobalScope.launch {
            updateBool = coinsDao.checkHaveData() > 0
            if (updateBool)
                checkLastUpdateBool = checkLastUpdate()
            if (checkLastUpdateBool)
                onRefreshGlobalMetrics()

            Log.d("CoinsLog", "lastTimeUpdate: ${coinsDao.checkLastGMUpdate()}")

            coinsDao.updateLastGMUpdate(TIME_IS_NOW)
        }
        return coinsDao.loadGM()
    }

    private suspend fun onRefreshGlobalMetrics() {
        try {
            val response = Api.coinsApi.getGlobalMetrics(CONVERT_VALET).await()
            withContext(Dispatchers.Default) {
                if (updateBool) {
                    coinsDao.updateGM(response.data)
                    Log.d("CoinsLog", "UpdateGM")
                }
                else {
                    val id = coinsDao.insertGM(response.data)
                    Log.d("CoinsLog", "InsertGM, $id")
                }
            }
        } catch (e: Exception) {
            Log.e("CoinsErrorLog", "ThrowableRefreshCoins: ${e.message}")
        }
    }

    private fun checkLastUpdate(): Boolean {
        var localCheckLastUpdate = true
        val lastTimeUpdate: Long = coinsDao.checkLastGMUpdate()
        Log.d("CoinsLog", "lastTimeUpdate: $lastTimeUpdate")

        if (TIME_IS_NOW - lastTimeUpdate <= ONE_HOUR_IN_SECOND)
            localCheckLastUpdate = false

        Log.d("CoinsLog", "Разница: ${TIME_IS_NOW - lastTimeUpdate}")

        return localCheckLastUpdate
    }
}