package com.okatanaa.timemanager.activity.MainActivity.views.additional_interfaces

import com.okatanaa.timemanager.fragments.DayFragment.adapter.EventListAdapter
import com.okatanaa.timemanager.model.Event

interface OnEventClickListener {
    fun onEventClicked(event: Event, adapter: EventListAdapter, position: Int)
}