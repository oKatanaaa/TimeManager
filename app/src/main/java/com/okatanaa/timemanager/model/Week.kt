package com.okatanaa.timemanager.model

class Week(val days: ArrayList<Day>) {
    var name: String = "Default week"

    fun getDay(index: Int): Day {
        return days[index]
    }

    fun count() : Int{
        return this.days.count()
    }
}