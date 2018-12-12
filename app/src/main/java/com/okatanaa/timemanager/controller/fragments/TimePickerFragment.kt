package com.okatanaa.timemanager.controller.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.utilities.BUNDLE_TIME

class TimePickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val time = Time(arguments!![BUNDLE_TIME] as Int)
        return TimePickerDialog(activity, activity as TimePickerDialog.OnTimeSetListener, time.hours, time.minutes,
            android.text.format.DateFormat.is24HourFormat(activity))
    }
}