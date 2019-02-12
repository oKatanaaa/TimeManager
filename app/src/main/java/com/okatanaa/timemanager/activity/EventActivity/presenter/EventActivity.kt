package com.okatanaa.timemanager.activity.EventActivity.presenter

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
import com.okatanaa.timemanager.activity.EventActivity.view.EventView
import com.okatanaa.timemanager.activity.EventActivity.view.EventViewImpl
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.utils.*
import kotlinx.android.synthetic.main.activity_event_content.*
import org.json.JSONObject
import kotlin.IllegalArgumentException


class EventActivity : AppCompatActivity(),
    EventView.OnColorSelectListener,
    EventView.OnEndTimeSelectListener,
    EventView.OnStartTimeSelectListener {

    lateinit var event : Event


    var topTimeBorder = 0
    var bottomTimeBorder = Time.MINUTES_IN_DAY

    var currentStartTime = 0
    var currentEndTime = Time.MINUTES_IN_DAY

    var eventPosition = 0
    lateinit var eventView: EventView
    lateinit var descriptionAndNameEventEditor: DescriptionAndNameEventEditor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.eventView = EventViewImpl(this, null)
        setContentView(this.eventView.getRootView())
        setSupportActionBar(toolbar)

        setUpEventData()
        setUpTextViews()
        this.eventView.setColor(this.event.color)
        this.eventView.setOnColorSelectListener(this)

        this.descriptionAndNameEventEditor = DescriptionAndNameEventEditor(this, this.eventView, this.event)
        this.eventView.setOnEventNameClickListener(this.descriptionAndNameEventEditor)
        this.eventView.setOnEventDescriptionClickListener(this.descriptionAndNameEventEditor)
        this.eventView.setOnStartTimeSelectListener(this)
        this.eventView.setOnEndTimeSelectListener(this)
    }

    private fun setUpEventData() {
        val eventJsonString = intent.getStringExtra(EXTRA_EVENT_JSON)
        val eventJson = JSONObject(eventJsonString)
        this.event = JsonHelper.eventFromJson(eventJson)
        this.topTimeBorder = intent.getIntExtra(EXTRA_TOP_TIME_BORDER, this.topTimeBorder)
        this.bottomTimeBorder = intent.getIntExtra(EXTRA_BOTTOM_TIME_BORDER, this.bottomTimeBorder)
        this.currentStartTime = event.startTime.toMinutes()
        this.currentEndTime = event.endTime.toMinutes()
        this.eventPosition = intent.getIntExtra(EXTRA_EVENT_POSITION, 0)
    }

    private fun setUpTextViews() {
        this.eventView.setEventName(event.name)
        this.eventView.setDescription(event.description)
        this.eventView.setDayName(event.inDay.toString())
        this.eventView.setStartTime(this.event.startTime.toString())
        this.eventView.setEndTime(this.event.endTime.toString())
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

    override fun onStartTimeSelected(hourOfDay: Int, minute: Int) {
        val newTime = Time(hourOfDay, minute)

        if (newTime.isBetween(Time(this.topTimeBorder), Time(this.currentEndTime))) {
            this.currentStartTime = newTime.toMinutes()
            this.eventView.setStartTime(newTime.toString())
        } else
            this.eventView.showToast("Incorrect time value!", Toast.LENGTH_SHORT)
    }

    override fun onEndTimeSelected(hourOfDay: Int, minute: Int) {
        val newTime = Time(hourOfDay, minute)

        if (newTime.isBetween(Time(this.currentStartTime), Time(this.bottomTimeBorder))) {
            this.currentEndTime = newTime.toMinutes()
            this.eventView.setEndTime(newTime.toString())
        } else
           this.eventView.showToast("Incorrect time value!", Toast.LENGTH_SHORT)
    }

    override fun onColorSelected(color: Int) {
        this.event.color = color
    }

    private fun actionDeleteEvent(): Boolean {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_ACTION, ACTION_DELETE)
        resultIntent.putExtra(EXTRA_EVENT_POSITION, this.eventPosition)
        setResult(Activity.RESULT_OK, resultIntent)
        println("Finish")
        finish()
        return true
    }

    private fun actionSaveEvent(): Boolean {
        // Save changed data
        event.name = eventNameTxt.text.toString()
        event.description = eventDescriptionTxt.text.toString()
        event.smartSetStartTime(Time(this.currentStartTime))
        event.smartSetEndTime(Time(this.currentEndTime))
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_EVENT_JSON, JsonHelper.eventToJson(event).toString())
        resultIntent.putExtra(EXTRA_ACTION, ACTION_SAVE)
        resultIntent.putExtra(EXTRA_EVENT_POSITION, this.eventPosition)
        setResult(Activity.RESULT_OK, resultIntent)
        println("Finish")
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            RC_TEXT_EDITOR_ACTIVITY ->
                this.descriptionAndNameEventEditor.receivingResults(resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

}
