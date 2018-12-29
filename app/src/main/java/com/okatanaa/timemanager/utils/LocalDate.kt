package com.okatanaa.timemanager.utils
/*
* This class is created in order to replace android.LocalDate class
* and decrease the android version os requirement.(android.LocalDate requires API 26)
*
 */
class LocalDate {
    private val year: Int

    private val month: Int

    private val day: Int

    val dayOfMonth: Int
        get() = _getDayOfMonth()
    val monthValue: Int
        get() = _getMonthValue()


    enum class Month(val days: Int) {
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31)
    }


    private constructor(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    companion object {
        fun of(year: Int, month: Int, day: Int): LocalDate {
            return LocalDate(year, month, day)
        }
    }

    fun _getDayOfMonth(): Int {
        return this.day
    }

    fun _getMonthValue(): Int {
        return this.month
    }



    private fun getMonthType(month: Int): Month =
        when(month) {
            1 -> Month.JANUARY
            2 -> Month.FEBRUARY
            3 -> Month.MARCH
            4 -> Month.APRIL
            5 -> Month.MAY
            6 -> Month.JUNE
            7 -> Month.JULY
            8 -> Month.AUGUST
            9 -> Month.SEPTEMBER
            10 -> Month.OCTOBER
            11 -> Month.NOVEMBER
            12 -> Month.DECEMBER
            else -> throw IllegalArgumentException("Incorrect month value!")
        }


    fun minusDays(days: Int): LocalDate {
        var days_ = days
        var year = this.year
        var month = this.month
        var day = this.day

        while(days_ != 0) {
            if(days_ > day) {
                days_ -= day
                month--
                if(month == 0) {
                    month = 12
                    year--
                }
                day = getMonthType(month).days
                continue
            }

            if(days_ < day) {
                day -= days_
                days_ = 0
            }
        }

        return LocalDate(year, month, day)
    }

    fun plusDays(days: Int): LocalDate {
        var year = this.year
        var month = this.month
        var day = this.day + days

        while(day > getMonthType(month).days) {
            day -= getMonthType(month).days
            month++
            if(month == 13) {
                month = 1
                year++
            }
        }

        return LocalDate(year, month, day)
    }
}