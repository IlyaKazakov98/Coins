package com.readyfo.coins.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.readyfo.coins.App
import com.readyfo.coins.http.Api
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.Response
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Callback
import java.util.*

object CoinsRepository {

    private lateinit var date: Date
    private const val oneHourInMilliSec: Long = 3600000

    private val coinsDao by lazy {
        App.coinsDB.getCoinsDao()
    }

    fun getFirst30Coins(): LiveData<List<CoinsModel>> {
        onRefreshCoinsData("1", "3", "USD")

        return coinsDao.getFirst30Coins()
    }

    private fun onRefreshCoinsData(start: String, limit: String, convert: String){

        GlobalScope.launch {
            Api.coinsApi.getFirst30Coins(start, limit, convert).enqueue(object : Callback<Response> {
                override fun onFailure(call: retrofit2.Call<Response>, t: Throwable) {
                    Log.d("CoinLogFailure", "${t.message}")
                }

                override fun onResponse(call: retrofit2.Call<Response>, response: retrofit2.Response<Response>) {
                    response.body()?.let {
                        Log.d("CoinLogResponse", "${it.data}")
                        launch {
                            coinsDao.insert(it.data)
                        }
                    }
                }
            })
        }
    }
}