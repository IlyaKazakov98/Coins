package com.readyfo.coins.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.paginate.Paginate
import com.readyfo.coins.R
import com.readyfo.coins.adapter.CoinsAdapter
import com.readyfo.coins.view.fragment.PreviewFragment
import com.readyfo.coins.viewmodel.CoinsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Paginate.Callbacks {

    private lateinit var coinsViewModel: CoinsViewModel
    private var previewTrue = false

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
        val adapter = CoinsAdapter(this)
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
            Log.d("CoinsLogSwipe", "Обновленно")
            // coinsViewModel.init()
        }
    }

    override fun onLoadMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLoading(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasLoadedAllItems(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
