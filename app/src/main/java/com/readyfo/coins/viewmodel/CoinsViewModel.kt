package com.readyfo.coins.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.readyfo.coins.model.MinimalCoinsModel
import com.readyfo.coins.paging.CoinsBoundaryCallBack
import com.readyfo.coins.repository.CoinsRepository
import com.readyfo.coins.repository.SearchAndSortRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CoinsViewModel : ViewModel() {
    private lateinit var coinsMediatorLiveData: MediatorLiveData<PagedList<MinimalCoinsModel>>
    private lateinit var coinsLiveData: LiveData<PagedList<MinimalCoinsModel>>
    private lateinit var searchCoinsLiveData: LiveData<PagedList<MinimalCoinsModel>>
    private lateinit var sortCoinsLiveData: LiveData<PagedList<MinimalCoinsModel>>
    private val boundaryCallBack = CoinsBoundaryCallBack()

    fun init() {
        // Инициализируем LiveData и запрашиваем PagedList у Репозитория
        if (this::coinsLiveData.isInitialized)
            return
        else {
            coinsMediatorLiveData = MediatorLiveData<PagedList<MinimalCoinsModel>>()
            updateCoins(false)
        }
    }

    val coins: LiveData<PagedList<MinimalCoinsModel>>
        get() = coinsMediatorLiveData

    fun updateCoins(isForcedUpdate: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                coinsLiveData = pagedListBuilder(CoinsRepository.loadCoinsRepo(isForcedUpdate))
            }
            coinsMediatorLiveData.addSource(coinsLiveData) {
                coinsMediatorLiveData.value = it
            }
        }
    }

    // Функция поиска по имени
    fun searchBy(newText: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchCoinsLiveData =
                    pagedListBuilder(SearchAndSortRepository.searchByRepo(newText))
            }
            coinsMediatorLiveData.addSource(searchCoinsLiveData) {
                coinsMediatorLiveData.value = it
            }
        }
    }

    // Функция сортировки
    fun sortBy(value: Int) {
        sortCoinsLiveData = pagedListBuilder(SearchAndSortRepository.sortByRepo(value))
        coinsMediatorLiveData.addSource(sortCoinsLiveData) {
            coinsMediatorLiveData.value = it
        }
    }

    // Запись или удаление из ибранного
    fun setFavorites(position: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            CoinsRepository.setFavoritesRepo(position, value)
        }
    }

    // Обновляем данные в DataSource и строим PagedList
    private fun pagedListBuilder(query: DataSource.Factory<Int, MinimalCoinsModel>): LiveData<PagedList<MinimalCoinsModel>> {
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