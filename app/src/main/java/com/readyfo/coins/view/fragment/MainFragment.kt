package com.readyfo.coins.view.fragment


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    private lateinit var coinsViewModel: CoinsViewModel
    private var adapter = CoinsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = toolbarInFrag as Toolbar
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        // Создаём Адаптер и привязываем его к RecyclerView
        coinRecyclerView.layoutManager = LinearLayoutManager(activity)
        coinRecyclerView.adapter = adapter

        coinsViewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        // Инициализируем загрузку данных в LiveData.
        coinsViewModel.init()
        // Подписываемся на изменение данных
        coinsViewModel.getCoins().observe(this, Observer{
            // Обновляем UI(передаём PagedList в адаптер)
            adapter.submitList(it)
        })

        // Реагируем на жест пользователя
        val swipeHandler = object : SimpleItemTouchHelperCallback(activity as Context){
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Обрабатываем нажатия на пункты меню сортировки и передаём в качестве параметра имя столбца по которому будем сортировать
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSortByPrice -> sortBy(0)
            R.id.menuSortByPercent -> sortBy(1)
        }
        return super.onOptionsItemSelected(item)
    }

    // Обрабатываем действия пользователя в поле поиска
    private fun initSearchView(menu: Menu){
        val searchViewMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchViewMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val resultSearch =
                    if (query != null || query != "")
                        query?.let { coinsViewModel.searchBy(it).value }
                    else
                        query.let { coinsViewModel.getCoins().value}
                adapter.submitList(resultSearch)
                Log.d("CoinsLog", "onQueryTextSubmitSearch: $resultSearch")
                Log.d("CoinsLog", "onQueryTextSubmitSearch: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val resultSearch =
                    if (newText != null || newText != "")
                        newText?.let { coinsViewModel.searchBy(it).value }
                    else
                        newText.let { coinsViewModel.getCoins().value}
                adapter.submitList(resultSearch)
                Log.d("CoinsLog", "onQueryTextChangeSearch: $resultSearch")
                Log.d("CoinsLog", "onQueryTextChangeSearch: $newText")
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

    // Получаем отсортированный PagedList и передаём его адаптеру
    private fun sortBy(value: Int){
        Log.d("CoinsLog", "Sort: $value")
        val res = coinsViewModel.sortBy(value).value
        adapter.submitList(res)
    }
}
