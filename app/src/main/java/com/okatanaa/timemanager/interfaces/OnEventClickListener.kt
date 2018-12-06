package com.okatanaa.timemanager.interfaces

import com.okatanaa.timemanager.adapter.DayListAdapter
import com.okatanaa.timemanager.model.Event

interface OnEventClickListener {
    fun onEventClicked(event: Event, adapter: DayListAdapter, position: Int)
}