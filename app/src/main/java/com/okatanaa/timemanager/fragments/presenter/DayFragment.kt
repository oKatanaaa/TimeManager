package com.okatanaa.timemanager.fragments.presenter

import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week

interface DayFragment {
    fun setDay(day: Day)
    fun currentEventChanged()
}