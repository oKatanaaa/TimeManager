package com.okatanaa.timemanager.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.adapter.DayListAdapter
import com.okatanaa.timemanager.adapter.WeekRecycleAdapter
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var adapter: WeekRecycleAdapter
    lateinit var modifiingEvent: Event
    lateinit var modifiingAdapter: DayListAdapter
    lateinit var week: Week

    // This variable is inner class.
    val outActivity = this

    // Data for move buttons
    lateinit var listView: AdapterView<DayListAdapter>
    var eventPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Read week from json
        println("Read week")
        this.week = JsonHelper.readFirstWeekFromJson(JsonHelper.readJSON(this))
        setWeekRecycleAdapter()
        setMoveButtons()
    }

    fun setWeekRecycleAdapter() {
        this.adapter = WeekRecycleAdapter(this, this.week,
            // Create lambda for the starting EventActivity
            { event : Event, outerAdaper: DayListAdapter ->
                CommonListener().onClickedEvent(event, outerAdaper)
            },
            // Create lambda for the adding new empty event
            { day : Day ->
                CommonListener().onClickedAddEventBtn(day)
            },
            { parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long ->
                CommonListener().onLongClickedEvent(parent, view, position, id)
            })

        weekRecycleView.adapter = this.adapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        weekRecycleView.layoutManager = layoutManager
        weekRecycleView.setHasFixedSize(true)
    }

    fun setMoveButtons() {
        moveUpBtn.alpha = 0F
        moveUpBtn.isEnabled = false
        moveDoneBtn.alpha = 0F
        moveDoneBtn.isEnabled = false
        moveDownBtn.alpha = 0F
        moveDownBtn.isEnabled = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED)
            return

        if(data?.getStringExtra(EXTRA_ACTION) == ACTION_SAVE) {
            // Receive changed event
            val changedEventJsonString = data?.getStringExtra(EXTRA_EVENT_JSON)
            val changedEventJson = JSONObject(changedEventJsonString)
            val changedEvent = JsonHelper.eventFromJson(changedEventJson)
            // Change picked event data to received data
            this.modifiingEvent.copy(changedEvent)
        }

        if(data?.getStringExtra(EXTRA_ACTION) == ACTION_DELETE) {
            val position = this.modifiingEvent.inDay.getPosition(this.modifiingEvent)
            this.modifiingAdapter.day.deleteEvent(position)
            this.modifiingAdapter.notifyDataSetChanged()
        }

        this.adapter.notifyDataSetChanged()
        val scrollPosition = findViewById<RecyclerView>(R.id.weekRecycleView).scrollState
        weekRecycleView.layoutManager?.scrollHorizontallyBy(scrollPosition, weekRecycleView.Recycler(), RecyclerView.State())
        println("DONE")
    }

    override fun onPause() {
        super.onPause()
        saveData()
        println("onPause")
    }

    fun onClickedMoveUpBtn(view: View) {

    }

    fun onClickedMoveDoneBtn(view: View) {
        this.listView.getChildAt(this.eventPosition).isSelected = false
        setMoveButtons()
    }

    fun onClickedMoveDownBtn(view: View) {

    }

    fun saveData() {
        val json = JSONObject()
        val jsonArray = JSONArray()
        jsonArray.put(JsonHelper.weekToJson(this.week))
        json.put(JSON_WEEKS, jsonArray)
        this.openFileOutput(JSON_PRIMARY_DATA_WEEK_FILE, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }

    inner class CommonListener {
        fun onClickedEvent(event : Event, outerAdaper: DayListAdapter) {
            println("Event clicked!")
            modifiingEvent = event
            modifiingAdapter = outerAdaper
            val eventIntent = Intent(outActivity, EventActivity::class.java)
            val eventJson = JsonHelper.eventToJson(event)
            eventIntent.putExtra(EXTRA_EVENT_JSON, eventJson.toString())
            startActivityForResult(eventIntent, 0)
        }

        fun onClickedAddEventBtn(day : Day) {
            println("Add event!")
            val newEmptyEvent = Event("Empty event")
            day.addEvent(newEmptyEvent)

            outActivity.adapter.notifyDataSetChanged()
            Toast.makeText(outActivity, "Event added", Toast.LENGTH_SHORT).show()
        }

        fun onLongClickedEvent(parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long): Boolean {
            listView = parent
            eventPosition = position

            parent.getChildAt(position).isSelected = true

            moveUpBtn.alpha = 1F
            moveUpBtn.isEnabled = true
            moveDoneBtn.alpha = 1F
            moveDoneBtn.isEnabled = true
            moveDownBtn.alpha = 1F
            moveDownBtn.isEnabled = true

            val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade)

            moveUpBtn.startAnimation(animation)
            moveDoneBtn.startAnimation(animation)
            moveDownBtn.startAnimation(animation)

            return true
        }

    }

}
