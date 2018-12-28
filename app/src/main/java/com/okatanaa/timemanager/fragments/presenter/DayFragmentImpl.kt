package com.okatanaa.timemanager.fragments.presenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.okatanaa.timemanager.adapter.EventListAdapter
import com.okatanaa.timemanager.controller.EventActivity
import com.okatanaa.timemanager.fragments.views.DayFragmentView
import com.okatanaa.timemanager.fragments.views.DayFragmentViewImpl
import com.okatanaa.timemanager.fragments.presenter.event_ui_interaction.OnEventUIClickListenerImpl
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.utils.JsonHelper
import com.okatanaa.timemanager.utilities.*
import org.json.JSONObject

class DayFragmentImpl : Fragment(), DayFragment {

    private lateinit var dayFragmentView: DayFragmentView
    private lateinit var day: Day
    private lateinit var eventList: ListView
    private lateinit var eventListAdapter: EventListAdapter
    private lateinit var aEventList: ArrayList<Event>
    private var isToday = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.dayFragmentView = DayFragmentViewImpl(context!!, container)
        //this.dayFragmentView.setDayName("${day.title}, ${day.todaysDate} ${day.month}")
        this.dayFragmentView.setDayName("${day.title}, ${day.todaysDate} Month")
        this.dayFragmentView.setOnEventUIClickListener(OnEventUIClickListenerImpl(this.dayFragmentView, this.day))

        if(day.isToday)
            this.dayFragmentView.setDayCurrent()

        this.eventList = this.dayFragmentView.getEventList()
        this.eventList.setOnItemClickListener { _, _, position, _ -> onEventClicked(position) }
        this.eventListAdapter = this.eventList.adapter as EventListAdapter

        return this.dayFragmentView.getRootView()
    }

    override fun bindEventList(eventList: ArrayList<Event>) {
        this.aEventList = eventList
        this.dayFragmentView.bindEventList(eventList)
    }

    override fun setCurrentDay() {
        this.isToday = true
        this.dayFragmentView.setDayCurrent()
    }

    override fun currentEventChanged() {
        if(this.isToday)
            this.eventListAdapter.notifyDataSetChanged()
    }

    fun onEventClicked(position: Int) {
        var topTimeBorder: Int = 0
        var bottomTimeBorder: Int = Time.MINUTES_IN_DAY

        // There is the only event in day
        if (position == 0 && this.eventListAdapter.count == 1) {
            topTimeBorder = 0
            bottomTimeBorder = Time.MINUTES_IN_DAY
        }
        // This event is first, but there is other events
        else if (position == 0 && this.eventListAdapter.count > 1) {
            val belowEvent = this.eventListAdapter.getItem(position + 1) as Event
            topTimeBorder = 0
            bottomTimeBorder = belowEvent.startTime.toMinutes()
        }
        // There are many events and this event is somewhere in the middle of the list
        else if (position != 0 && position < this.eventListAdapter.count - 1) {
            val aboveEvent = this.eventListAdapter.getItem(position - 1) as Event
            val belowEvent = this.eventListAdapter.getItem(position + 1) as Event
            topTimeBorder = aboveEvent.endTime.toMinutes()
            bottomTimeBorder = belowEvent.startTime.toMinutes()
        }
        // There are many events and this event is the last one
        else if (position == this.eventListAdapter.count - 1) {
            val aboveEvent = this.eventListAdapter.getItem(position - 1) as Event
            topTimeBorder = aboveEvent.endTime.toMinutes()
            bottomTimeBorder = Time.MINUTES_IN_DAY
        }

        val eventIntent = Intent(context, EventActivity::class.java)
        val eventJson = JsonHelper.eventToJson(this.day.getEvent(position))

        eventIntent.putExtra(EXTRA_EVENT_JSON, eventJson.toString())
        eventIntent.putExtra(EXTRA_TOP_TIME_BORDER, topTimeBorder)
        eventIntent.putExtra(EXTRA_BOTTOM_TIME_BORDER, bottomTimeBorder)
        eventIntent.putExtra(EXTRA_EVENT_POSITION, position)
        startActivityForResult(eventIntent, RC_EVENT_ACTIVITY)
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
                    this.eventListAdapter?.notifyDataSetChanged()
                }

                if (data?.getStringExtra(EXTRA_ACTION) == ACTION_DELETE) {
                    val changedEventPosition = data?.getIntExtra(EXTRA_EVENT_POSITION, 0)
                    synchronized(this.day) {
                        day.deleteEvent(changedEventPosition)
                    }
                    this.eventListAdapter?.notifyDataSetChanged()
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
        fun newInstance(sectionNumber: Int, day: Day): DayFragmentImpl {
            val fragment = DayFragmentImpl()
            fragment.day = day
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}