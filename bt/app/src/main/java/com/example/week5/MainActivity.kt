package com.example.week5

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = tabSlideAdapter(supportFragmentManager)
        viewpager.adapter = fragmentAdapter
        main_tab.setupWithViewPager(viewpager)

    }






}
