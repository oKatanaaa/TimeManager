package com.okatanaa.timemanager.activity.MainActivity.views.additional_interfaces

import android.view.View
import android.widget.AdapterView
import com.okatanaa.timemanager.fragments.DayFragment.adapter.EventListAdapter

interface OnEventLongClickListener {
    fun onEventLongClicked(parent: AdapterView<EventListAdapter>, view: View, position: Int, id: Long): Boolean
}