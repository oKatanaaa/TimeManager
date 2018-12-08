package com.okatanaa.timemanager.model

class Week {
    var name: String
    val days: ArrayList<Day>

    constructor(name: String, days: ArrayList<Day>) {
        this.name = name
        this.days = days
    }

    constructor(name: String) {
        this.name = name
        this.days = arrayListOf(
            Day(title = "Monday"),
            Day(title = "Tuesday"),
            Day(title = "Wednesday"),
            Day(title = "Thursday"),
            Day(title = "Friday"),
            Day(title = "Saturday"),
            Day(title = "Sunday")
        )
    }

    private val dayMap: Map<String, Int> = mapOf(
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