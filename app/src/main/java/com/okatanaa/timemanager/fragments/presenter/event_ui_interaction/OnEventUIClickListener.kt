package com.okatanaa.timemanager.fragments.presenter.event_ui_interaction

import android.widget.AdapterView
import com.okatanaa.timemanager.fragments.adapter.EventListAdapter

interface OnEventUIClickListener {
    fun onMoveEventUpBtnClicked()
    fun onMoveEventDownBtnClicked()
    fun onDeleteEventBtnClicked()
    fun onDoneEventBtnClicked()
    fun onAddEventBtnClicked(adapter: EventListAdapter)
    fun onEventSelected(parent: AdapterView<EventListAdapter>, position: Int): Boolean
}