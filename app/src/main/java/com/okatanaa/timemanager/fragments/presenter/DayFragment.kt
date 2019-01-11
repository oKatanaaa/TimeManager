package com.okatanaa.timemanager.fragments.presenter

import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week

interface DayFragment {
    fun setCurrentDay()
    fun currentEventChanged()
}