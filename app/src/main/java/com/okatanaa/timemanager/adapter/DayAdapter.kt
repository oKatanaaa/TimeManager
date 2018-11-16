package com.okatanaa.timemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Day

class DayAdapter(val context: Context, val day: Day) : BaseAdapter() {
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
}