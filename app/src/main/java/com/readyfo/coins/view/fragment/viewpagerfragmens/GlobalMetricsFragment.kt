package com.readyfo.coins.view.fragment.viewpagerfragmens


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.readyfo.coins.Common

import com.readyfo.coins.R
import com.readyfo.coins.model.GlobalMetricsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_global_metrics.*

class GlobalMetricsFragment : Fragment() {

    // Создаётся экземпляр фрагмента
    companion object {
        fun newInstance(globalMetrics: GlobalMetricsModel?): GlobalMetricsFragment {
            Log.d("CoinsLog", "GlobalMetricsModel: $globalMetrics")
            val args = Bundle().apply {
                putInt("active_cryptocurrencies", globalMetrics?.active_cryptocurrencies!!)
                putInt("active_market_pairs", globalMetrics.active_market_pairs)
                putDouble("total_market_cap", globalMetrics.quote.uSD.total_market_cap)
                putDouble("total_volume_24h", globalMetrics.quote.uSD.total_volume_24h)
                putDouble("btc_dominance", globalMetrics.btc_dominance)
                putDouble("eth_dominance", globalMetrics.eth_dominance)
            }

            val fragment = GlobalMetricsFragment()
            fragment.arguments = args
            return fragment
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

        // Записываем полученные данные в соответствующие им поля
        val args = arguments
        cryptocurrenciesGM.text = args?.getInt("active_cryptocurrencies").toString()
        marketsGM.text = args?.getInt("active_market_pairs").toString()
        marketCapGM.text = args?.getDouble("total_market_cap").toString()
        volumeTwentyFourHourGM.text = args?.getDouble("total_volume_24h").toString()
        btcDominanceGM.text = args?.getDouble("btc_dominance").toString()
        ethDominanceGM.text = args?.getDouble("eth_dominance").toString()

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
            .into(coinIconBTCGM)

        super.onViewCreated(view, savedInstanceState)
    }
}
