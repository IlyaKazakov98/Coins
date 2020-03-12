package com.readyfo.coins.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.readyfo.coins.R
import com.readyfo.coins.view.adapter.CoinsAdapter
import com.readyfo.coins.view.fragment.MainFragment
import com.readyfo.coins.view.fragment.viewpagerfragments.ViewPagerFragment

class MainActivity : AppCompatActivity(), CoinsAdapter.ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Запускаем фрагмент главный фрагмент
        if (savedInstanceState == null) {
            addFragment(MainFragment(), "main")
        }
    }

    // По нажатию на элемент RecyclerView передаюм данные во фрагмент, для ViewPaged
    override fun onClickItem(coinsId: Int, favoritesId: Int) {
        replaceFragment(ViewPagerFragment(coinsId, favoritesId), "viewPager")
    }

    private fun addFragment(fragment: Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, fragment, tag)
            .commitAllowingStateLoss()
    }

    private fun replaceFragment(fragment: Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}
