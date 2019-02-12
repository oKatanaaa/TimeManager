package com.okatanaa.timemanager.activity.EventActivity.view

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Time

class EventViewImpl: EventView, AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    private val context: Context
    private val rootView: View
    private val eventName: TextView
    private val dayName: TextView
    private val description: TextView
    private val startTime: TextView
    private val endTime: TextView
    private val colorSpinner: Spinner

    private var startTimeSelectListener: EventView.OnStartTimeSelectListener? = null
    private var endTimeSelectListener: EventView.OnEndTimeSelectListener? = null
    private var colorSelectListener: EventView.OnColorSelectListener? = null

    enum class TimeEditState { END_TIME, START_TIME, NONE }
    private var timeEditState: TimeEditState = TimeEditState.NONE

    constructor(context: Context, container: ViewGroup?) {
        this.context = context
        this.rootView = LayoutInflater.from(context).inflate(R.layout.activity_event, container)

        this.eventName = this.rootView.findViewById(R.id.eventNameTxt)
        this.dayName = this.rootView.findViewById(R.id.dayNameTxt)
        this.description = this.rootView.findViewById(R.id.eventDescriptionTxt)

        this.startTime = this.rootView.findViewById(R.id.startTimeTxt)
        this.startTime.setOnClickListener {
            this.timeEditState = TimeEditState.START_TIME
            showTimePicker(it as TextView)
        }

        this.endTime = this.rootView.findViewById(R.id.endTimeTxt)
        this.endTime.setOnClickListener {
            this.timeEditState = TimeEditState.END_TIME
            showTimePicker(it as TextView)

        }

        this.colorSpinner = this.rootView.findViewById(R.id.colorSpinner)
        ArrayAdapter.createFromResource(this.context, R.array.colors, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.colorSpinner.adapter = adapter
            }
        this.colorSpinner.onItemSelectedListener = this
    }

    override fun getRootView(): View {
        return this.rootView
    }

    override fun getViewState(): Bundle {
       return Bundle()
    }

    override fun showToast(msg: String, length: Int) {
        Toast.makeText(this.context, msg, length).show()
    }

    override fun setEventName(name: String) {
        this.eventName.text = name
    }

    override fun setDayName(name: String) {
        this.dayName.text = name
    }

    override fun setDescription(description: String) {
        this.description.text = description
    }

    override fun setStartTime(time: String) {
        this.startTime.text = time
    }

    override fun setEndTime(time: String) {
        this.endTime.text = time
    }

    override fun setColor(color: Int) {
        this.colorSpinner.setSelection(color)
    }

    override fun setOnColorSelectListener(listener: EventView.OnColorSelectListener) {
        this.colorSelectListener = listener
    }

    override fun setOnStartTimeSelectListener(listener: EventView.OnStartTimeSelectListener) {
        this.startTimeSelectListener = listener
    }

    override fun setOnEndTimeSelectListener(listener: EventView.OnEndTimeSelectListener) {
        this.endTimeSelectListener = listener
    }

    override fun setOnEventNameClickListener(listener: EventView.OnEventNameClickListener) {
        this.eventName.setOnClickListener {
            listener.onEventNameClicked(this.eventName.text.toString()) }
    }

    override fun setOnEventDescriptionClickListener(listener: EventView.OnEventDescriptionClickListener) {
        this.description.setOnClickListener {
            listener.onEventDescriptionClicked(this.eventName.text.toString()) }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // AdapterView.OnItemSelectedListener interface requires implementing this function.
        // We don't need to do something, so we do nothing. Yes.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Send information about choosed color
        this.colorSelectListener?.onColorSelected(position)
        this.colorSpinner.setSelection(position)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(this.timeEditState == TimeEditState.START_TIME)
            this.startTimeSelectListener?.onStartTimeSelected(hourOfDay, minute)
        if(this.timeEditState == TimeEditState.END_TIME)
            this.endTimeSelectListener?.onEndTimeSelected(hourOfDay, minute)

        this.timeEditState = TimeEditState.NONE
    }

    private fun showTimePicker(timeView: TextView) {
        val time = Time(timeView.text.toString())
        TimePickerDialog(
            this.context,
            this,
            time.hours,
            time.minutes,
            DateFormat.is24HourFormat(this.context)).show()
    }

}