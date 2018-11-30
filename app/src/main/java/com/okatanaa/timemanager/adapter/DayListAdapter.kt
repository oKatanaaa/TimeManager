package com.okatanaa.timemanager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import kotlinx.android.synthetic.main.day_item.view.*

class DayListAdapter(val context: Context, val day: Day, val eventClick: (Event) -> Unit) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val eventView: View
        val holder: ViewHolder

        if(convertView == null) {
            eventView = LayoutInflater.from(context).inflate(R.layout.day_item, null)
            holder = ViewHolder()
            holder.eventName = eventView.findViewById(R.id.eventName)
            eventView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            eventView = convertView
        }


        val event = day.getEvent(position)
        holder.eventName?.text = event.title

        // Make events in the list clickable!
        // We need to update listener each time because otherwise
        // event activity would get another event(not one you clicked on)
        eventView.setOnClickListener { eventClick(event) }

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
        var eventName: TextView? = null
    }

    fun addEvent() {
        day.addEvent(Event("Empty event"))
    }
}