package com.okatanaa.timemanager.fragments.presenter.event_ui_interaction

import android.content.Context
import android.widget.AdapterView
import android.widget.Toast
import com.okatanaa.timemanager.adapter.EventListAdapter
import com.okatanaa.timemanager.fragments.views.DayFragmentView
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event

class OnEventUIClickListenerImpl: OnEventUIClickListener {

    /*
    * These fields are initialized in onEventSelected function.
    * It's because adapter can be recreated while application is running, so
    * we can't initialize adapter field in constructor. It changes dynamically
    * while app runs.
     */
    private lateinit var adapter: EventListAdapter
    private var selectedEventPosition: Int = -1

    private val dayFragmentView: DayFragmentView
    private val day: Day

    constructor(dayFragmentView: DayFragmentView, day: Day) {
        this.dayFragmentView = dayFragmentView
        this.day = day
    }

    override fun onMoveEventUpBtnClicked() {
        this.adapter.removeSelectedView(this.selectedEventPosition)
        synchronized(this.day) {
            if (this.day.moveEventUp(this.selectedEventPosition)) {
                this.selectedEventPosition = this.selectedEventPosition - 1
            }
        }
        this.adapter.addSelectedView(this.selectedEventPosition)
        this.adapter.notifyDataSetChanged()
    }

    override fun onMoveEventDownBtnClicked() {
        this.adapter.removeSelectedView(this.selectedEventPosition)
        synchronized(this.day) {
            if (this.day.moveEventDown(this.selectedEventPosition)) {
                this.selectedEventPosition = this.selectedEventPosition + 1
            }
        }
        this.adapter.addSelectedView(selectedEventPosition)
        this.adapter.notifyDataSetChanged()
    }

    override fun onDeleteEventBtnClicked() {
        if(this.adapter.count == 1) {
            this.adapter.removeAllSelectedViews()
            this.day.deleteEvent(0)
            this.adapter.notifyDataSetChanged()
            this.dayFragmentView.hideEventInteractionUI()
            return
        }

        if(selectedEventPosition == this.adapter.count - 1) {
            this.adapter.removeSelectedView(selectedEventPosition)
            this.day.deleteEvent(selectedEventPosition)
            this.adapter.addSelectedView(selectedEventPosition - 1)
            this.selectedEventPosition = selectedEventPosition - 1
            this.adapter.notifyDataSetChanged()
            return
        }

        this.day.deleteEvent(selectedEventPosition)
        this.adapter.notifyDataSetChanged()
    }

    override fun onDoneEventBtnClicked() {
        this.adapter.removeAllSelectedViews()
        this.dayFragmentView.hideEventInteractionUI()
    }

    override fun onAddEventBtnClicked() {
        val newEvent = Event("Event ${day.eventCount()}")
        if (this.day.addNewEvent(newEvent)) {
            this.adapter.notifyDataSetChanged()
            this.dayFragmentView.showToast("Event added", Toast.LENGTH_SHORT)
        } else
            this.dayFragmentView.showToast("Can't add event!", Toast.LENGTH_SHORT)
    }

    override fun onEventSelected(parent: AdapterView<EventListAdapter>, position: Int): Boolean {
        this.adapter = parent.adapter
        this.selectedEventPosition = position

        if (this.adapter.getSelectedViewsCount() > 0)
            this.adapter.removeAllSelectedViews()

        this.adapter.addSelectedView(position)
        this.adapter.notifyDataSetChanged()

        this.dayFragmentView.showEventInteractionUI()

        return true
    }
}