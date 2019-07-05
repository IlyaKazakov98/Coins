package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.work.*
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.repository.CoinsRepository
import com.readyfo.coins.workmanager.UpLoadWorker
import java.util.concurrent.TimeUnit


class CoinsViewModel: ViewModel() {
    private lateinit var coinLiveData: LiveData<PagedList<CoinsModel>>
    private val workManager: WorkManager = WorkManager.getInstance()

    init {
        workManager.enqueueUniquePeriodicWork("network_update", ExistingPeriodicWorkPolicy.KEEP, applyFonWork())
    }

    fun init(){
        // Инициализируем LiveData и запрашиваем PagedList у Репозитория
        coinLiveData = CoinsRepository.initLiveData()
    }

    // Функция для запроса coinLiveData на обновленеие данных в Activity/Fragment
    fun getCoins() = coinLiveData

    // Функция обновления
    fun updateCoins(): LiveData<PagedList<CoinsModel>> = CoinsRepository.updateCoins()

    // Функция поиска по имени
    fun searchBy(newText: String) = CoinsRepository.searchBy(newText)

    // Функция сортировки
    fun sortBy(value: String) = CoinsRepository.sortBy(value)

    private fun applyFonWork(): PeriodicWorkRequest{
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return PeriodicWorkRequest.Builder(UpLoadWorker::class.java, 60, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
    }

}