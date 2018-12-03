package com.okatanaa.timemanager.model

class Day(events: ArrayList<Event>, val title: String) {
    private val events: ArrayList<Event>

    init {
        this.events = ArrayList<Event>(events)
    }

    fun addEvent(event: Event) {
        event.setDay(this.title)
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