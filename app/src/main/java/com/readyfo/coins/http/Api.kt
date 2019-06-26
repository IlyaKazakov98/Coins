package com.readyfo.coins.http

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Api {
    private const val apiKey = "61911c37-bf05-4e3f-8395-636f94b754fe"
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    lateinit var coinsApi: CoinsService

    fun init(){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val interceptor = Interceptor {
            val request = it.request().newBuilder()
                .header("Accept", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", apiKey)
                .build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

        coinsApi = retrofit.create(CoinsService::class.java)

    }
}