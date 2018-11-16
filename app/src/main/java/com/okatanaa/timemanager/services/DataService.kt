package com.okatanaa.timemanager.services

import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event

object DataService {
    val day1 = Day(arrayListOf<Event>(Event("Math"), Event("English"), Event("Programming")), "Monday")
    val week = arrayListOf<Day>(day1, day1, day1, day1, day1, day1, day1)
}