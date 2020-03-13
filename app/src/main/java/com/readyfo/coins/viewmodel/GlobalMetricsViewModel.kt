package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.repository.GlobalMetricsRepository

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