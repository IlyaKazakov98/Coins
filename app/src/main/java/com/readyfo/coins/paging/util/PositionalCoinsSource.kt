package com.readyfo.coins.paging.util

//class PositionalCoinsSource: PositionalDataSource<CoinsModel>() {
//    private val computeCount = 30
//    // private lateinit var coinsRepository: CoinsRepository
//
//    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CoinsModel>) {
//        // val result: MutableList<CoinsModel> = coinsRepository.loadRangeCoins(params.startPosition, params.loadSize)
//        // callback.onResult(result)
//    }
//
//    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<CoinsModel>) {
//        GlobalScope.launch {
//            Log.d("CoinsLogLoad1", "${params.requestedStartPosition} ${params.requestedLoadSize}")
//            val totalCount = computeCount
//            val position = computeInitialLoadPosition(params, totalCount)
//            val loadSize = computeInitialLoadSize(params, position, totalCount)
//            callback.onResult(getCoins(params.requestedStartPosition, params.requestedLoadSize), position, loadSize)
//        }
//    }
//
//    private suspend fun getCoins(requestedStartPosition: Int, requestedLoadSize: Int) = CoinsRepository.getCoins(requestedStartPosition, requestedLoadSize)
    //private suspend fun load (requestedStartPosition: Int, requestedLoadSize: Int) = CoinsRepository.getCoins(requestedStartPosition, requestedLoadSize)
//}