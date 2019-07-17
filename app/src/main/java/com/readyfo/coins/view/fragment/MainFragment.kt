package com.readyfo.coins.view.fragment


import android.app.Activity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.readyfo.coins.adapter.CoinsAdapter
import com.readyfo.coins.viewmodel.CoinsViewModel
import kotlinx.android.synthetic.main.activity_main.coinRecyclerView
import kotlinx.android.synthetic.main.activity_main.swipeToRefresh
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.readyfo.coins.R
import com.readyfo.coins.adapter.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.coin_layout.view.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment(val context: Activity) : Fragment() {
    private lateinit var coinsViewModel: CoinsViewModel
    private var adapter = CoinsAdapter(context)
    private var initBool = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        retainInstance = true
        setHasOptionsMenu(true)

        coinsViewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        // Инициализируем загрузку данных в LiveData. Если данные были загружены/обновленны, то до следующего
        // запуска приложения больше этого не делаем
        if(initBool){
            coinsViewModel.init()
            initBool = false
        }
        // Подписываемся на изменение данных
        coinsViewModel.getCoins().observe(this, Observer{
            // Обновляем UI(передаём PagedList в адаптер)
            adapter.submitList(it)
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = toolbarInFrag as Toolbar
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        // Создаём Адаптер и привязываем его к RecyclerView
        coinRecyclerView.layoutManager = LinearLayoutManager(activity)
        coinRecyclerView.adapter = adapter

        // Реагируем на жест пользователя
        val swipeHandler = object : SimpleItemTouchHelperCallback(context){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Взависимости от него, либо добавляем в избранное
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.submitList(coinsViewModel.setFavorites(viewHolder.itemView.coinSymbol.text.toString(), 1).value)
                }
                // Либо удаляем из избранного
                else if (direction == ItemTouchHelper.RIGHT)
                    adapter.submitList(coinsViewModel.setFavorites(viewHolder.itemView.coinSymbol.text.toString(), 0).value)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(coinRecyclerView)

        // Полное обновление данных
        swipeToRefresh.setOnRefreshListener {
            update()
        }
    }

    // Привязываем меню к фрагменту
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Обрабатываем нажатия на пункты меню сортировки и передаём в качестве параметра имя столбца по которому будем сортировать
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuSortByPrice -> sortBy(0)
            R.id.menuSortByPercent -> sortBy(1)
        }
        return super.onOptionsItemSelected(item)
    }

    // Получаем отсортированный PagedList и передаём его адаптеру
    private fun sortBy(value: Int){
        val resultSort = coinsViewModel.sortBy(value).value
        Log.d("CoinsLog", "Sort: $value")
        adapter.submitList(resultSort)
    }

    // Обрабатываем действия пользователя в поле поиска
    private fun initSearchView(menu: Menu){
        val searchViewMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchViewMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()){
                    val resultSearch = coinsViewModel.searchBy(query).value
                    Log.d("CoinsLog", "Search1: $resultSearch")
                    adapter.submitList(resultSearch)
                }
                Log.d("CoinsLog", "onQueryTextSubmit1: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()){
                    val resultSearch = coinsViewModel.searchBy(newText).value
                    Log.d("CoinsLog", "Search2: $resultSearch")
                    adapter.submitList(resultSearch)
                }
                Log.d("CoinsLog", "onQueryTextSubmit2: $newText")
                return true
            }
        })
    }

    // Обновляем данные по просьбе пользователя
    private fun update(){
        swipeToRefresh.isRefreshing = true
        Log.d("CoinsLog", "Swipe: Обновленно")
        GlobalScope.launch {
            coinsViewModel.updateCoins()
        }
        swipeToRefresh.isRefreshing = false
    }
}
