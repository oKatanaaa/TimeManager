package com.okatanaa.timemanager.model

class Week(val days: ArrayList<Day>) {
    var name: String = "Default week"


    private val dayMap = mapOf(
        Pair("Monday", 0),
        Pair("Tuesday", 1),
        Pair("Wednesday", 2),
        Pair("Thursday", 3),
        Pair("Friday", 4),
        Pair("Saturday", 5),
        Pair("Sunday", 6)
    )

    fun getDay(index: Int): Day {
        return days[index]
    }

    fun getDayByName(dayName: String): Day {
        return getDay(this.dayMap[dayName]!!)
    }

    fun count() : Int{
        return this.days.count()
    }
}