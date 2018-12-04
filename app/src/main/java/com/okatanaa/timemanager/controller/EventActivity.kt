package com.okatanaa.timemanager.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Event
import kotlinx.android.synthetic.main.activity_event.*
import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.okatanaa.timemanager.additional_classes.TextClickedListener
import com.okatanaa.timemanager.services.JsonHelper
import com.okatanaa.timemanager.utilities.*
import kotlinx.android.synthetic.main.content_event.*
import org.json.JSONObject


class EventActivity : AppCompatActivity() {

    lateinit var event : Event
    val EVENT_NAME = "Event Name"
    val DESCRIPTION = "Description"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        setSupportActionBar(toolbar)

        val eventJsonString = intent.getStringExtra(EXTRA_EVENT_JSON)
        val eventJson = JSONObject(eventJsonString)
        event = JsonHelper.eventFromJson(eventJson)

        eventNameTxt.text = event.name
        eventNameTxt.setOnClickListener{TextClickedListener.onClick(this, "Event Name", eventNameTxt.text.toString())}

        eventDescriptionTxt.text = event.description
        eventDescriptionTxt.setOnClickListener{TextClickedListener.onClick(this, "Description", eventDescriptionTxt.text.toString())}
        inWhatDayTxt.text = event?.inDay.toString()
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
                    Unit
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

}
