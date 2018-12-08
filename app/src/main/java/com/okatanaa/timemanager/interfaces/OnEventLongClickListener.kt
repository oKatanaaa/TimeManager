package com.okatanaa.timemanager.interfaces

import android.view.View
import android.widget.AdapterView
import com.okatanaa.timemanager.adapter.DayListAdapter

interface OnEventLongClickListener {
    fun onEventLongClicked(parent: AdapterView<DayListAdapter>, view: View, position: Int, id: Long): Boolean
}