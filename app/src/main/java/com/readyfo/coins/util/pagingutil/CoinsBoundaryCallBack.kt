package com.readyfo.coins.util.pagingutil

import androidx.paging.PagedList
import com.readyfo.coins.model.CoinsModel

class CoinsBoundaryCallBack(): PagedList.BoundaryCallback<CoinsModel>() {

    // val helper = PagingRequestHelper(ioExecutor)
    // val networkState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: CoinsModel) {
        super.onItemAtEndLoaded(itemAtEnd)
    }

    override fun onItemAtFrontLoaded(itemAtFront: CoinsModel) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}