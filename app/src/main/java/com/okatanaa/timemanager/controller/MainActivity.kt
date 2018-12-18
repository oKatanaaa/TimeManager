package com.okatanaa.timemanager.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.PagerAdapter
import android.support.v7.app.ActionBarDrawerToggle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.adapter.DayListAdapter
import com.okatanaa.timemanager.adapter.WeekListAdapter
import com.okatanaa.timemanager.additional_classes.TextClickedListener
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.interfaces.OnEventLongClickListener
import com.okatanaa.timemanager.interfaces.OnWeekClickListener
import com.okatanaa.timemanager.model.*
import com.okatanaa.timemanager.services.DataService
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.services.Settings
import com.okatanaa.timemanager.utilities.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.app_bar_test.*
import kotlinx.android.synthetic.main.content_test.*
import kotlinx.android.synthetic.main.week_item_fragment_content.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.NullPointerException

class MainActivity : AppCompatActivity(),  OnWeekClickListener {
    // Permanent data
    lateinit var weekAdapter: WeekPagerAdapter
    lateinit var week: Week
    var currentWeekPosition: Int = 0
    lateinit var calendarSynchronizer: CalendarSynchronizer
    lateinit var weekListAdapter: WeekListAdapter

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

        // Load necessary data
        DataService.weekArray = JsonHelper.readWeekArr(JsonHelper.readJSON(this))
        DataService.currentWeek = DataService.weekArray[0]
        this.weekListAdapter = WeekListAdapter(this, DataService.weekArray, this)
        week_list_view.adapter = this.weekListAdapter

        // Load Settings
        JsonHelper.readSettings(JsonHelper.readJSON(this))

        // Set the data will be worked on
        this.week = DataService.currentWeek
        this.handler = Handler() {
            val  dayPosition = it.what.toInt()
            (this.weekAdapter.getItem(weekViewPager.currentItem) as WeekItemFragment)
                .updateCurrentEvent()
            true

        }
        this.calendarSynchronizer = CalendarSynchronizer(this.week, this.handler)

        weekViewPager.offscreenPageLimit = 7
        this.weekAdapter = WeekPagerAdapter(supportFragmentManager)
        weekViewPager.adapter = this.weekAdapter


