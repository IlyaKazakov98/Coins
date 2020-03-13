package com.readyfo.coins.repository

import android.util.Log
import androidx.paging.DataSource
import com.readyfo.coins.App
import com.readyfo.coins.Common.CONVERT_VALET
import com.readyfo.coins.Common.ONE_HOUR_IN_SECOND
import com.readyfo.coins.Common.TIME_IS_NOW
import com.readyfo.coins.TAG
import com.readyfo.coins.http.Api
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.MinimalCoinsModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object CoinsRepository {
    private var jobLastCoin: Job? = null
    private var updateBool = true
    private var checkLastUpdateBool = true

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    suspend fun loadCoinsRepo(forcedUpdate: Boolean): DataSource.Factory<Int, MinimalCoinsModel> {
        val limit = checkLastCoin()
        // Проверяем время последнего обновление
        if (updateBool && !forcedUpdate)
            checkLastUpdateBool = checkLastUpdate()
        // Если прошло больше часа с последнего обновления, то обновляем
        if (checkLastUpdateBool)
            onRefreshCoinsData("1", "$limit", CONVERT_VALET)
        return coinsDao.getCoins()
    }

    // Подгрузка данных с сервера в бд, по просьбе BoundaryCallback()
    suspend fun itemAtEndLoaded(itemAtEnd: MinimalCoinsModel, limit: String, convert: String) {
        updateBool = false
        // Передаюм ID последнего элемента, сохранённого в бд, как стартовый параметр для загрузки новых данных с сервера
        Log.d(TAG, "itemAtEndLoaded: $itemAtEnd")
        onRefreshCoinsData("${itemAtEnd.coin_id?.plus(1)}", limit, convert)
    }

    private suspend fun onRefreshCoinsData(start: String, limit: String, convert: String) {
        try {
            val response = Api.coinsApi.getCoinsAsync(start, limit, convert).await()
            // Проверяем что нам надо сделать с полученными данными и в зависимости от этого сохроняем или обновляем их
            if (updateBool) {
                coinsDao.updateCoinsTrans(response.data, TIME_IS_NOW)
                Log.d(TAG, "Update")
            } else {
                coinsDao.insertCoinsTrans(response.data, TIME_IS_NOW)
                updateBool = true
                Log.d(TAG, "Insert")
            }

        } catch (e: Exception) {
            Log.e(TAG, "ThrowableRefreshCoins: ${e.message}")
        }
    }

    // Обновляем значение столбца favorites
    suspend fun setFavoritesRepo(symbol: String, value: Int) {
        coinsDao.setFavorites(symbol, value)
    }

    // Функция проверки наличия данных в бд. В дальнейшем(onRefreshCoinsData()), зависимости от результата мы обновим
    // или запишим данные в бд
    private suspend fun checkLastCoin(): Int {
        var limit = 0

        jobLastCoin = GlobalScope.launch {
            val lastCoin: CoinsModel? = coinsDao.checkLastCoin()

            if (lastCoin != null)
                limit = lastCoin.coin_id!!
            else {
                limit = 30
                updateBool = false
            }
        }
        // Дожидаемся ответа от бд и только после этого идём дальше
        jobLastCoin!!.join()

        return limit
    }

    // Пороверяем последнее время обновления, если разница превышает 1 час, то обновляем. Если обновленин происходит по
    // просьбе пользователя(forcedUpdate), то обновляем в обязательном порядке
    private fun checkLastUpdate(): Boolean {
        var localStartUpdated = true

        val lastTimeUpdate: Long = coinsDao.checkLastCoinsUpdate()
        if (TIME_IS_NOW - lastTimeUpdate <= ONE_HOUR_IN_SECOND)
            localStartUpdated = false

        return localStartUpdated
    }
}