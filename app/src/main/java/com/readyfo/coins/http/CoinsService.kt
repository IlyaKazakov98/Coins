package com.readyfo.coins.http

import com.readyfo.coins.model.GMResponse
import com.readyfo.coins.model.Response
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinsService {
    @GET("v1/cryptocurrency/listings/latest")
    fun getCoinsAsync(@Query("start") start: String, @Query("limit") limit: String,
                      @Query("convert") convert: String): Deferred<Response>

    @GET("/v1/global-metrics/quotes/latest")
    fun getGlobalMetrics(@Query("convert") convert: String): Deferred<GMResponse>
}