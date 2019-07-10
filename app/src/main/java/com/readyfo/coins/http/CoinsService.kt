package com.readyfo.coins.http

import com.readyfo.coins.model.GMResponse
import com.readyfo.coins.model.Response
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinsService {
    // @Headers(
    //     "Accept: application/json",
    //     "X-CMC_PRO_API_KEY: 61911c37-bf05-4e3f-8395-636f94b754fe"
    // )
    @GET("v1/cryptocurrency/listings/latest")
    fun getCoinsAsync(@Query("start") start: String, @Query("limit") limit: String,
                      @Query("convert") convert: String): Deferred<Response>

    // @GET("v1/cryptocurrency/listings/latest")
    // fun getTabCoinAsync(@Query("start") start: String, @Query("limit") limit: String,
    //                   @Query("convert") convert: String): Deferred<TabResponse>

    @GET("/v1/global-metrics/quotes/latest")
    fun getGlobalMetrics(@Query("convert") convert: String): Deferred<GMResponse>

    // @Query("sort_dir") sort_dir: String
}