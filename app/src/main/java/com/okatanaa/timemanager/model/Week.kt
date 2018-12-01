package com.okatanaa.timemanager.model

class Week(val days: ArrayList<Day>) {
    // Later application will allow the user to add other weeks
    var number: Int = 0

    fun getDay(index: Int): Day {
        return days[index]
    }

    fun count() : Int{
        return this.days.count()
    }
}