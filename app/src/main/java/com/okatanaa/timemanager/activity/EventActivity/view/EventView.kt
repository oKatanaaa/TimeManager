package com.okatanaa.timemanager.activity.EventActivity.view

import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.viewmvp.ViewMvp

interface EventView: ViewMvp {

    interface OnStartTimeSelectListener {
        fun onStartTimeSelected(hourOfDay: Int, minute: Int)
    }

    interface OnEndTimeSelectListener {
        fun onEndTimeSelected(hourOfDay: Int, minute: Int)
    }

    interface OnColorSelectListener {
        fun onColorSelected(color: Int)
    }

    interface OnEventNameClickListener {
        fun onEventNameClicked(currentText: String)
    }

    interface OnEventDescriptionClickListener {
        fun onEventDescriptionClicked(currentText: String)
    }

    fun setEventName(name: String)
    fun setDayName(name: String)
    fun setDescription(description: String)
    fun setStartTime(time: String)
    fun setEndTime(time: String)
    fun setColor(color: Int)
    fun setOnColorSelectListener(listener: OnColorSelectListener)
    fun setOnStartTimeSelectListener(listener: OnStartTimeSelectListener)
    fun setOnEndTimeSelectListener(listener: OnEndTimeSelectListener)
    fun setOnEventNameClickListener(listener: OnEventNameClickListener)
    fun setOnEventDescriptionClickListener(listener: OnEventDescriptionClickListener)
}