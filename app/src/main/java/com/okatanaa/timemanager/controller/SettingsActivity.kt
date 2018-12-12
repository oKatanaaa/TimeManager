package com.okatanaa.timemanager.controller

import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.additional_classes.TextClickedListener
import com.okatanaa.timemanager.controller.fragments.TimePickerFragment
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.services.Settings
import com.okatanaa.timemanager.utilities.BUNDLE_TIME
import com.okatanaa.timemanager.utilities.EXTRA_EDITED_VALUE
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        globalStartTimeTxt.text = Settings.globalStartTime.toString()
    }

    fun onStartingTimeClicked(view: View) {
        val globalTime = Time(this.globalStartTimeTxt.text.toString())
        val timeBundle = Bundle()
        timeBundle.putInt(BUNDLE_TIME, globalTime.minutes)
        val timeFragment = TimePickerFragment()
        timeFragment.arguments = timeBundle
        timeFragment.show(supportFragmentManager, "Global start time")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val newTime = Time(hourOfDay, minute)
        Settings.globalStartTime = newTime
        globalStartTimeTxt.text = newTime.toString()
    }
}
