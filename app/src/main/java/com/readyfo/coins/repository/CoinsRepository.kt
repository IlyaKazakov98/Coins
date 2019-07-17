package com.readyfo.coins.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.readyfo.coins.App
import com.readyfo.coins.http.Api
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.paging.CoinsBoundaryCallBack
import kotlinx.coroutines.*


object CoinsRepository {
    private var job: Job? = null
    private const val convertValet = "USD"
    private val boundaryCallBack = CoinsBoundaryCallBack()
    private var updateBool = true
    // private var updateGMBool = false

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    // Вызываем эту функцию при запуске приложения
    fun initRepo(): LiveData<PagedList<CoinsModel>>{
        GlobalScope.launch {
            // Узнаём надо записать данные или обновить
            val limit = lastCoin()
            onRefreshCoinsData("1", "$limit", convertValet)
        }
        return pagedListBuilder(coinsDao.getCoins())
    }

    fun updateCoinsRepo(): LiveData<PagedList<CoinsModel>>{
        GlobalScope.launch {
            // Определяем последний сохранённый элемент в бд и передаём его как размер загружаемых данных с сервера для
            // обновления
            val limit = lastCoin()
            updateBool = true
            onRefreshCoinsData("1", "$limit", convertValet)
        }
        return pagedListBuilder(coinsDao.getCoins())
    }

    // Подгрузка данных с сервера в бд, по просьбе BoundaryCallback()
    fun itemAtEndLoaded(itemAtEnd: CoinsModel, limit: String, convert: String){
        updateBool = false
        // Передаюм ID последнего элемента, сохранённого в бд, как стартовый параметр для загрузки новых данных с сервера
        GlobalScope.launch {
            onRefreshCoinsData("${itemAtEnd.local_id}", limit, convert)
        }
    }

    // Функция проверки наличия данных в бд. В дальнейшем(onRefreshCoinsData()), зависимости от результата мы обновим
    // или запишим данные в бд
    private suspend fun lastCoin(): Int{
        var limit = 0

        job = GlobalScope.launch {
            val lastCoin: CoinsModel? = coinsDao.lastInsertRowid()

            if (lastCoin != null)
                limit = lastCoin.local_id!!
            else {
                limit = 30
                updateBool = false
            }
        }
        // Дожидаемся ответа от бд и только после этого идём дальше
        job!!.join()

        Log.d("CoinsLog", "InsertOrUpdate: $limit")
        return limit
    }

    // Обновляем данные в DataSource и строим PagedList
    private fun pagedListBuilder(query: DataSource.Factory<Int, CoinsModel>): LiveData<PagedList<CoinsModel>>{
        // Задаём параметры PagedList
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .setPageSize(30)
            .build()

        return LivePagedListBuilder(query, pagedListConfig)
            .setBoundaryCallback(boundaryCallBack)
            .build()
    }

    private suspend fun onRefreshCoinsData(start: String, limit: String, convert: String){
        // Делаем сразу 2 запроса на сервер, не блокируя поток
        GlobalScope.launch(Dispatchers.IO){
            try {
                val response = Api.coinsApi.getCoinsAsync(start, limit, convert).await()
                val responseGM = Api.coinsApi.getGlobalMetrics(convert).await()
                // Проверяем что нам надо сделать с полученными данными и в зависимости от этого сохроняем или обновляем их
                if (updateBool) {
                    withContext(Dispatchers.Default) {
                        coinsDao.updateCoinsAndGM(response.data, responseGM.data)
                        Log.d("CoinsLog", "HaveGM: ${coinsDao.getGlobalMetrics().value}")
                        Log.d("CoinsLog", "Update")
                    }
                }
                else {
                    withContext(Dispatchers.Default) {
                        coinsDao.insertCoinsAndGM(response.data, responseGM.data)
                        Log.d("CoinsLog", "Insert")
                    }
                }
            } catch (e: Exception) {
                Log.e("CoinsErrorLog", "ThrowableRefreshCoins: ${e.message}")
                // Snackbar.make(this, "Данные не обновленны, проверте подключение к интернету,", Snackbar.LENGTH_LONG)
            }
        }
    }

    // Обновляем значение столбца favorites
    fun setFavoritesRepo(symbol: String, value: Int): LiveData<PagedList<CoinsModel>>{
        GlobalScope.launch(Dispatchers.IO) {
            coinsDao.setFavorites(symbol, value)
        }
        return pagedListBuilder(coinsDao.getCoins())
    }

    // Поиск по имения криптовальты
    fun searchByRepo(newText: String): LiveData<PagedList<CoinsModel>> {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("CoinsLog", "searchByCoins: ${coinsDao.testSearchBy(newText)}")
        }
        return pagedListBuilder(coinsDao.searchBy(newText))
    }

    // Сортировк по цене и изменению процента
    fun sortByRepo(value: Int): LiveData<PagedList<CoinsModel>> {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("CoinsLog", "sortByCoins: ${coinsDao.testSortBy()}")
        }
        return pagedListBuilder(when(value){
            0 -> coinsDao.sortByPrice()
            else -> coinsDao.sortByPercent()
        })
    }

//    private suspend fun dataAvailabilityCheck(): Boolean?{
//        var haveData: Boolean? = null
//
//        job = GlobalScope.launch {
//            haveData = coinsDao.dataAvailabilityCheckDB() > 0
//            Log.d("CoinsLog", "haveData: $haveData")
//        }
//        job?.join()
//
//        return haveData
//    }
}


