package com.okatanaa.timemanager.model

class Time {
    companion object {
        const val MINUTES_IN_HOUR = 60
        const val MINUTES_IN_DAY = 1440
    }
    var hours: Int
    var minutes: Int

    constructor(minutes: Int) {
        this.hours = minutes / Time.MINUTES_IN_HOUR
        this.minutes = minutes % Time.MINUTES_IN_HOUR
    }

    constructor(hours: Int, minutes: Int) {
        if(hours > 24 || minutes > 59)
            throw IllegalArgumentException("Incorrect hours and minutes: ${hours}:${minutes}")

        this.hours = hours
        this.minutes = minutes
    }

    constructor(time: ArrayList<Int>) {
        this.hours = time[0]
        this.minutes = time[1]
    }


    fun toMinutes(): Int = this.hours * Time.MINUTES_IN_HOUR + this.minutes

    override fun toString(): String {
        val string = StringBuilder()
        if(this.hours < 10)
            string.append(0).append(this.hours)
        else
            string.append(this.hours)

        string.append(':')

        if(this.minutes < 10)
            string.append(0).append(this.minutes)
        else
            string.append(this.minutes)

        return string.toString()
    }

    fun isBetween(left: Time, right: Time): Boolean {
        val leftMinutes = left.toMinutes()
        val rightMinutes = right.toMinutes()
        val current = this.toMinutes()
        return leftMinutes < current && current < rightMinutes
    }
}