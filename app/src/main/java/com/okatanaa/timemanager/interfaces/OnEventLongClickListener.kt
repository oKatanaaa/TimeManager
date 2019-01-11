package com.okatanaa.timemanager.interfaces

import android.view.View
import android.widget.AdapterView
import com.okatanaa.timemanager.fragments.adapter.EventListAdapter

interface OnEventLongClickListener {
    fun onEventLongClicked(parent: AdapterView<EventListAdapter>, view: View, position: Int, id: Long): Boolean
}