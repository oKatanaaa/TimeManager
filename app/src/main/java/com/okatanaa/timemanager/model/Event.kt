package com.okatanaa.timemanager.model

import android.os.Parcel
import android.os.Parcelable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Event {
    var name: String
    var description: String
    var startTime: ArrayList<Int>
    var endTime: ArrayList<Int>
    lateinit var inDay: Day

    constructor(name: String = "Empty event",
                description: String = "",
                startTime: ArrayList<Int> = arrayListOf(0,0),
                endTime: ArrayList<Int> = arrayListOf(0,0),
                inDay: Day = Day(title = "No day")) {
        this.name = name
        this.description = description
        this.startTime = startTime
        this.endTime = endTime
        this.inDay = inDay
    }


    fun copy(other: Event) {
        this.name = other.name
        this.description = other.description
        this.startTime = other.startTime.clone() as ArrayList<Int>
        this.endTime = other.endTime.clone() as ArrayList<Int>
        this.inDay = other.inDay
    }

    // This function is called only after adding event in a particular day
    fun setDay(day: Day) {
        this.inDay = day
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Event)
            return false

        return this.name == other.name &&
                this.description == other.description &&
                this.startTime == other.startTime &&
                this.endTime == other.endTime
    }

}