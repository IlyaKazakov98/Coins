package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.MinimalCoinsModel
import com.readyfo.coins.paging.CoinsBoundaryCallBack
import com.readyfo.coins.repository.CoinsRepository
import com.readyfo.coins.repository.SearchAndSortRepository


class CoinsViewModel: ViewModel() {
    private lateinit var coinLiveData: LiveData<PagedList<MinimalCoinsModel>>
    private val boundaryCallBack = CoinsBoundaryCallBack()

    fun init(){
        // Инициализируем LiveData и запрашиваем PagedList у Репозитория
        if (this::coinLiveData.isInitialized)
            return
        else
            coinLiveData = pagedListBuilder(CoinsRepository.loadCoinsRepo(false))
    }

    // Функция для запроса coinLiveData на обновленеие данных в Activity/Fragment
    fun getCoins() = coinLiveData

    // Функция обновления
    fun updateCoins(){
        coinLiveData = pagedListBuilder(CoinsRepository.loadCoinsRepo(true))
    }

    // Функция поиска по имени
    fun searchBy(newText: String) = pagedListBuilder(SearchAndSortRepository.searchByRepo(newText))

    // Функция сортировки
    fun sortBy(value: Int) = pagedListBuilder(SearchAndSortRepository.sortByRepo(value))

    // Запись или удаление из ибранного
    fun setFavorites(position: String, value: Int) = pagedListBuilder(CoinsRepository.setFavoritesRepo(position, value))

    // Обновляем данные в DataSource и строим PagedList
    private fun pagedListBuilder(query: DataSource.Factory<Int, MinimalCoinsModel>): LiveData<PagedList<MinimalCoinsModel>>{
        // Задаём параметры PagedList
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .setPageSize(30)
            .build()

        return LivePagedListBuilder(query, pagedListConfig)
            .setBoundaryCallback(boundaryCallBack)
            .build()
    }
}