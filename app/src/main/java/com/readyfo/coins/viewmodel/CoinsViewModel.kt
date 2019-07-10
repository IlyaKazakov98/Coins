package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.work.*
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.repository.CoinsRepository
import com.readyfo.coins.workmanager.UpLoadWorker
import java.util.concurrent.TimeUnit


class CoinsViewModel: ViewModel() {
    private lateinit var coinLiveData: LiveData<PagedList<CoinsModel>>
    private lateinit var liveDataGM: LiveData<GlobalMetricsModel>

    fun init(){
        // Инициализируем LiveData и запрашиваем PagedList у Репозитория
        coinLiveData = CoinsRepository.initRepo()
    }

    // Функция для запроса coinLiveData на обновленеие данных в Activity/Fragment
    fun getCoins() = coinLiveData

    fun getGlobalMetrics() = liveDataGM

    // Функция обновления
    fun updateCoins(): LiveData<PagedList<CoinsModel>> = CoinsRepository.updateCoinsRepo()

    // Функция поиска по имени
    fun searchBy(newText: String) = CoinsRepository.searchByRepo(newText)

    // Функция сортировки
    fun sortBy(value: Int) = CoinsRepository.sortByRepo(value)

    // Запись или удаление из ибранного
    fun setFavorites(position: String, value: Int) = CoinsRepository.setFavoritesRepo(position, value)

}