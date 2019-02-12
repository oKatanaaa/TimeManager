package com.okatanaa.timemanager.utils.CalendarSynchronizer

import java.util.Calendar
import android.os.Handler
import android.util.Log
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.utils.LOG_TIME_SYNCHRONIZER
import com.okatanaa.timemanager.utils.LocalDate
import java.util.*
import kotlin.IllegalArgumentException

class CalendarSynchronizer(val week: Week, val handler: Handler) {

    val calendar = Calendar.getInstance()
    val date = Date()
    lateinit var currentWeekDay: String
    var currentWeekDayNum = 0

    lateinit var currentMonth: String
    var currentMonthNum = 0
    var todaysDate = 0

    lateinit var synchronizedDay: Day
    lateinit var thread: DaySyncronizer

    init {
        setData()
        synchronize()
        startSynchronizingThread()
    }

    /**
     * This function sets dates and months on days in current week.
     */
    fun synchronize() {
        var helper = LocalDate.of(2018, this.currentMonthNum, this.todaysDate).minusDays(this.currentWeekDayNum)
        for(i in 0 until 7) {
            val day = this.week.getDay(i)
            day.todaysDate = helper.dayOfMonth
            day.month = getMonthNameByNumber(helper.monthValue)
            helper = helper.plusDays(1)
        }
    }

    private fun setData() {
        setCurrentWeekDay()
        setCurrentMonth()
        this.todaysDate = this.calendar.get(Calendar.DAY_OF_MONTH)
        this.synchronizedDay = this.week.getDay(this.currentWeekDayNum)
        this.synchronizedDay.isToday = true
    }

    private fun startSynchronizingThread() {
        this.thread = DaySyncronizer(this.handler)
        this.thread.isDaemon = true
        this.thread.start()
        println("Thread started")
    }

    fun stopSynchronizingThread() {
        this.thread.interrupt()
    }

    private fun setCurrentWeekDay() {
        this.currentWeekDayNum = this.calendar.get(Calendar.DAY_OF_WEEK)

        this.currentWeekDay = when(this.currentWeekDayNum) {
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> throw IllegalArgumentException("Unknown day!")
        }

        this.currentWeekDayNum = when(this.currentWeekDayNum) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> throw IllegalArgumentException("Unknown day!")
        }
    }

    private fun setCurrentMonth() {
        this.currentMonthNum = this.calendar.get(Calendar.MONTH)

        this.currentMonth = when(this.currentMonthNum) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> throw IllegalArgumentException("Unknown month!")
        }

        // This increment caused by fact that Calendar month constants
        // start count from zero
        this.currentMonthNum++
    }

    fun getMonthNameByNumber(number: Int) = when(number) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> throw IllegalArgumentException("Unknown month!")
    }

    inner class DaySyncronizer(val handler: Handler): Thread() {
        override fun run() {
            val currentDay = this@CalendarSynchronizer.synchronizedDay
            var currentEvent: Event? = null
            while(true) {
                if(isInterrupted)
                    break
                val currentHours = this@CalendarSynchronizer.calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinutes = this@CalendarSynchronizer.calendar.get(Calendar.MINUTE)
                val currentTime = Time(currentHours, currentMinutes)
                var newCurrentEvent: Event? = null
                var oldEventCount = currentDay.eventCount()

                begin@for(i in 0 until currentDay.eventCount()) {
                    if(currentDay.eventCount() != oldEventCount) {
                        oldEventCount = currentDay.eventCount()
                        break@begin
                    }

                    val event = currentDay.getEvent(i)

                    event.isCurrent = currentTime.isBetween(event.startTime, event.endTime)
                    if(event.isCurrent) {
                        newCurrentEvent = event
                        break
                    }
                }

                if(newCurrentEvent != null && currentEvent == null) {
                    Log.i(LOG_TIME_SYNCHRONIZER, "New current event.")
                    currentEvent = newCurrentEvent
                    this.handler.sendEmptyMessage(this@CalendarSynchronizer.currentWeekDayNum)
                }

                if(newCurrentEvent == null && currentEvent != null) {
                    Log.i(LOG_TIME_SYNCHRONIZER, "No current event.")
                    currentEvent = newCurrentEvent
                    this.handler.sendEmptyMessage(this@CalendarSynchronizer.currentWeekDayNum)
                }

                if(newCurrentEvent != null && currentEvent != null && !newCurrentEvent?.equals(currentEvent)) {
                    Log.i(LOG_TIME_SYNCHRONIZER, "New current event.")
                    currentEvent = newCurrentEvent
                    this.handler.sendEmptyMessage(this@CalendarSynchronizer.currentWeekDayNum)
                }
                try {
                    sleep(500)
                } catch (e: InterruptedException) {
                    break
                }
            }

        }
    }

}