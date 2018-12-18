package com.okatanaa.timemanager.model

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Event {
    var name: String
    var description: String
    var startTimeArr: ArrayList<Int>
    var endTimeArr: ArrayList<Int>
    lateinit var inDay: Day
    var color: Int

    @Volatile var isCurrent = false

    @Volatile lateinit var startTime: Time
    @Volatile lateinit var endTime: Time

    constructor(name: String = "Empty event",
                description: String = "",
                startTime: ArrayList<Int> = arrayListOf(0,0),
                endTime: ArrayList<Int> = arrayListOf(0,0),
                inDay: Day = Day(title = "No day"),
                color: Int = Event.NONE) {
        this.name = name
        this.description = description
        this.startTimeArr = startTime
        this.endTimeArr = endTime
        this.inDay = inDay
        this.color = color

        this.startTime = Time(this.startTimeArr[0], this.startTimeArr[1])
        this.endTime = Time(this.endTimeArr[0], this.endTimeArr[1])
    }


    @Synchronized fun copy(other: Event): Event {
        this.name = other.name
        this.description = other.description
        this.startTimeArr = other.startTimeArr.clone() as ArrayList<Int>
        this.endTimeArr = other.endTimeArr.clone() as ArrayList<Int>
        this.inDay = other.inDay
        this.color = other.color
        this.startTime = Time(this.startTimeArr)
        this.endTime = Time(this.endTimeArr)
        return this
    }

    // This function is called only after adding event in a particular day
    @Synchronized fun setDay(day: Day) {
        this.inDay = day
    }

    @Synchronized fun smartSetStartTime(startTime: Time) {
        this.startTime = startTime
        this.startTimeArr[0] = startTime.hours
        this.startTimeArr[1] = startTime.minutes
    }

    @Synchronized fun smartSetEndTime(endTime: Time) {
        this.endTime = endTime
        this.endTimeArr[0] = endTime.hours
        this.endTimeArr[1] = endTime.minutes
    }

    @Synchronized fun swapWithoutTimeCopy(other: Event) {
        val temp = Event().copy(other)
        other.copy(this)
        other.smartSetStartTime(temp.startTime)
        other.smartSetEndTime(temp.endTime)
        val oldStartTime = this.startTime
        val oldEndTime = this.endTime
        this.copy(temp)
        this.smartSetStartTime(oldStartTime)
        this.smartSetEndTime(oldEndTime)
    }

    @Synchronized override fun toString(): String {
        return name
    }

    @Synchronized override fun equals(other: Any?): Boolean {
        if(other !is Event)
            return false

        return this.name == other.name &&
                this.description == other.description &&
                this.startTimeArr == other.startTimeArr &&
                this.endTimeArr == other.endTimeArr
    }

    @Synchronized fun clone(): Event {
        val newEvent = Event()
        newEvent.copy(this)
        return newEvent
    }

    companion object {
        const val RED = 4
        const val GREEN = 3
        const val YELLOW = 1
        const val BLUE = 2
        const val NONE = 0
    }

}