package com.readyfo.coins.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.readyfo.coins.repository.CoinsRepository

//
class UpLoadWorker(context: Context, parameters: WorkerParameters): Worker(context, parameters) {

    override fun doWork(): Result {
        return try {
            CoinsRepository.updateCoinsRepo()
            Log.d("CoinsLog", "StartWork")
            Result.success()
        }
        catch (throwable: Throwable){
            Log.e("CoinsErrorLog", "$throwable")
            Result.failure()
        }
    }
}