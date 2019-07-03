package com.readyfo.coins.paging

import androidx.paging.PagedList
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.repository.CoinsRepository
import kotlinx.coroutines.*

class CoinsBoundaryCallBack: PagedList.BoundaryCallback<CoinsModel>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: CoinsModel) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                CoinsRepository.itemAtEndLoaded(itemAtEnd, "30", "USD")
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: CoinsModel) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}