package com.okatanaa.timemanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.okatanaa.timemanager.adapter.WeekRecycleAdapter
import com.okatanaa.timemanager.services.DataService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: WeekRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.adapter = WeekRecycleAdapter(this, DataService.week)
        weekRecycleView.adapter = this.adapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        weekRecycleView.layoutManager = layoutManager
        weekRecycleView.setHasFixedSize(true)
    }
}
