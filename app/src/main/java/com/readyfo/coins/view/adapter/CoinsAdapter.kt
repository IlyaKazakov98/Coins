package com.readyfo.coins.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.readyfo.coins.Common
import com.readyfo.coins.R
import com.readyfo.coins.model.MinimalCoinsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*

class CoinsAdapter(): PagedListAdapter<MinimalCoinsModel, CoinsAdapter.CoinsViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    // fun getItemAt(position: Int): CoinsModel = getItem(position)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder =
        CoinsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.coin_layout, parent, false))

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MinimalCoinsModel>(){
            override fun areItemsTheSame(oldItem: MinimalCoinsModel, newItem: MinimalCoinsModel): Boolean = oldItem.coin_id == newItem.coin_id

            override fun areContentsTheSame(oldItem: MinimalCoinsModel, newItem: MinimalCoinsModel): Boolean = oldItem == newItem
        }
    }

    inner class CoinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val parentLayout = itemView.parentLayout
        private var coinsIcon = itemView.coinIcon
        private var favoritesIcon = itemView.favoritesIcon
        private var coinSymbol = itemView.coinSymbol
        private var coinName = itemView.coinName
        private var coinPrice = itemView.priceUSD
        private var oneHourChange = itemView.oneHourChange

        private var minimalCoinsModel: MinimalCoinsModel? = null

        /**
         * Bind data
         */
        fun bindTo(minimalCoinsModel1: MinimalCoinsModel?) {
            this.minimalCoinsModel = minimalCoinsModel1

            // Обрабатываем нажатия на элемент RecyclerView и передаём данные о нём, по цепочке, в ViewPager
            parentLayout.setOnClickListener{
                if (minimalCoinsModel1 != null) {
                    val listener = parentLayout.context as ItemClickListener
                    listener.onClickItem(minimalCoinsModel1.coin_id!!, minimalCoinsModel?.favorites?.favorites_id!!)
                }
            }

            // Записываем данные в coin_layout
            coinSymbol.text = minimalCoinsModel1?.symbol
            coinName.text = minimalCoinsModel1?.name
            coinPrice.text = stringFormat(minimalCoinsModel1?.quote?.USD?.price)
            oneHourChange.text = stringFormat(minimalCoinsModel1?.quote?.USD?.percent_change_1h)
            favoritesIcon.setImageResource(
                if (minimalCoinsModel?.favorites?.favorites_id == 0){
                    R.drawable.ic_favorites_false_24dp
                } else{
                    R.drawable.ic_favorites_true_24dp
                }
            )


            Picasso.get()
                .load(StringBuilder(Common.imageUrl)
                    .append(minimalCoinsModel1?.symbol?.toLowerCase())
                    .append(".png")
                    .toString())
                .into(coinsIcon)

            oneHourChange.setTextColor(
                if ("${minimalCoinsModel1?.quote?.USD?.percent_change_1h}".contains("-"))
                    Color.parseColor("#d94040")
                else Color.parseColor("#009e73")
            )
        }
    }

    private fun stringFormat(value: Double?) = String.format("%.2f", value)

    interface ItemClickListener {
        fun onClickItem(coinsId: Int, favoritesId: Int)
    }
}