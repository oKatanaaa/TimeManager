package com.okatanaa.timemanager.activity.EventActivity.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.okatanaa.timemanager.activity.EventActivity.view.EventView
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.utils.EXTRA_EDITED_NAME
import com.okatanaa.timemanager.utils.EXTRA_EDITED_VALUE
import com.okatanaa.timemanager.utils.TextEditorActivity.TextEditorActivity


/**
 * This is the helper class which is responsible for changing
 * event name and its description. It was made in order not to make
 * activity class too big and also to separate event interaction logic a bit.
 */
class DescriptionAndNameEventEditor:
    EventView.OnEventNameClickListener,
    EventView.OnEventDescriptionClickListener {

    private val context: Context
    private val eventView: EventView
    private val event: Event

    constructor(context: Context, eventView: EventView, event: Event) {
        this.context = context
        this.eventView = eventView
        this.event = event
    }

    override fun onEventNameClicked(currentText: String) {
        TextEditorActivity.launchTextEditor(
            this.context,
            EVENT_NAME,
            currentText
        )
    }

    override fun onEventDescriptionClicked(currentText: String) {
        TextEditorActivity.launchTextEditor(
            this.context,
            DESCRIPTION,
            currentText
        )
    }

    fun receivingResults(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
            return

        when (data?.getStringExtra(EXTRA_EDITED_NAME)) {
            EVENT_NAME -> {
                val newName = data?.getStringExtra(EXTRA_EDITED_VALUE)
                this.eventView.setEventName(newName)
                this.event.name = newName
            }
            DESCRIPTION -> {
                val newDescription = data?.getStringExtra(EXTRA_EDITED_VALUE)
                this.eventView.setDescription(newDescription)
                this.event.description = newDescription
            }
            else ->
                throw IllegalArgumentException("Unknown edited name!")
        }
    }

    companion object {
        const val EVENT_NAME = "Event Name"
        const val DESCRIPTION = "Description"
    }
}