package com.readyfo.coins.view.fragment.viewpagerfragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.readyfo.coins.Common

import com.readyfo.coins.R
import com.readyfo.coins.model.GlobalMetricsModel
import com.readyfo.coins.viewmodel.GlobalMetricsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_global_metrics.*

class GlobalMetricsFragment : Fragment() {
    private lateinit var globalMetricsViewModel: GlobalMetricsViewModel

    // Создаётся экземпляр фрагмента
    companion object {
        fun newInstance(): GlobalMetricsFragment {
            return GlobalMetricsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_global_metrics, container, false)
        retainInstance = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        globalMetricsViewModel = ViewModelProviders.of(this).get(GlobalMetricsViewModel::class.java)
        globalMetricsViewModel.init()
        globalMetricsViewModel.getGlobalMetrics().observe(this, Observer {
            it?.let {
                refreshUI(it)
            }
        })
    }

    private fun refreshUI(globalMetricsModel: GlobalMetricsModel){
        cryptocurrenciesGM.text = globalMetricsModel.active_cryptocurrencies.toString()
        marketsGM.text = globalMetricsModel.active_market_pairs.toString()
        marketCapGM.text = "$ ${stringFormat(globalMetricsModel.quote.USD.total_market_cap)}"
        volumeTwentyFourHourGM.text = "$ ${stringFormat(globalMetricsModel.quote.USD.total_volume_24h)}"
        btcDominanceGM.text = stringFormat(globalMetricsModel.btc_dominance)
        ethDominanceGM.text = stringFormat(globalMetricsModel.eth_dominance)

        Picasso.get()
            .load(StringBuilder(Common.imageUrl)
                .append("btc")
                .append(".png")
                .toString())
            .into(coinIconBTCGM)

        Picasso.get()
            .load(StringBuilder(Common.imageUrl)
                .append("eth")
                .append(".png")
                .toString())
            .into(coinIconETHGM)
    }

    private fun stringFormat(value: Double?) = String.format("%.2f", value)
}
