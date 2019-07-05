package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.repository.CoinsRepository


class CoinsViewModel: ViewModel() {
    private lateinit var coinLiveData: LiveData<PagedList<CoinsModel>>

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

}