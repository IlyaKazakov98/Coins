package com.readyfo.coins.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.readyfo.coins.Common
import com.readyfo.coins.R
import com.readyfo.coins.model.CoinsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*

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

        private val parentLayout = itemView.parentLayout
        private var coinsIcon = itemView.coinIcon
        private var favoritesIcon = itemView.favoritesIcon
        private var coinSymbol = itemView.coinSymbol
        private var coinName = itemView.coinName
        private var coinPrice = itemView.priceUSD
        private var oneHourChange = itemView.oneHourChange

        private var coinsModel: CoinsModel? = null

        /**
         * Bind data
         */
        fun bindTo(coinsModel: CoinsModel?) {
            this.coinsModel = coinsModel

            // Обрабатываем нажатия на элемент RecyclerView и передаём данные о нём, по цепочке, в ViewPager
            parentLayout.setOnClickListener{
                Log.d("CoinsLog", "ModelInAdapter: $coinsModel")

                if (coinsModel != null) {
                    val listener = context as ItemClickListener
                    listener.onClickItem(coinsModel)
                }
            }

            // Записываем данные в coin_layout
            coinSymbol.text = coinsModel?.symbol
            coinName.text = coinsModel?.name
            coinPrice.text = String.format("%.6g%n", coinsModel?.quote?.USD?.price)
            oneHourChange.text = "${coinsModel?.quote?.USD?.percent_change_1h} %"
            favoritesIcon.setImageResource(
                if (coinsModel?.favorites == 0){
                    R.drawable.ic_favorites_false_24dp
                } else{
                    R.drawable.ic_favorites_true_24dp
                }
            )


            Picasso.get()
                .load(StringBuilder(Common.imageUrl)
                    .append(coinsModel?.symbol?.toLowerCase())
                    .append(".png")
                    .toString())
                .into(coinsIcon)

            oneHourChange.setTextColor(
                if ("${coinsModel?.quote?.USD?.percent_change_1h}".contains("-"))
                    Color.parseColor("#d94040")
                else Color.parseColor("#009e73")
            )
        }
    }

    interface ItemClickListener {
        fun onClickItem(coinsModel: CoinsModel)
    }

}