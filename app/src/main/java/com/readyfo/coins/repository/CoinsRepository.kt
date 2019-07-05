package com.readyfo.coins.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.readyfo.coins.App
import com.readyfo.coins.http.Api
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.Response
import com.readyfo.coins.paging.CoinsBoundaryCallBack
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.await


object CoinsRepository {
    private var job: Job? = null
    private const val convertValet = "USD"
    private val boundaryCallBack = CoinsBoundaryCallBack()
    private var updateBool = true

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    // Вызываем эту функцию при запуске приложения
    fun initLiveData(): LiveData<PagedList<CoinsModel>>{
        GlobalScope.launch {
            // Узнаём надо записать данные или обновить
            val limit = lastCoin()
            onRefreshCoinsData("1", "$limit", convertValet)
        }

        return pagedListBuilder(coinsDao.loadInitialCoins())
    }

    fun updateCoins(): LiveData<PagedList<CoinsModel>>{
        GlobalScope.launch {
            // Определяем последний сохранённый элемент в бд и передаём его как размер загружаемых данных с сервера для
            // обновления
            val limit = lastCoin()
            updateBool = true
            onRefreshCoinsData("1", "$limit", convertValet)
        }

        return pagedListBuilder(coinsDao.loadInitialCoins())
    }

    fun itemAtEndLoaded(itemAtEnd: CoinsModel, limit: String, convert: String){
        updateBool = false
        // Передаюм ID последнего элемента, сохранённого в бд, как стартовый параметр для загрузки новых данных с сервера
        GlobalScope.launch {
            onRefreshCoinsData("${itemAtEnd.localId}", limit, convert)
        }
    }

    // Функция проверки наличия данных в бд. В дальнейшем(onRefreshCoinsData()), зависимости от результата мы обновим
    // или запишим данные в бд
    private suspend fun lastCoin(): Int{
        var limit = 0

        job = GlobalScope.launch {
            val lastCoin: CoinsModel? = coinsDao.lastInsertRowid()

            if (lastCoin != null)
                limit = lastCoin.localId!!
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

    fun searchBy(newText: String) = pagedListBuilder(coinsDao.searchBy(newText))

    fun sortBy(value: String) = pagedListBuilder(coinsDao.sortBy(value))

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
        // Делаем запрос на сервер не блокируя поток
        GlobalScope.launch{
            try {
                val response = Api.coinsApi.getFirst30CoinsAsync(start, limit, convert).await()
                // Проверяем что нам надо сделать с полученными данными и в зависимости от этого сохроняем или обновляем их
                if (updateBool) {
                    withContext(Dispatchers.Default) {
                        coinsDao.update(response.data)
                        Log.d("CoinsLog", "Update")
                    }
                } else {
                    withContext(Dispatchers.Default) {
                        coinsDao.insert(response.data)
                        Log.d("CoinsLog", "Insert")
                    }
                }
            } catch (e: Exception) {
                Log.d("CoinsLog", "ThrowableRefreshData: ${e.message}")
                // Snackbar.make(this, "Данные не обновленны, проверте подключение к интернету,", Snackbar.LENGTH_LONG)
            }
        }
    }
}


//          var responseForSaveInDB: List<CoinsModel>? = null

            // Делаем запрос на сервер не блокируя поток
//          GlobalScope.async{
//            Api.coinsApi.getFirst30CoinsAsync(start, limit, convert).enqueue(object : Callback<Response> {
//                override fun onFailure(call: Call<Response>, t: Throwable) {
//                    Log.d("CoinLogFailure", "${t.message}")
//                }
//
//                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
//                    response.body()?.let {
//                        Log.d("CoinLogResponse", "${it.data}")
//                        if (response.isSuccessful)
//                            responseForSaveInDB = it.data
//                    }
//                }
//            })
//        }.await()
//
//        // Согласен, спорное решение блокировать поток на 1.5 секунды, но подругому не получается отсинхронить
//        // загрузку с сервера и сохранением в бд. ПОКА НЕ ПОЛУЧАЕТСЯ)
//
//        delay(1500)
//
//        // Проверяем что нам надо сделать с полученными данными от сервера и в зависимости от этого сохроняем или обновляем их
//        if (updateBool) {
//            withContext(Dispatchers.Default) {
//                responseForSaveInDB?.let { coinsDao.update(it) }
//                Log.d("CoinLogInsertOrUpdate", "Update")
//            }
//        } else {
//            withContext(Dispatchers.Default) {
//                responseForSaveInDB?.let { coinsDao.insert(it) }
//                Log.d("CoinLogInsertOrUpdate", "Insert")
//            }
//        }


