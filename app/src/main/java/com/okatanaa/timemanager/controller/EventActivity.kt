package com.okatanaa.timemanager.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Event
import kotlinx.android.synthetic.main.activity_event.*
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.okatanaa.timemanager.additional_classes.TextClickedListener
import com.okatanaa.timemanager.controller.fragments.TimePickerFragment
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.utilities.*
import kotlinx.android.synthetic.main.content_event.*
import org.json.JSONObject
import kotlin.IllegalArgumentException
import kotlin.math.round


class EventActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {


    lateinit var event : Event
    companion object {
        const val EVENT_NAME = "Event Name"
        const val DESCRIPTION = "Description"
        const val START_TIME = "Start time"
        const val END_TIME = "End time"

        const val NO_CHANGE = "No change"
    }


    var topTimeBorder = 0
    var bottomTimeBorder = Time.MINUTES_IN_DAY

    var currentStartTime = 0
    var currentEndTime = Time.MINUTES_IN_DAY

    var changingTime = NO_CHANGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        setSupportActionBar(toolbar)
        setEventData()
        setTextViews()
    }

    fun setEventData() {
        val eventJsonString = intent.getStringExtra(EXTRA_EVENT_JSON)
        val eventJson = JSONObject(eventJsonString)
        this.event = JsonHelper.eventFromJson(eventJson)
        this.topTimeBorder = intent.getIntExtra(EXTRA_TOP_TIME_BORDER, this.topTimeBorder)
        this.bottomTimeBorder = intent.getIntExtra(EXTRA_BOTTOM_TIME_BORDER, this.bottomTimeBorder)
        this.currentStartTime = event.startTime.toMinutes()
        this.currentEndTime = event.endTime.toMinutes()
    }

    fun setTextViews() {
        eventNameTxt.text = event.name
        eventNameTxt.setOnClickListener{TextClickedListener.onClick(this, EVENT_NAME, eventNameTxt.text.toString())}

        eventDescriptionTxt.text = event.description
        eventDescriptionTxt.setOnClickListener{TextClickedListener.onClick(this, DESCRIPTION, eventDescriptionTxt.text.toString())}
        inWhatDayTxt.text = event?.inDay.toString()

        startTimeDynamicTxt.text = this.event.startTime.toString()
        startTimeDynamicTxt.setOnClickListener {
            this.changingTime = START_TIME
            val timeBundle = Bundle()
            timeBundle.putInt(BUNDLE_TIME, this.currentStartTime)
            val timeFragment = TimePickerFragment()
            timeFragment.arguments = timeBundle
            timeFragment.show(supportFragmentManager, START_TIME)
        }

        endTimeDynamicTxt.text = this.event.endTime.toString()
        endTimeDynamicTxt.setOnClickListener {
            this.changingTime = END_TIME
            val timeBundle = Bundle()
            timeBundle.putInt(BUNDLE_TIME, this.currentEndTime)
            val timeFragment = TimePickerFragment()
            timeFragment.arguments = timeBundle
            timeFragment.show(supportFragmentManager, END_TIME)
        }
    }

    fun updateTimeFields() {
        startTimeDynamicTxt.text = Time(this.currentStartTime).toString()
        endTimeDynamicTxt.text = Time(this.currentEndTime).toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_event -> actionDeleteEvent()
            R.id.action_save_event -> actionSaveEvent()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun actionDeleteEvent(): Boolean {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_ACTION, ACTION_DELETE)
        setResult(Activity.RESULT_OK, resultIntent)
        println("Finish")
        finish()
        return true
    }

    fun actionSaveEvent(): Boolean {
        // Save changed data
        event.name = eventNameTxt.text.toString()
        event.description = eventDescriptionTxt.text.toString()
        event.smartSetStartTime(Time(this.currentStartTime))
        event.smartSetEndTime(Time(this.currentEndTime))
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_EVENT_JSON, JsonHelper.eventToJson(event).toString())
        resultIntent.putExtra(EXTRA_ACTION, ACTION_SAVE)
        setResult(Activity.RESULT_OK, resultIntent)
        println("Finish")
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK)
            when(data?.getStringExtra(EXTRA_EDITED_NAME)) {
                EVENT_NAME->
                    eventNameTxt.text = data?.getStringExtra(EXTRA_EDITED_VALUE)
                DESCRIPTION ->
                    eventDescriptionTxt.text = data?.getStringExtra(EXTRA_EDITED_VALUE)
                else ->
                    throw IllegalArgumentException("Unknown edited name!")
            }
    }

    /*
    * This function is called when user presses OK btn on the TimePickerFragment(the dialog pops up when user presses
    * any of time textviews: starttime or endtime),
    * so the application has to change current start and end time
    * and also change seekbars' progresses.
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        when(this.changingTime) {
            START_TIME -> {
                val newTime = Time(hourOfDay, minute)

                if (newTime.isBetween(Time(this.topTimeBorder), Time(this.currentEndTime))) {
                    this.currentStartTime = newTime.toMinutes()
                    startTimeDynamicTxt.text = newTime.toString()
                } else
                    Toast.makeText(this, "Incorrect time value!", Toast.LENGTH_SHORT).show()
            }
            END_TIME -> {
                val newTime = Time(hourOfDay, minute)

                if (newTime.isBetween(Time(this.currentStartTime), Time(this.bottomTimeBorder))) {
                    this.currentEndTime = newTime.toMinutes()
                    endTimeDynamicTxt.text = newTime.toString()
                } else
                    Toast.makeText(this, "Incorrect time value!", Toast.LENGTH_SHORT).show()
            }
            else -> throw IllegalArgumentException("Unknown changing type! Passed: ${this.changingTime}")
        }

        this.changingTime = NO_CHANGE
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

}
