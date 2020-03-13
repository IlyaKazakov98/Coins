package com.readyfo.coins.view.fragment.viewpagerfragments


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import com.readyfo.coins.App
import com.readyfo.coins.Common
import com.readyfo.coins.R
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.viewmodel.DetailedInfoViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detailed_info.*

class DetailedInfoFragment : Fragment() {
    private var args: Bundle? = null
    private val detailedInfoViewModel: DetailedInfoViewModel by viewModels(
        factoryProducer = { SavedStateViewModelFactory(App(), this) }
    )

    // Создаётся экземпляр фрагмента
    companion object {
        fun newInstance(coinId: Int, favoritesId: Int): DetailedInfoFragment {
            val args = Bundle().apply {
                putInt("coinId", coinId)
                putInt("favoritesId", favoritesId)
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
        return inflater.inflate(R.layout.fragment_detailed_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = arguments
        detailedInfoViewModel.saveCurrentCoinId(args?.getInt("coinId"))
        detailedInfoViewModel.getDetailedInfo().observe(viewLifecycleOwner, Observer {
            refreshUI(it)
        })

        detailedFavoritesIcon.setImageResource(
            if (args?.getInt("favoritesId") == 0) {
                R.drawable.ic_favorites_false_24dp
            } else {
                R.drawable.ic_favorites_true_24dp
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun refreshUI(coinModel: CoinsModel) {

        detailedCoinSymbol.text = coinModel.symbol
        detailedCoinName.text = coinModel.name
        detailedPriceUSD.text = stringFormat(coinModel.quote?.USD?.price)

        detailedOneHourChange.text = "${stringFormat(coinModel.quote?.USD?.percent_change_1h)} %"
        setColorText(detailedOneHourChange, "${coinModel.quote?.USD?.percent_change_1h}")

        detailedTwentyFourHourChange.text =
            "${stringFormat(coinModel.quote?.USD?.percent_change_24h)}%"
        setColorText(detailedTwentyFourHourChange, "${coinModel.quote?.USD?.percent_change_24h}")

        detailedSevenDaysChange.text = "${stringFormat(coinModel.quote?.USD?.percent_change_7d)}%"
        setColorText(detailedSevenDaysChange, "${coinModel.quote?.USD?.percent_change_7d}")

        detailedVolTwentyFourHourChange.text = stringFormat(coinModel.quote?.USD?.volume_24h)

        detailedMarketCap.text = stringFormat(coinModel.quote?.USD?.market_cap)

        Picasso.get()
            .load(
                StringBuilder(Common.imageUrl)
                    .append(coinModel.symbol?.toLowerCase())
                    .append(".png")
                    .toString()
            )
            .into(detailedCoinIcon)

    }

    private fun setColorText(tv: TextView, text: String) {
        tv.setTextColor(
            if (text.contains("-"))
                Color.parseColor("#d94040")
            else Color.parseColor("#009e73")
        )
    }

    private fun stringFormat(value: Double?) = String.format("%.2f", value)
}
