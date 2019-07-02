package com.readyfo.coins.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.readyfo.coins.App
import com.readyfo.coins.http.Api
import com.readyfo.coins.model.CoinsModel
import kotlinx.coroutines.*
import java.lang.Exception


object CoinsRepository {
    // private var job: Job? = null
    private const val convertValet = "USD"

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    // Обновляем данные в DataSource и строим PagedList
    fun pagedListBuilder(): LiveData<PagedList<CoinsModel>>{
        onRefreshCoinsData("1", "30", convertValet)

        // Задаём параметры PagedList
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .setPageSize(30)
            .build()

        return LivePagedListBuilder(coinsDao.loadInitialCoins(), pagedListConfig)
            .build()
    }

    private fun onRefreshCoinsData(start: String, limit: String, convert: String){
        // Делаем запрос на сервер не блокируя поток
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = Api.coinsApi.getFirst30CoinsAsync(start, limit, convert).await()
                // И сохраняем ответ в БД
                async {
                    coinsDao.insert(response.data)
                }.await()

                Log.d("CoinsLogDB", "${coinsDao.logLoadInitialCoins()}")
            }
            catch (e: Exception){
                Log.d("ThrowableRefreshData", "${e.message}")
            }
        }
    }
}


//Api.coinsApi.getFirst30Coins(start, limit, convert).enqueue(object : Callback<Response> {
//    override fun onFailure(call: Call<Response>, t: Throwable) {
//        Log.d("CoinLogFailure", "${t.message}")
//    }
//
//    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
//        response.body()?.let {
//            Log.d("CoinLogResponse", "${it.data}")
//            async {
//                coinsDao.insert(it.data)
//            } .await()
//        }
//    }
//})