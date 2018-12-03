package com.okatanaa.timemanager.model

import android.os.Parcel
import android.os.Parcelable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Event {
    var name: String
    var description: String
    var startTime: ArrayList<Int>
    var endTime: ArrayList<Int>
    var inDay: String

    constructor(name: String = "Empty event",
                description: String = "",
                startTime: ArrayList<Int> = arrayListOf(0,0),
                endTime: ArrayList<Int> = arrayListOf(0,0),
                inDay: String = "") {
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
    fun setDay(dayName: String) {
        this.inDay = "In day: $dayName"
    }

    override fun toString(): String {
        return name
    }

}