package com.readyfo.coins.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readyfo.coins.http.Api.init
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.model.GlobalMetricsQuote
import com.readyfo.coins.model.GlobalMetricsUSD
import com.readyfo.coins.repository.GlobalMetricsRepository
import kotlinx.coroutines.*

class GlobalMetricsViewModel: ViewModel() {
    private lateinit var liveDataGM: LiveData<GlobalMetricsModel>

    fun init() {
        if (this::liveDataGM.isInitialized)
            return
        else {
            liveDataGM = GlobalMetricsRepository.loadGlobalMetricsRepo()
        }
    }

    fun getGlobalMetrics() = liveDataGM
}