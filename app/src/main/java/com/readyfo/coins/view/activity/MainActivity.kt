package com.readyfo.coins.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.readyfo.coins.R
import com.readyfo.coins.view.fragment.PreviewFragment

class MainActivity : AppCompatActivity() {

    private var previewTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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