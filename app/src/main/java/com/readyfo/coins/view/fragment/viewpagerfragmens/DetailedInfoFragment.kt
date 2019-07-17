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
import com.readyfo.coins.model.CoinsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detailed_info.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailedInfoFragment : Fragment() {

    // Создаётся экземпляр фрагмента
    companion object {
        fun newInstance(coin: CoinsModel?): DetailedInfoFragment {
            Log.d("CoinsLog", "TabModel: $coin")
            val args = Bundle().apply {
                putString("symbol", coin?.symbol)
                putString("name", coin?.name)
                putDouble("price", coin?.quote?.USD?.price!!)
                putDouble("volume_24h", coin.quote.USD.volume_24h!!)
                //putDouble("percent_change_1h", coin.quote.USD.percent_change_1h!!)
                putDouble("percent_change_24h", coin.quote.USD.percent_change_24h!!)
                putDouble("percent_change_7d", coin.quote.USD.percent_change_7d!!)
                putDouble("market_cap", coin.quote.USD.market_cap!!)
                putString("last_updated", coin.quote.USD.last_updated!!)
            }

            val fragment = DetailedInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detailed_info, container, false)
        retainInstance = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Записываем полученные данные в соответствующие им поля
        val args = arguments
        tabCoinSymbol.text = args?.getString("symbol")
        tabCoinName.text = args?.getString("name")
        tabPriceUSD.text = "${args?.getDouble("price")}"

        tabOneHourChange.text = "${args?.getDouble("percent_change_1h")}%"
        setColorText(tabOneHourChange, "${args?.getDouble("percent_change_1h")}")

        tabVolumeTwentyFourHourChange.text = "${args?.getDouble("volume_24h")}"

        tabPercentTwentyFourHourChange.text = "${args?.getDouble("percent_change_24h")}%"
        setColorText(tabPercentTwentyFourHourChange, "${args?.getDouble("percent_change_24h")}")

        tabPercentSevenDaysChange.text = "${args?.getDouble("percent_change_7d")}%"
        setColorText(tabPercentSevenDaysChange, "${args?.getDouble("percent_change_7d")}")

        tabMarketCap.text = "${args?.getDouble("market_cap")}"
        tabLastUpDated.text = "${args?.getString("last_updated")}"

        Picasso.get()
            .load(StringBuilder(Common.imageUrl)
                .append(args?.getString("symbol")?.toLowerCase())
                .append(".png")
                .toString())
            .into(tabCoinIcon)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setColorText(tv: TextView, text: String){
        tv.setTextColor(
            if (text.contains("-"))
                Color.parseColor("#d94040")
            else Color.parseColor("#009e73"))
    }
}
