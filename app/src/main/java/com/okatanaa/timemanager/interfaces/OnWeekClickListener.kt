package com.okatanaa.timemanager.interfaces

import com.okatanaa.timemanager.adapter.DayListAdapter
import com.okatanaa.timemanager.adapter.WeekListAdapter
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week

interface OnWeekClickListener {
    fun onWeekClicked(week: Week, adapter: WeekListAdapter, position: Int)
}