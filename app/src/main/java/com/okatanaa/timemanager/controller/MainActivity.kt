package com.okatanaa.timemanager.controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.adapter.WeekRecycleAdapter
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.utilities.EXTRA_EVENT
import com.okatanaa.timemanager.utilities.JSON_FILENAME
import com.okatanaa.timemanager.utilities.JSON_WEEKS
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.week_item.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: WeekRecycleAdapter
    lateinit var modifiingEvent: Event
    lateinit var week: Week

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Read week from json
        println("Read week")
        this.week = JsonHelper.readFirstWeekFromJson(JsonHelper.readJSON(this))
        this.adapter = WeekRecycleAdapter(this, this.week,
            // Create lambda for the starting EventActivity
            { event : Event ->
                println("Event clicked!")
                this.modifiingEvent = event
                val eventIntent = Intent(this, EventActivity::class.java)
                eventIntent.putExtra(EXTRA_EVENT, event)
                startActivityForResult(eventIntent, 0)
            },
            // Create lambda for the adding new empty event
            { day : Day ->
                println("Add event!")
                val newEmptyEvent = Event("Empty event")
                day.addEvent(newEmptyEvent)

                this.adapter.notifyDataSetChanged()
                Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show()
            })

        weekRecycleView.adapter = this.adapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        weekRecycleView.layoutManager = layoutManager
        weekRecycleView.setHasFixedSize(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Receive changed event
        val changedEvent = data?.getParcelableExtra(EXTRA_EVENT) as Event
        // Change picked event data to received data
        this.modifiingEvent.description = changedEvent.description
        this.modifiingEvent.title = changedEvent.title
        val scrollPosition = findViewById<RecyclerView>(R.id.weekRecycleView).scrollState

        this.adapter.notifyDataSetChanged()

        weekRecycleView.layoutManager?.scrollHorizontallyBy(scrollPosition, weekRecycleView.Recycler(), RecyclerView.State())
        println("DONE")
    }

    override fun onPause() {
        super.onPause()
        saveData()
        println("onPause")
    }

    fun saveData() {
        val json = JSONObject()
        val jsonArray = JSONArray()
        jsonArray.put(JsonHelper.weekToJson(this.week))
        json.put(JSON_WEEKS, jsonArray)
        this.openFileOutput(JSON_FILENAME, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }

}
