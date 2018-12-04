package com.okatanaa.timemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event

class DayListAdapter(val context: Context, val day: Day, val eventClick: (Event, DayListAdapter) -> Unit) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val eventView: View
        val holder: ViewHolder

        if(convertView == null) {
            eventView = LayoutInflater.from(context).inflate(R.layout.day_item, null)
            holder = ViewHolder()
            holder.eventNameTxt = eventView.findViewById(R.id.eventNameTxt)
            holder.startTimeTxt = eventView.findViewById(R.id.startTimeTxt)
            holder.endTimeTxt = eventView.findViewById(R.id.endTimeTxt)
            eventView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            eventView = convertView
        }

        val event = day.getEvent(position)
        holder.eventNameTxt?.text = event.name
        holder.startTimeTxt?.text = "${event.startTime[0].toString()}:${event.startTime[1]}"
        holder.endTimeTxt?.text = "${event.endTime[0].toString()}:${event.endTime[1]}"

        // Make events in the list clickable!
        // We need to update listener each time because otherwise
        // event activity would get another event(not one you clicked on)
        eventView.setOnClickListener { eventClick(event, this) }
        eventView.isLongClickable = true

        return eventView
    }

    override fun getItem(position: Int): Any {
        return day.getEvent(position)
    }


    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return day.eventCount()
    }


    private class ViewHolder {
        var eventNameTxt: TextView? = null
        var startTimeTxt: TextView? = null
        var endTimeTxt: TextView? = null
    }

    fun addEvent() {
        day.addEvent(Event("Empty event"))
    }
}