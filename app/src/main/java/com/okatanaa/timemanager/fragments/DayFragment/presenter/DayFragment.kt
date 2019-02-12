package com.okatanaa.timemanager.fragments.DayFragment.presenter

import com.okatanaa.timemanager.model.Day

interface DayFragment {
    fun setDay(day: Day)
    fun currentEventChanged()
}