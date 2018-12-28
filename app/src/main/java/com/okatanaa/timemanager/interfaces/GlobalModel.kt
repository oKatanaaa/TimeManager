package com.okatanaa.timemanager.interfaces

import android.content.Context
import android.os.Handler
import com.okatanaa.timemanager.model.Week

interface GlobalModel {
    fun initialize(context: Context)
    fun getWeekCount(): Int
    fun getWeek(position: Int): Week
    fun getWeekNameList(): ArrayList<String>
    fun addWeek()
    fun copyWeek(position: Int)
    fun deleteWeek(position: Int): Boolean
    fun moveWeekUp(position: Int): Boolean
    fun moveWeekDown(position: Int): Boolean
    fun startTimeSynchronizing(onCurrentEventChangedListener: CurrentEventChangedListener): Handler
    fun saveData(context: Context)
}