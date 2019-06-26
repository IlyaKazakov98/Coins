package com.readyfo.coins

import android.app.Application
import com.readyfo.coins.db.CoinsDatabase
import com.readyfo.coins.http.Api

class App : Application()  {
    companion object {
        lateinit var instance: App
        lateinit var coinsDB: CoinsDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Api.init()
        instance = this
        coinsDB = CoinsDatabase.getInstance(context = this)
    }
}