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
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.adapter.DayListAdapter
import com.okatanaa.timemanager.adapter.WeekRecycleAdapter
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), OnEventClickListener{

    override fun onEventClicked(event: Event, adapter: DayListAdapter, position: Int) {
        println("Event clicked!")
        this.modifyingEvent = event
        this.modifyingEventPosition = position
        this.modifyingAdapter = adapter
        val eventIntent = Intent(this@MainActivity, EventActivity::class.java)
        val eventJson = JsonHelper.eventToJson(event)
        eventIntent.putExtra(EXTRA_EVENT_JSON, eventJson.toString())
        startActivityForResult(eventIntent, 0)
    }


    lateinit var weekAdapter: WeekRecycleAdapter
    lateinit var modifyingEvent: Event
    var modifyingEventPosition: Int = 0
    lateinit var modifyingAdapter: DayListAdapter
    lateinit var week: Week

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
        this.weekAdapter = WeekRecycleAdapter(this, this.week, this
        ) { parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long ->
            CommonListener().onLongClickedEvent(parent, view, position, id)
        }

        weekRecycleView.adapter = this.weekAdapter

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
            this.modifyingEvent.copy(changedEvent)
        }

        if(data?.getStringExtra(EXTRA_ACTION) == ACTION_DELETE) {
            this.modifyingAdapter.day.deleteEvent(this.modifyingEventPosition)
            this.modifyingAdapter.notifyDataSetChanged()
        }

        this.weekAdapter.notifyDataSetChanged()
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
        this.listView.adapter.removeSelectedView(this.eventPosition)
        if(this.listView.adapter.day.moveEventUp(this.eventPosition)) {
            this.eventPosition = this.eventPosition - 1
        }
        this.listView.adapter.addSelectedView(this.eventPosition)
        this.listView.adapter.notifyDataSetChanged()
    }


    fun onClickedMoveDoneBtn(view: View) {
        this.listView.adapter.removeAllSelectedViews()
        this.listView.adapter.notifyDataSetChanged()
        setMoveButtons()
    }

    fun onClickedMoveDownBtn(view: View) {
        this.listView.adapter.removeSelectedView(this.eventPosition)
        if(this.listView.adapter.day.moveEventDown(this.eventPosition)) {
            this.eventPosition = this.eventPosition + 1
        }
        this.listView.adapter.addSelectedView(this.eventPosition)
        this.listView.adapter.notifyDataSetChanged()
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

        fun onLongClickedEvent(parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long): Boolean {
            this@MainActivity.listView = parent
            this@MainActivity.eventPosition = position

            if(parent.adapter.getSelectedViewsCount() > 0)
                parent.adapter.removeAllSelectedViews()

            parent.adapter.addSelectedView(position)
            parent.adapter.notifyDataSetChanged()


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
