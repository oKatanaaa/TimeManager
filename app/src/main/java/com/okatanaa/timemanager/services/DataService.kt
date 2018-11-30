package com.okatanaa.timemanager.services

import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event

object DataService {
    val day1 = Day(arrayListOf<Event>(Event("Math | Programming | English | GameDev | Math | Programming | English | GameDev"), Event("English"), Event("Programming"),
        Event("Math"), Event("English"), Event("Programming"),
        Event("Math"), Event("English"), Event("Programming"),
        Event("Math"), Event("English"), Event("Programming"),
        Event("Math"), Event("English"), Event("Programming")), "Monday")
    val day2 = Day(arrayListOf<Event>(Event("Math | Programming | English | GameDev | Math | Programming | English | GameDev"), Event("English"), Event("Programming")),
        "Tuesday")
    val day3 = Day(arrayListOf<Event>(Event("Math")), "Wednesday")
    val week = arrayListOf<Day>(day1, day2, day3, day1, day1, day1, day1)
}