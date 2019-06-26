package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.repository.CoinsRepository


class CoinsViewModel: ViewModel() {
    private lateinit var coinLiveData: LiveData<List<CoinsModel>>

    fun init(){
        coinLiveData = CoinsRepository.getFirst30Coins()
    }

    fun getCoins() = coinLiveData
}