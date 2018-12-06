package com.okatanaa.timemanager.interfaces

import com.okatanaa.timemanager.model.Event

interface CurrentEventChangedListener {
    fun currentEventChanged(dayPosition: Int)
}