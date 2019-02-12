package com.okatanaa.timemanager.fragments.DayFragment.views.additional_interfaces

import android.widget.AdapterView
import com.okatanaa.timemanager.fragments.DayFragment.adapter.EventListAdapter

interface OnEventUIClickListener {
    fun onMoveEventUpBtnClicked()
    fun onMoveEventDownBtnClicked()
    fun onDeleteEventBtnClicked()
    fun onDoneEventBtnClicked()
    fun onAddEventBtnClicked(adapter: EventListAdapter)
    fun onEventSelected(parent: AdapterView<EventListAdapter>, position: Int): Boolean
}