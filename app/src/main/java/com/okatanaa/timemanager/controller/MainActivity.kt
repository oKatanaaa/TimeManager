package com.okatanaa.timemanager.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.adapter.DayListAdapter
import com.okatanaa.timemanager.adapter.WeekListAdapter
import com.okatanaa.timemanager.adapter.WeekRecycleAdapter
import com.okatanaa.timemanager.additional_classes.TextClickedListener
import com.okatanaa.timemanager.interfaces.CurrentEventChangedListener
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.interfaces.OnEventLongClickListener
import com.okatanaa.timemanager.interfaces.OnWeekClickListener
import com.okatanaa.timemanager.model.CalendarSynchronizer
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.DataService
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.app_bar_test.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.IllegalStateException

class MainActivity : AppCompatActivity(), OnEventClickListener, CurrentEventChangedListener, OnWeekClickListener,
    OnEventLongClickListener{
    // Permanent data
    lateinit var weekAdapter: WeekRecycleAdapter
    lateinit var week: Week
    var currentWeekPosition: Int = 0
    lateinit var calendarSynchronizer: CalendarSynchronizer
    lateinit var weekListAdapter: WeekListAdapter

    // Data for modifying events
    lateinit var modifyingEvent: Event
    var modifyingEventPosition: Int = 0
    lateinit var modifyingAdapter: DayListAdapter

    // Data for move buttons
    lateinit var listView: AdapterView<DayListAdapter>
    var eventPosition: Int = 0
    lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        DataService.weekArray = JsonHelper.readWeekArr(JsonHelper.readJSON(this))
        DataService.currentWeek = DataService.weekArray[0]
        this.weekListAdapter = WeekListAdapter(this, DataService.weekArray, this)
        week_list_view.adapter = this.weekListAdapter

        this.week = DataService.currentWeek
        this.handler = Handler() {
            val  dayPosition = it.what.toInt()
            this.weekAdapter?.notifyItemChanged(dayPosition)
            true
        }
        this.calendarSynchronizer = CalendarSynchronizer(this.week, this, this.handler)

        setWeekRecycleAdapter()
        setMoveButtons()
        weekRecycleView.scrollToPosition(this.calendarSynchronizer.currentWeekDayNum)
    }

    fun reloadData() {
        this.week = DataService.currentWeek
        this.calendarSynchronizer.stopSynchronizingThread()
        this.calendarSynchronizer = CalendarSynchronizer(this.week, this, this.handler)
        setWeekRecycleAdapter()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            R.id.action_delete_week -> {
                if (DataService.weekArray.size == 1) {
                    Toast.makeText(this, "Must be at least one week!", Toast.LENGTH_SHORT).show()
                    return false
                }
                if (this.currentWeekPosition == DataService.weekArray.size - 1)
                    this.currentWeekPosition = this.currentWeekPosition - 1

                DataService.weekArray.removeAt(this.currentWeekPosition)
                DataService.currentWeek = DataService.weekArray[currentWeekPosition]
                weekListAdapter.notifyDataSetChanged()
                reloadData()
                Toast.makeText(this, "Week deleted!", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_rename_week -> {
                TextClickedListener.onClick(this, WEEK_NAME, week.name)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun setWeekRecycleAdapter() {
        this.weekAdapter = WeekRecycleAdapter(
            this, this.week, this, this)

        weekRecycleView.adapter = this.weekAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        weekRecycleView.layoutManager = layoutManager
        weekRecycleView.setHasFixedSize(true)
        (weekRecycleView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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
        if (resultCode == Activity.RESULT_CANCELED)
            return
        when (requestCode) {
            RC_EVENT_ACTIVITY -> {
                if (data?.getStringExtra(EXTRA_ACTION) == ACTION_SAVE) {
                    // Receive changed event
                    val changedEventJsonString = data?.getStringExtra(EXTRA_EVENT_JSON)
                    val changedEventJson = JSONObject(changedEventJsonString)
                    val changedEvent = JsonHelper.eventFromJson(changedEventJson)
                    // Change picked event data to received data
                    this.modifyingEvent.copy(changedEvent)
                    this.modifyingAdapter.notifyDataSetChanged()
                }

                if (data?.getStringExtra(EXTRA_ACTION) == ACTION_DELETE) {
                    synchronized(this.modifyingAdapter.day) {
                        this.modifyingAdapter.day.deleteEvent(this.modifyingEventPosition)
                    }
                    this.modifyingAdapter.notifyDataSetChanged()
                }

                this.weekAdapter.notifyDataSetChanged()
                val scrollPosition = findViewById<RecyclerView>(R.id.weekRecycleView).scrollState
                weekRecycleView.layoutManager?.scrollHorizontallyBy(
                    scrollPosition,
                    weekRecycleView.Recycler(),
                    RecyclerView.State()
                )
                println("DONE")
            }
            RC_TEXT_EDITOR_ACTIVITY -> {
                val newWeekName = data?.getStringExtra(EXTRA_EDITED_VALUE)
                this.week.name = newWeekName!!
                this.weekListAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
        println("onPause")
    }

    fun saveData() {
        val json = JSONObject()
        val jsonArray = JSONArray()
        for (i in 0 until DataService.weekArray.count()) {
            jsonArray.put(JsonHelper.weekToJson(DataService.weekArray[i]))
        }
        json.put(JSON_WEEKS, jsonArray)
        this.openFileOutput(JSON_PRIMARY_DATA_WEEK_FILE, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }


    /// LISTEN FUNCTIONS


    // This listen function is for drawer layout where the weeks are
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onClickedMoveUpBtn(view: View) {
        this.listView.adapter.removeSelectedView(this.eventPosition)
        synchronized(this.listView.adapter.day) {
            if (this.listView.adapter.day.moveEventUp(this.eventPosition)) {
                this.eventPosition = this.eventPosition - 1
            }
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
        synchronized(this.listView.adapter.day) {
            if (this.listView.adapter.day.moveEventDown(this.eventPosition)) {
                this.eventPosition = this.eventPosition + 1
            }
        }
        this.listView.adapter.addSelectedView(this.eventPosition)
        this.listView.adapter.notifyDataSetChanged()
    }

    override fun onEventClicked(event: Event, adapter: DayListAdapter, position: Int) {
        println("Event clicked!")
        this.modifyingEvent = event
        this.modifyingEventPosition = position
        this.modifyingAdapter = adapter

        var topTimeBorder: Int = 0
        var bottomTimeBorder: Int = Time.MINUTES_IN_DAY

        // There is the only event in day
        if (position == 0 && adapter.count == 1) {
            topTimeBorder = 0
            bottomTimeBorder = Time.MINUTES_IN_DAY
        }
        // This event is first, but there is other events
        else if (position == 0 && adapter.count > 1) {
            val belowEvent = adapter.getItem(position + 1) as Event
            topTimeBorder = 0
            bottomTimeBorder = belowEvent.startTime.toMinutes()
        }
        // There are many events and this event is somewhere in the middle of list
        else if (position != 0 && position < adapter.count - 1) {
            val aboveEvent = adapter.getItem(position - 1) as Event
            val belowEvent = adapter.getItem(position + 1) as Event
            topTimeBorder = aboveEvent.endTime.toMinutes()
            bottomTimeBorder = belowEvent.startTime.toMinutes()
        }
        // There are many events and this event is the last one
        else if (position == adapter.count - 1) {
            val aboveEvent = adapter.getItem(position - 1) as Event
            topTimeBorder = aboveEvent.endTime.toMinutes()
            bottomTimeBorder = Time.MINUTES_IN_DAY
        }

        val eventIntent = Intent(this@MainActivity, EventActivity::class.java)
        val eventJson = JsonHelper.eventToJson(event)

        eventIntent.putExtra(EXTRA_EVENT_JSON, eventJson.toString())
        eventIntent.putExtra(EXTRA_TOP_TIME_BORDER, topTimeBorder)
        eventIntent.putExtra(EXTRA_BOTTOM_TIME_BORDER, bottomTimeBorder)
        startActivityForResult(eventIntent, RC_EVENT_ACTIVITY)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onWeekClicked(week: Week, adapter: WeekListAdapter, position: Int) {
        DataService.currentWeek = week
        this.currentWeekPosition = position
        this.weekListAdapter.notifyDataSetChanged()
        reloadData()
        Toast.makeText(this, "Week clicked!", Toast.LENGTH_SHORT).show()
    }

    fun addWeekBtnClicked(view: View) {
        DataService.weekArray.add(Week("Week ${this.weekListAdapter.count + 1}"))
        this.weekListAdapter.notifyDataSetChanged()
    }

    override fun currentEventChanged(dayPosition: Int) {
        try {
            // Safe call is here because it can be not initialized yet
            if(this.weekAdapter != null)
                this.weekAdapter?.notifyItemChanged(dayPosition)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }


    override fun onEventLongClicked(parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long): Boolean {
        this@MainActivity.listView = parent
        this@MainActivity.eventPosition = position

        if (parent.adapter.getSelectedViewsCount() > 0)
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


    companion object {
        const val WEEK_NAME = "Week name"
    }

}
