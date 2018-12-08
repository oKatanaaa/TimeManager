package com.okatanaa.timemanager.model

import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import com.okatanaa.timemanager.additional_classes.LocalDate
import com.okatanaa.timemanager.interfaces.CurrentEventChangedListener
import kotlin.IllegalArgumentException

class CalendarSynchronizer(val week: Week, val eventChangedListener: CurrentEventChangedListener, val handler: Handler) {

    val calendar = Calendar.getInstance()
    lateinit var currentWeekDay: String
    var currentWeekDayNum = 0

    lateinit var currentMonth: String
    var currentMonthNum = 0
    var todaysDate = 0

    lateinit var synchronizedDay: Day
    lateinit var thread: CalendarSynchronizer.DaySyncronizer

    init {
        setData()
        synchronize()
        startSynchronizingThread()
    }

    fun synchronize() {
        var helper = LocalDate.of(2018, this.currentMonthNum, this.todaysDate).minusDays(this.currentWeekDayNum)
        for(day in this.week.days) {
            day.todaysDate = helper.dayOfMonth
            day.month = getMonthNameByNumber(helper.monthValue)
            helper = helper.plusDays(1)
        }
        this.week.getDayByName(this.currentWeekDay).isToday = true
        this.week.getDayByName(this.currentWeekDay).month = this.currentMonth
        this.week.getDayByName(this.currentWeekDay).todaysDate = this.todaysDate
    }

    fun setData() {
        setCurrentWeekDay()
        setCurrentMonth()
        this.todaysDate = this.calendar.get(Calendar.DAY_OF_MONTH)
        this.synchronizedDay = this.week.getDay(this.currentWeekDayNum)
    }

    fun startSynchronizingThread() {
        this.thread = DaySyncronizer(this.eventChangedListener, this.handler)
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

    inner class DaySyncronizer(val eventChangedListener: CurrentEventChangedListener, val handler: Handler): Thread() {
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
                    println("New!")
                    currentEvent = newCurrentEvent
                    this.handler.sendEmptyMessage(this@CalendarSynchronizer.currentWeekDayNum)
                    //this.eventChangedListener.currentEventChanged(this@CalendarSynchronizer.currentWeekDayNum)
                }

                if(newCurrentEvent == null && currentEvent != null) {
                    println("New!")
                    currentEvent = newCurrentEvent
                    this.handler.sendEmptyMessage(this@CalendarSynchronizer.currentWeekDayNum)
                    //this.eventChangedListener.currentEventChanged(this@CalendarSynchronizer.currentWeekDayNum)
                }

                if(newCurrentEvent != null && currentEvent != null && !newCurrentEvent?.equals(currentEvent)) {
                    println("New!")
                    currentEvent = newCurrentEvent
                    this.handler.sendEmptyMessage(this@CalendarSynchronizer.currentWeekDayNum)
                    //this.eventChangedListener.currentEventChanged(this@CalendarSynchronizer.currentWeekDayNum)
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