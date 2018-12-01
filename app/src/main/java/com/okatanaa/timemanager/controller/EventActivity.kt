package com.okatanaa.timemanager.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.utilities.EXTRA_EVENT
import kotlinx.android.synthetic.main.activity_event.*
import android.app.Activity
import android.content.Intent
import android.view.View


class EventActivity : AppCompatActivity() {

    lateinit var event : Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        event = intent.getParcelableExtra<Event>(EXTRA_EVENT)

        // Set event name
        eventTitleTxt.setText(event.title)
        // Set event description
        eventDescriptionTxt.setText(event.description)
    }

    fun onClickedDoneBtn(view: View) {
        // Save changed data
        // Save new event name
        event.title = eventTitleTxt.text.toString()
        // Save new event description
        event.description = eventDescriptionTxt.text.toString()

        // Paste data to Intent
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_EVENT, this.event)
        setResult(Activity.RESULT_OK, resultIntent)
        println("Finish")
        finish()
    }

}
