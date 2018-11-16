package com.okatanaa.timemanager.model

class Day {
    private val events: ArrayList<Event>
    val title: String

    constructor(events: ArrayList<Event>, title: String) {
        this.events = ArrayList<Event>(events)
        this.title = title
    }

    fun addEvent(event: Event) {
        events.add(event)
    }

    fun deleteEvent(index: Int) {
        if(events.size == 0)
            throw ArrayIndexOutOfBoundsException("Day is empty.")

        events.drop(index)
    }

    fun getEvent(index: Int): Event {
        return events[index]
    }

    fun eventCount(): Int {
        return events.count()
    }
}