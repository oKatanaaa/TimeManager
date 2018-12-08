package com.okatanaa.timemanager.services

import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week

object DataService {
    lateinit var weekArray: ArrayList<Week>
    lateinit var todaysWeek: Week
    lateinit var currentWeek: Week

    fun moveWeekUp(position: Int): Boolean {
        if(position == 0)
            return false

        val currentWeek = weekArray[position]
        weekArray[position] = weekArray[position - 1]
        weekArray[position - 1] = currentWeek

        return true
    }

    fun moveWeekDown(position: Int): Boolean {
        if(position == weekArray.size - 1)
            return false

        val currentWeek = weekArray[position]
        weekArray[position] = weekArray[position + 1]
        weekArray[position + 1] = currentWeek

        return true
    }
}