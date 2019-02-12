package com.okatanaa.timemanager.utils.CalendarSynchronizer

import com.okatanaa.timemanager.model.Event

interface CurrentEventChangedListener {
    fun currentEventChanged(dayPosition: Int)
}