        setMoveButtons()
        weekViewPager.currentItem = this.calendarSynchronizer.currentWeekDayNum
    }

    private fun reloadData() {
        this.week = DataService.currentWeek
        this.calendarSynchronizer.stopSynchronizingThread()
        this.calendarSynchronizer = CalendarSynchronizer(this.week, this.handler)
        weekAdapter.notifyDataSetChanged()
        weekViewPager.currentItem = this.calendarSynchronizer.currentWeekDayNum
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
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_delete_week -> {
                return deleteCurrentWeek()
            }
            R.id.action_rename_week -> {
                TextClickedListener.onClick(this, WEEK_NAME, week.name)
                return true
            }
            R.id.action_copy_week -> {
                val weekCopy = Week(this.week)
                weekCopy.name = weekCopy.name + "(copy)"
                DataService.weekArray.add(this.currentWeekPosition + 1, weekCopy)
                this.weekListAdapter.notifyDataSetChanged()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED)
            return

        when (requestCode) {
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

    private fun setMoveButtons() {
        moveLayout.alpha = 0F
        moveLayout.isEnabled = false

        moveUpBtn.alpha = 0F
        moveUpBtn.isEnabled = false
        moveDoneBtn.alpha = 0F
        moveDoneBtn.isEnabled = false
        moveDownBtn.alpha = 0F
        moveDownBtn.isEnabled = false
    }



    private fun saveData() {
        val json = JSONObject()
        val jsonArray = JSONArray()
        for (i in 0 until DataService.weekArray.count()) {
            jsonArray.put(JsonHelper.weekToJson(DataService.weekArray[i]))
        }
        json.put(JSON_WEEKS, jsonArray)

        val jsonSettings = JSONObject()
        val jsonTimeArray = JSONArray()
        jsonTimeArray.put(Settings.globalStartTime.hours)
        jsonTimeArray.put(Settings.globalStartTime.minutes)
        jsonSettings.put(JSON_GLOBAL_START_TIME, jsonTimeArray)
        json.put(JSON_SETTINGS, jsonSettings)
        this.openFileOutput(JSON_PRIMARY_DATA_WEEK_FILE, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }


    /// LISTEN FUNCTIONS

    /// EVENT INTERACTION

    override fun onBackPressed() {
        println("Back is pressed!")
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if(moveLayout.isEnabled){
            onClickedMoveDoneBtn(View(this))
        }else{
            super.onBackPressed()
        }
    }

    fun onClickedMoveUpBtn(view: View) {
        val day = this.week.getDay(weekViewPager.currentItem)
        val fragmentAdapter = weekViewPager.focusedChild.findViewById<ListView>(R.id.eventListView).adapter
                as DayListAdapter
        val pickedEventPosition = fragmentAdapter.pickedEventPosition
        fragmentAdapter.removeSelectedView(pickedEventPosition)
        synchronized(day) {
            if (day.moveEventUp(pickedEventPosition)) {
                fragmentAdapter.pickedEventPosition = pickedEventPosition - 1
            }
        }
        fragmentAdapter.addSelectedView(fragmentAdapter.pickedEventPosition)
        fragmentAdapter.notifyDataSetChanged()
    }

    /*
    * This method can be called from onDeleteBtnClicked.
    * If it is called from onDeleteBtnClicked it causes NullPointerException because
    * adapter of the eventListView is destroyed because of lack of elements(???).
     */
    fun onClickedMoveDoneBtn(view: View) {
        try {
            val fragmentAdapter = weekViewPager.focusedChild.findViewById<ListView>(R.id.eventListView)
                .adapter as DayListAdapter
            fragmentAdapter.removeAllSelectedViews()
            fragmentAdapter.notifyDataSetChanged()

        } catch (e: NullPointerException) { }
        finally {
            setMoveButtons()
            val newConstraintSet = ConstraintSet()
            newConstraintSet.clone(contentTestLayout)
            newConstraintSet.connect(R.id.weekViewPager, ConstraintSet.BOTTOM, R.id.contentTestLayout, ConstraintSet.BOTTOM)
            newConstraintSet.applyTo(contentTestLayout)
        }
    }

    fun onClickedMoveDownBtn(view: View) {
        val day = this.week.getDay(weekViewPager.currentItem)
        val fragmentAdapter = weekViewPager.focusedChild.findViewById<ListView>(R.id.eventListView).adapter
            as DayListAdapter
        val pickedEventPosition = fragmentAdapter.pickedEventPosition
        fragmentAdapter.removeSelectedView(pickedEventPosition)
        synchronized(day) {
            if (day.moveEventDown(pickedEventPosition)) {
                fragmentAdapter.pickedEventPosition = pickedEventPosition + 1
            }
        }
        fragmentAdapter.addSelectedView(fragmentAdapter.pickedEventPosition)
        fragmentAdapter.notifyDataSetChanged()
    }

    fun onDeleteBtnClicked(view: View) {
        val day = this.week.getDay(weekViewPager.currentItem)
        val fragmentAdapter = weekViewPager.focusedChild.findViewById<ListView>(R.id.eventListView).adapter
                as DayListAdapter
        val pickedEventPosition = fragmentAdapter.pickedEventPosition

        if(fragmentAdapter.count == 1) {
            fragmentAdapter.removeAllSelectedViews()
            fragmentAdapter.day.deleteEvent(0)
            fragmentAdapter.notifyDataSetChanged()
            onClickedMoveDoneBtn(View(this))
            return
        }

        if(pickedEventPosition == fragmentAdapter.count - 1) {
            fragmentAdapter.removeSelectedView(pickedEventPosition)
            day.deleteEvent(pickedEventPosition)
            fragmentAdapter.addSelectedView(pickedEventPosition - 1)
            fragmentAdapter.pickedEventPosition = pickedEventPosition - 1
            fragmentAdapter.notifyDataSetChanged()
            return
        }

        day.deleteEvent(pickedEventPosition)
        fragmentAdapter.notifyDataSetChanged()

    }



    /// WEEK INTERACTION

    fun onMoveWeekUpBtnClicked(view: View) {
        if(DataService.moveWeekUp(this.currentWeekPosition))
            this.currentWeekPosition--

        this.weekListAdapter.notifyDataSetChanged()
    }

    fun onMoveWeekDownBtnClicked(view: View) {
        if(DataService.moveWeekDown(this.currentWeekPosition))
            this.currentWeekPosition++

        this.weekListAdapter.notifyDataSetChanged()
    }

    fun onDeleteWeekBtnClicked(view: View) {
        deleteCurrentWeek()
    }

    private fun deleteCurrentWeek(): Boolean {
        if (DataService.weekArray.size == 1) {
            Toast.makeText(this, "Must be at least one week!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (this.currentWeekPosition == DataService.weekArray.size - 1) {
            this.currentWeekPosition = this.currentWeekPosition - 1
            DataService.weekArray.removeAt(this.currentWeekPosition + 1)
            DataService.currentWeek = DataService.weekArray[currentWeekPosition]
            weekListAdapter.notifyDataSetChanged()
            reloadData()
            Toast.makeText(this, "Week deleted!", Toast.LENGTH_SHORT).show()
            return true
        }

        DataService.weekArray.removeAt(this.currentWeekPosition)
        DataService.currentWeek = DataService.weekArray[currentWeekPosition]
        weekListAdapter.notifyDataSetChanged()
        reloadData()
        Toast.makeText(this, "Week deleted!", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onWeekClicked(week: Week, adapter: WeekListAdapter, position: Int) {
        DataService.currentWeek = week
        this.currentWeekPosition = position
        this.weekListAdapter.notifyDataSetChanged()
        reloadData()
    }

    fun addWeekBtnClicked(view: View) {
        DataService.weekArray.add(Week("Week ${this.weekListAdapter.count + 1}"))
        this.weekListAdapter.notifyDataSetChanged()
    }

    companion object {
        const val WEEK_NAME = "Week name"
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class WeekPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a WeekItemFragment (defined as a static inner class below).

            return WeekItemFragment.newInstance(position + 1, this@MainActivity.week.getDay(position))
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 7
        }

        /*
        * This method is overrided in order to have all fragments be updated after changing current week.
         */
        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class WeekItemFragment : Fragment(), OnEventClickListener, OnEventLongClickListener{

        lateinit var day: Day
        var adapter: DayListAdapter? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val weekItem = inflater.inflate(R.layout.week_item_fragment_content, container, false)
            val dayName = weekItem?.findViewById<TextView>(R.id.day_name)
            val dayListView = weekItem?.findViewById<ListView>(R.id.eventListView)
            val eventAddBtn = weekItem?.findViewById<Button>(R.id.addEventBtn)

            dayName?.text = "${day.title}, ${day.todaysDate} ${day.month}"

            if(day.isToday)
                weekItem.dayLayout.setBackgroundResource(R.drawable.week_item_today_look)
            else
                weekItem.dayLayout.setBackgroundResource(R.drawable.week_item_look)

            this.adapter = DayListAdapter(context!!, day, this)
            dayListView?.adapter = this.adapter
            dayListView?.setOnItemLongClickListener { parent, view, position, id ->
                this.onEventLongClicked(parent as @kotlin.ParameterName(name = "parent") AdapterView<DayListAdapter>, view, position, id)}

            eventAddBtn?.setOnClickListener{
                val newEvent = Event("Event ${day.eventCount()}")
                if (day.addNewEvent(newEvent)) {
                    (dayListView?.adapter as DayListAdapter).notifyDataSetChanged()
                    Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Can't add event!", Toast.LENGTH_SHORT).show()
                }
            }
            return weekItem
        }

        fun updateCurrentEvent() {
            if(this.day.isToday)
                this.adapter?.notifyDataSetChanged()
        }

        override fun onEventClicked(event: Event, adapter: DayListAdapter, position: Int) {
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

            val eventIntent = Intent(context, EventActivity::class.java)
            val eventJson = JsonHelper.eventToJson(event)

            eventIntent.putExtra(EXTRA_EVENT_JSON, eventJson.toString())
            eventIntent.putExtra(EXTRA_TOP_TIME_BORDER, topTimeBorder)
            eventIntent.putExtra(EXTRA_BOTTOM_TIME_BORDER, bottomTimeBorder)
            eventIntent.putExtra(EXTRA_EVENT_POSITION, position)
            startActivityForResult(eventIntent, RC_EVENT_ACTIVITY)
        }

        override fun onEventLongClicked(parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long): Boolean {
            this.adapter = parent.adapter
            this.adapter?.pickedEventPosition = position

            if (parent.adapter.getSelectedViewsCount() > 0)
                parent.adapter.removeAllSelectedViews()

            parent.adapter.addSelectedView(position)
            parent.adapter.notifyDataSetChanged()
            val newConstraintSet = ConstraintSet()
            newConstraintSet.clone((context as MainActivity).contentTestLayout)
            newConstraintSet.connect(R.id.weekViewPager, ConstraintSet.BOTTOM, R.id.moveLayout, ConstraintSet.TOP)
            newConstraintSet.applyTo((context as MainActivity).contentTestLayout)

            (context as MainActivity).moveLayout.alpha = 1F
            (context as MainActivity).moveLayout.isEnabled = true
            (context as MainActivity).moveUpBtn.alpha = 1F
            (context as MainActivity).moveUpBtn.isEnabled = true
            (context as MainActivity).moveDoneBtn.alpha = 1F
            (context as MainActivity).moveDoneBtn.isEnabled = true
            (context as MainActivity).moveDownBtn.alpha = 1F
            (context as MainActivity).moveDownBtn.isEnabled = true

            val animation = AnimationUtils.loadAnimation(context, R.anim.fade)

            (context as MainActivity).moveUpBtn.startAnimation(animation)
            (context as MainActivity).moveDoneBtn.startAnimation(animation)
            (context as MainActivity).moveDownBtn.startAnimation(animation)
            (context as MainActivity).moveLayout.startAnimation(animation)

            return true
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
                        val changedEventPosition = data?.getIntExtra(EXTRA_EVENT_POSITION, 0)
                        // Change picked event data with received data
                        this.day.getEvent(changedEventPosition).copy(changedEvent)
                        this.adapter?.notifyDataSetChanged()
                    }

                    if (data?.getStringExtra(EXTRA_ACTION) == ACTION_DELETE) {
                        val changedEventPosition = data?.getIntExtra(EXTRA_EVENT_POSITION, 0)
                        synchronized(this.day) {
                            day.deleteEvent(changedEventPosition)
                        }
                        this.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int, day: Day): WeekItemFragment {
                val fragment = WeekItemFragment()
                fragment.day = day
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
