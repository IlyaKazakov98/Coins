package com.readyfo.coins.view.fragment


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.readyfo.coins.R
import com.readyfo.coins.view.adapter.CoinsAdapter
import com.readyfo.coins.view.adapter.SimpleItemTouchHelperCallback
import com.readyfo.coins.viewmodel.CoinsViewModel
import kotlinx.android.synthetic.main.activity_main.coinRecyclerView
import kotlinx.android.synthetic.main.activity_main.swipeToRefresh
import kotlinx.android.synthetic.main.coin_layout.view.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private val layout = R.menu.main_menu
    private val coinsViewModel: CoinsViewModel by viewModels()
    private val adapter by lazy {
        CoinsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = toolbarInFrag as Toolbar
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        // Создаём Адаптер и привязываем его к RecyclerView
        coinRecyclerView.layoutManager = LinearLayoutManager(activity)
        coinRecyclerView.adapter = adapter

        // Инициализируем загрузку данных в LiveData.
        coinsViewModel.init()
        // Подписываемся на изменение данных
        coinsViewModel.coins.observe(viewLifecycleOwner, Observer {
            // Обновляем UI(передаём PagedList в адаптер)
            adapter.apply {
                submitList(it)
                notifyDataSetChanged()
            }
            coinRecyclerView.scrollToPosition(0)
        })

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(coinRecyclerView)

        // Полное обновление данных
        swipeToRefresh.setOnRefreshListener {
            update()
        }
    }

    // Привязываем меню к фрагменту
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(layout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Обрабатываем нажатия на пункты меню сортировки и передаём в качестве параметра имя столбца по которому будем сортировать
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSortByPrice -> coinsViewModel.sortBy(0)
            R.id.menuSortByPercent -> coinsViewModel.sortBy(1)
        }
        return super.onOptionsItemSelected(item)
    }

    // Реагируем на жест пользователя
    private val swipeHandler by lazy {
        object : SimpleItemTouchHelperCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    // Добавляем в избранное
                    LEFT -> {
                        coinsViewModel.setFavorites(
                            viewHolder.itemView.coinSymbol.text.toString(),
                            1
                        )
                    }
                    // Удаляем из избранного
                    RIGHT -> {
                        coinsViewModel.setFavorites(
                            viewHolder.itemView.coinSymbol.text.toString(),
                            0
                        )
                    }
                }
            }
        }
    }

    // Обновляем данные по просьбе пользователя
    private fun update() {
        swipeToRefresh.isRefreshing = true
        coinsViewModel.updateCoins(true)
        swipeToRefresh.isRefreshing = false
    }
}
