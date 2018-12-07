package com.okatanaa.timemanager.model

import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter

class Day {
    @Volatile private var events: ArrayList<Event>
    @Volatile var title: String
    @Volatile lateinit var month: String
    @Volatile var todaysDate = 0
    @Volatile var isToday = false

    constructor(events: ArrayList<Event> =
                arrayListOf(), title: String) {
        this.events = events
        this.title = title
    }


    @Synchronized fun addNewEvent(event: Event): Boolean {
        if(this.eventCount() == 0) {
            event.setDay(this)
            event.smartSetStartTime(Time(0))
            event.smartSetEndTime(Time(30))
            this.events.add(event)
            return true
        }

        if(this.events[this.eventCount() - 1].endTime.toMinutes() == Time.MINUTES_IN_DAY)
            return false

        event.setDay(this)

        val aboveEvent = this.events[this.eventCount() - 1]
        val startTimeInMinutes = aboveEvent.endTime.toMinutes()

        event.smartSetStartTime(Time(startTimeInMinutes))
        if(startTimeInMinutes + 30 > Time.MINUTES_IN_DAY)
            event.smartSetEndTime(Time(Time.MINUTES_IN_DAY))
        else
            event.smartSetEndTime(Time(startTimeInMinutes + 30))

        events.add(event)
        return true
    }

    @Synchronized fun addExistingEvent(event: Event): Boolean {
        event.setDay(this)
        events.add(event)
        return true
    }

    @Synchronized fun deleteEvent(index: Int) {
        if(events.size == 0)
            throw ArrayIndexOutOfBoundsException("Day is empty.")

        events.removeAt(index)
    }

    @Synchronized fun moveEventUp(currentPos: Int): Boolean {
        if(currentPos == 0)
            return false

        val movingEvent = events[currentPos]
        val aboveEvent = events[currentPos - 1]
        movingEvent.swapWithoutTimeCopy(aboveEvent)
        return true
    }

    @Synchronized fun moveEventDown(currentPos: Int): Boolean {
        if(currentPos == events.size - 1)
            return false

        val movingEvent = events[currentPos]
        val belowEvent = events[currentPos + 1]
        movingEvent.swapWithoutTimeCopy(belowEvent)
        return true
    }

    @Synchronized fun getEvent(index: Int): Event {
        return events[index]
    }

    @Synchronized fun getPosition(event: Event): Int {
        var position: Int = 0
        for (i in 0 until eventCount())
            if(event.equals(this.events[i]))
                position = i

        return position
    }

    @Synchronized fun eventCount(): Int {
        return events.count()
    }

    @Synchronized override fun toString(): String {
        return this.title
    }
}