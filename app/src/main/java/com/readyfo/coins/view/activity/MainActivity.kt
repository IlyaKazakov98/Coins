package com.readyfo.coins.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.readyfo.coins.R
import com.readyfo.coins.adapter.CoinsAdapter
import com.readyfo.coins.model.CoinsModel
import com.readyfo.coins.view.fragment.PreviewFragment
import com.readyfo.coins.viewmodel.CoinsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var coinsViewModel: CoinsViewModel
    private var previewTrue = false
    private val adapter = CoinsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coinRecyclerView.layoutManager = LinearLayoutManager(this)

        // Запускаем фрагмент превью на 2 секунд, после удаляем
        if (!previewTrue) {
            if (savedInstanceState == null) {
                val handler = Handler()
                addFragment(PreviewFragment(), "preview")
                previewTrue = true
                handler.postDelayed({removeFragment("preview")
                }, 2000)
            }
        }

        // Создаём Адаптер и привязываем его к RecyclerView
        coinRecyclerView.layoutManager = LinearLayoutManager(this)
        coinRecyclerView.adapter = adapter

        coinsViewModel = ViewModelProviders.of(this).get(CoinsViewModel::class.java)
        // Инициализируем загрузку данных в LiveData
        coinsViewModel.init()
        // Подписываемся на изменение данных
        coinsViewModel.getCoins().observe(this, Observer{
            // Обновляем UI(передаём PagedList в адаптер)
            adapter.submitList(it)
        })

        swipeToRefresh.setOnRefreshListener {
            update()
        }
    }

    // Привязываем меню к нашей активности
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        initSearchView(menu)
        return true
    }

    // Обрабатываем нажатия на пункты меню сортировки и передаём в качестве параметра имя столбца по которому будем сортировать
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuSortByPrice -> sortBy("price")
            R.id.menuSortByPercent -> sortBy("percent_change_1h")
        }
        return super.onOptionsItemSelected(item)
    }

    // Получаем отсортированный PagedList и передаём его адаптеру
    private fun sortBy(value: String){
        val resultSort = coinsViewModel.sortBy(value).value
        adapter.submitList(resultSort)
    }

    //
    private fun initSearchView(menu: Menu) {
        val searchViewMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchViewMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()){

                    //
                    val resultSearch = coinsViewModel.searchBy("$query%").value
                    Log.d("CoinsLog", "Search2: $resultSearch")
                    adapter.submitList(resultSearch)
                }
                else{}
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()){

                    //
                    val resultSearch = coinsViewModel.searchBy("$newText%").value
                    Log.d("CoinsLog", "Search2: $resultSearch")
                    adapter.submitList(resultSearch)
                }
                else{}
                Log.d("CoinsLog", "onQueryTextSubmit2: $newText")
                return false
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

    // Добавление фрагмента
    private fun addFragment(fragment: androidx.fragment.app.Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, fragment, tag)
            .commit()
    }

    // Удаление фрагмента
    private fun removeFragment(tagFragment: String) {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}
