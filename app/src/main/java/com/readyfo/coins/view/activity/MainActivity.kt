package com.readyfo.coins.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.work.*
import com.readyfo.coins.R
import com.readyfo.coins.adapter.CoinsAdapter
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.model.GlobalMetricsQuote
import com.readyfo.coins.model.GlobalMetricsUSD
import com.readyfo.coins.view.fragment.MainFragment
import com.readyfo.coins.view.fragment.PreviewFragment
import com.readyfo.coins.view.fragment.viewpagerfragmens.ViewPagerFragment
import com.readyfo.coins.viewmodel.CoinsViewModel
import com.readyfo.coins.workmanager.UpLoadWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), CoinsAdapter.ItemClickListener {
    private var coinsViewModel: CoinsViewModel? = null
    private val workManager: WorkManager = WorkManager.getInstance()
    private val stub = GlobalMetricsModel(btc_dominance = 63.3, eth_dominance = 9.6, active_cryptocurrencies = 2322, active_market_pairs = 19121, active_exchanges = 0, quote = GlobalMetricsQuote(
        GlobalMetricsUSD(total_market_cap = 351790429658.0, total_volume_24h = 78129053277.0)
    ))
    private var previewTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager.enqueueUniquePeriodicWork("network_update", ExistingPeriodicWorkPolicy.KEEP, applyFonWork())

        // Запускаем фрагмент превью на 2 секунд, после удаляем
        if (!previewTrue) {
            if (savedInstanceState == null) {
                val handler = Handler()
                addFragment(PreviewFragment(), "preview")
                previewTrue = true
                handler.postDelayed({
                    removeFragment("preview")
                    addFragment(MainFragment(this), "main")
                }, 2000)
            }
        }
    }

    // По нажатию на элемент RecyclerView передаюм данные во фрагмент, для ViewPaged
    override fun onClickItem(coinsModel: CoinsModel) {
        val localGM =
            if (coinsViewModel?.getGlobalMetrics()?.value != null)
                coinsViewModel?.getGlobalMetrics()?.value
        else stub
        Log.d("CoinsLog", "ModelInActivity: $coinsModel")
        replaceFragment(ViewPagerFragment(this, coinsModel, localGM), "viewPager")
    }

    // Строится запрос на фоновое обновления данных(раз в час)
    private fun applyFonWork(): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return PeriodicWorkRequest.Builder(UpLoadWorker::class.java, 60, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
    }

    // Добавление фрагмента
    private fun addFragment(fragment: androidx.fragment.app.Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, fragment, tag)
            .commitAllowingStateLoss()
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
    // Удаление фрагмента
    private fun removeFragment(tagFragment: String) {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}
