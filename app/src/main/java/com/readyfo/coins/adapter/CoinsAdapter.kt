package com.readyfo.coins.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.readyfo.coins.R
import com.readyfo.coins.model.CoinsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*


private var imageUrl = "https://res.cloudinary.com/dxi90ksom/image/upload/"

class CoinsAdapter(internal var context: Context): PagedListAdapter<CoinsModel, CoinsAdapter.CoinsViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    // fun getItemAt(position: Int): CoinsModel = getItem(position)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder =
        CoinsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.coin_layout, parent, false))

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CoinsModel>(){
            override fun areItemsTheSame(oldItem: CoinsModel, newItem: CoinsModel): Boolean = oldItem.localId == newItem.localId

            override fun areContentsTheSame(oldItem: CoinsModel, newItem: CoinsModel): Boolean = oldItem == newItem
        }
    }

    inner class CoinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // look up the view
        private var coinsIcon = itemView.coinIcon
        private var coinSymbol = itemView.coinSymbol
        private var coinName = itemView.coinName
        private var coinPrice = itemView.priceUSD
        private var oneHourChange = itemView.oneHourChange

        // Create data
        private var coinsModel: CoinsModel? = null

        /**
         * Bind data
         */
        fun bindTo(coins: CoinsModel?) {
            this.coinsModel = coins
            // set the text
            coinSymbol.text = coins?.symbol
            coinName.text = coins?.name
            coinPrice.text = String.format("%.6g%n", coins?.quote?.USD?.price)
            oneHourChange.text = "${coins?.quote?.USD?.percent_change_1h} %"

            Picasso.get()
                .load(StringBuilder(imageUrl)
                    .append(coins?.symbol?.toLowerCase())
                    .append(".png")
                    .toString())
                .into(coinsIcon)

            oneHourChange.setTextColor(
                if ("${coins?.quote?.USD?.percent_change_1h}".contains("-"))
                    Color.parseColor("#d94040")
                else Color.parseColor("#009e73")
            )
        }
    }
}

