package com.readyfo.coins.paging

import androidx.paging.PagedList
import com.readyfo.coins.model.MinimalCoinsModel
import com.readyfo.coins.repository.CoinsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CoinsBoundaryCallBack : PagedList.BoundaryCallback<MinimalCoinsModel>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MinimalCoinsModel) {
        GlobalScope.launch(Dispatchers.IO) {
            CoinsRepository.itemAtEndLoaded(itemAtEnd, "30", "USD")
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: MinimalCoinsModel) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}