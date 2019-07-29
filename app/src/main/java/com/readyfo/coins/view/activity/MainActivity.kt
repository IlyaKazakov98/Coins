package com.readyfo.coins.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.readyfo.coins.R
import com.readyfo.coins.adapter.CoinsAdapter
import com.readyfo.coins.view.fragment.MainFragment
import com.readyfo.coins.view.fragment.viewpagerfragmens.ViewPagerFragment

class MainActivity : AppCompatActivity(), CoinsAdapter.ItemClickListener {
    // private val workManager: WorkManager = WorkManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Запускаем фрагмент главный фрагмент
        if (savedInstanceState == null) {
            addFragment(MainFragment(this), "main")
        }
    }

    // По нажатию на элемент RecyclerView передаюм данные во фрагмент, для ViewPaged
    override fun onClickItem(coinsId: Int, favoritesId: Int) {
        replaceFragment(ViewPagerFragment(this, coinsId, favoritesId), "viewPager")
    }

    private fun addFragment(fragment: androidx.fragment.app.Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, fragment, tag)
            .commitAllowingStateLoss()
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

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
