package com.okatanaa.timemanager.interfaces

import com.okatanaa.timemanager.fragments.adapter.EventListAdapter
import com.okatanaa.timemanager.model.Event

interface OnEventClickListener {
    fun onEventClicked(event: Event, adapter: EventListAdapter, position: Int)
}