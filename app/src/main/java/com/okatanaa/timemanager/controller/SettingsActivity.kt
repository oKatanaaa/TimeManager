package com.okatanaa.timemanager.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.additional_classes.TextClickedListener
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.services.Settings
import com.okatanaa.timemanager.utilities.EXTRA_EDITED_VALUE
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        globalStartTimeTxt.text = Settings.globalStartTime.toString()
    }

    fun onStartingTimeClicked(view: View) {
        TextClickedListener.onClick(this, STARTING_TIME, globalStartTimeTxt.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val newTime = Time(data?.getStringExtra(EXTRA_EDITED_VALUE)!!)
            Settings.globalStartTime = newTime
            globalStartTimeTxt.text = newTime.toString()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "Wrong time value!", Toast.LENGTH_SHORT)
        }
    }

    companion object {
        val STARTING_TIME = "Starting time"
    }
}
