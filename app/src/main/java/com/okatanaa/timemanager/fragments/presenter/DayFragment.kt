package com.okatanaa.timemanager.fragments.presenter

import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week

interface DayFragment {
    fun bindEventList(eventList: ArrayList<Event>)
    fun setCurrentDay()
    fun currentEventChanged()
}