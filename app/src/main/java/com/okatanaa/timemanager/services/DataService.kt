package com.okatanaa.timemanager.services

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import com.okatanaa.timemanager.interfaces.CurrentEventChangedListener
import com.okatanaa.timemanager.interfaces.GlobalModel
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.utils.*
import org.json.JSONArray
import org.json.JSONObject

object DataService: GlobalModel {
    lateinit var weekList: ArrayList<Week>
    lateinit var calendarSynchronizer: CalendarSynchronizer

    override fun initialize(context: Context) {
        this.weekList = JsonHelper.readWeekArr(JsonHelper.readJSON(context))
    }

    override fun getWeekCount(): Int {
        return this.weekList.size
    }

    override fun getWeek(position: Int): Week {
        return this.weekList[position]
    }

    override fun getWeekNameList(): ArrayList<String> {
        val weekNameList = arrayListOf<String>()
        for(week in this.weekList)
            weekNameList.add(week.name)
        return weekNameList
    }

    override fun addWeek() {
        this.weekList.add(Week("Week ${this.weekList.size}"))
    }

    /**
     * This function copies week at the given position and insert
     * the copy at 'position + 1'
     *
     * @param position Position of the week to copy
     */
    override fun copyWeek(position: Int) {
        val weekCopy = Week(this.weekList[position])
        weekCopy.name = weekCopy.name + "(copy)"
        this.weekList.add(position + 1, weekCopy)
    }

    override fun deleteWeek(position: Int): Boolean {
        // Must be at least one week
        if (this.weekList.size == 1) {
            return false
        }

        DataService.weekList.removeAt(position)
        return true
    }

    override fun moveWeekUp(position: Int): Boolean {
        if(position == 0)
            return false

        val currentWeek = weekList[position]
        weekList[position] = weekList[position - 1]
        weekList[position - 1] = currentWeek

        return true
    }

    override fun moveWeekDown(position: Int): Boolean {
        if(position == weekList.size - 1)
            return false

        val currentWeek = weekList[position]
        weekList[position] = weekList[position + 1]
        weekList[position + 1] = currentWeek

        return true
    }

    override fun startTimeSynchronizing(currentEventChangedListener: CurrentEventChangedListener): Handler {
        val handler = @SuppressLint("HandlerLeak")
        object: Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                currentEventChangedListener.currentEventChanged(msg!!.arg1)
            }
        }

        this.calendarSynchronizer = CalendarSynchronizer(this.weekList[0], handler)
        return handler
    }

    override fun saveData(context: Context) {
        val json = JSONObject()
        val jsonArray = JSONArray()
        for (i in 0 until this.weekList.size) {
            jsonArray.put(JsonHelper.weekToJson(DataService.weekList[i]))
        }
        json.put(JSON_WEEKS, jsonArray)

        val jsonSettings = JSONObject()
        val jsonTimeArray = JSONArray()
        jsonTimeArray.put(Settings.globalStartTime.hours)
        jsonTimeArray.put(Settings.globalStartTime.minutes)
        jsonSettings.put(JSON_GLOBAL_START_TIME, jsonTimeArray)
        json.put(JSON_SETTINGS, jsonSettings)
        context.openFileOutput(JSON_PRIMARY_DATA_WEEK_FILE, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }


}