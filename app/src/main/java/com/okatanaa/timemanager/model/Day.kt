package com.okatanaa.timemanager.model

import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter

class Day {
    private var events: ArrayList<Event>
    var title: String
    lateinit var month: String
    var todaysDate = 0
    var isToday = false

    constructor(events: ArrayList<Event> =
                arrayListOf(), title: String) {
        this.events = events
        this.title = title
    }

    fun addEvent(event: Event) {
        event.setDay(this)
        events.add(event)
    }

    fun deleteEvent(index: Int) {
        if(events.size == 0)
            throw ArrayIndexOutOfBoundsException("Day is empty.")

        events.removeAt(index)
    }

    fun moveEventUp(currentPos: Int): Boolean {
        if(currentPos == 0)
            return false

        val movingEvent = events.removeAt(currentPos)
        events.add(currentPos - 1, movingEvent)
        return true
    }

    fun moveEventDown(currentPos: Int): Boolean {
        if(currentPos == events.size - 1)
            return false

        val movingEvent = events.removeAt(currentPos)
        events.add(currentPos + 1, movingEvent)
        return true
    }

    fun getEvent(index: Int): Event {
        return events[index]
    }

    fun getPosition(event: Event): Int {
        var position: Int = 0
        for (i in 0 until eventCount())
            if(event.equals(this.events[i]))
                position = i

        return position
    }

    fun eventCount(): Int {
        return events.count()
    }

    override fun toString(): String {
        return this.title
    }
}