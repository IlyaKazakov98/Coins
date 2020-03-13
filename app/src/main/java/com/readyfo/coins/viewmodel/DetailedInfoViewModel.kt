package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.repository.DetailedInfoRepository

class DetailedInfoViewModel(private val stateHandle: SavedStateHandle): ViewModel() {

    companion object{
        private const val COIN_KEY = "coinId"
    }

    private val savedStateHandle: MutableLiveData<Int> =
        stateHandle.getLiveData(COIN_KEY)

    fun saveCurrentCoinId(coinId: Int?) {
        savedStateHandle.value = coinId
    }

    private val detailInfoLiveData: LiveData<CoinsModel> by lazy {
        loadDetailedInfo()
    }

    private fun loadDetailedInfo(): LiveData<CoinsModel>{
        val localCoinId = stateHandle.get<Int>(COIN_KEY)

        return DetailedInfoRepository.loadDetailedInfoRepo(localCoinId!!)
    }

    fun getDetailedInfo() = detailInfoLiveData
}