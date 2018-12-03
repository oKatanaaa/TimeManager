package com.okatanaa.timemanager.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.utilities.EXTRA_EVENT_JSON
import kotlinx.android.synthetic.main.activity_event.*
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.okatanaa.timemanager.services.JsonHelper
import org.json.JSONObject


class EventActivity : AppCompatActivity() {

    lateinit var event : Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        val eventJsonString = intent.getStringExtra(EXTRA_EVENT_JSON)
        val eventJson = JSONObject(eventJsonString)
        event = JsonHelper.eventFromJson(eventJson)

        eventNameTxt.setText(event.name)
        eventDescriptionTxt.setText(event.description)
        inWhatDayTxt.text = event.inDay

        setSpinner()
    }

    fun setSpinner() {
        val optionsSpinner = findViewById<Spinner>(R.id.optionsSpinner)
        val options = arrayListOf("Delete event")
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.options, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        optionsSpinner.adapter = spinnerAdapter
        optionsSpinner.onItemSelectedListener = SpinnerListener()
    }

    fun onClickedDoneBtn(view: View){
        // Save changed data
        event.name = eventNameTxt.text.toString()
        event.description = eventDescriptionTxt.text.toString()
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_EVENT_JSON, JsonHelper.eventToJson(event).toString())
        setResult(Activity.RESULT_OK, resultIntent)
        println("Finish")
        finish()
    }

    class SpinnerListener: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val text = parent?.getItemAtPosition(position).toString()
            Toast.makeText(parent!!.context, text, Toast.LENGTH_SHORT).show()

        }

    }

}
