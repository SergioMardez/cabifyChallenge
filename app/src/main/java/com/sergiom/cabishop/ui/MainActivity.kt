package com.sergiom.cabishop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergiom.cabishop.R
import com.sergiom.cabishop.ui.shopview.ShopFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ShopFragment())
                .commitNow()
        }
    }
}