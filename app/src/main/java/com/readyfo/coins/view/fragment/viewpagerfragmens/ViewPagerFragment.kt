package com.readyfo.coins.view.fragment.viewpagerfragmens


import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

import com.readyfo.coins.R
import kotlinx.android.synthetic.main.fragment_view_pager.*

class ViewPagerFragment(val context: Activity, private val coinId: Int, private val favoritesId: Int) : Fragment() {
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        retainInstance = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = PagerAdapter(
            childFragmentManager,
            coinId,
            favoritesId
        )
        viewPager = pager
        viewPager.adapter = pagerAdapter
    }


    class PagerAdapter(
        fm: FragmentManager,
        private val coinId: Int,
        private val favoritesId: Int
    ) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Detailed Info"
                else -> "Global Metrics"
            }
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> DetailedInfoFragment.newInstance(coinId, favoritesId)
                else -> GlobalMetricsFragment.newInstance()
            }
        }
    }
}
