package com.readyfo.coins.http

import com.readyfo.coins.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinsService {
    // @Headers(
    //     "Accept: application/json",
    //     "X-CMC_PRO_API_KEY: 61911c37-bf05-4e3f-8395-636f94b754fe"
    // )
    @GET("v1/cryptocurrency/listings/latest")
    fun getFirst30Coins(@Query("start") start: String, @Query("limit") limit: String, @Query("convert") convert: String): Call<Response>
}