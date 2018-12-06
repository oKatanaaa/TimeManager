package com.okatanaa.timemanager.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import kotlinx.android.synthetic.main.day_item.view.*

class DayListAdapter(val context: Context, val day: Day, val onEventClickListener: OnEventClickListener) : BaseAdapter() {
    val selectedViews: MutableSet<Int> = mutableSetOf()

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
        eventView.setOnClickListener { onEventClickListener.onEventClicked(event, this, position) }
        eventView.isLongClickable = true
        if(this.selectedViews.contains(position))
            eventView.dayItemLayout.setBackgroundResource(R.drawable.day_item_selected)
        else
            eventView.dayItemLayout.setBackgroundResource(R.drawable.day_item_not_selected)

        println("Event position ${position} isSelected: ${eventView.isSelected} setContains: ${this.selectedViews.contains(position)}")

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

    fun addSelectedView(position: Int) {
        this.selectedViews.add(position)
        println(this.selectedViews)
    }

    fun removeSelectedView(position: Int) {
        if(!this.selectedViews.contains(position))
            throw NoSuchElementException("This view is not selected!")

        this.selectedViews.remove(position)
    }

    fun removeAllSelectedViews() {
        this.selectedViews.clear()
    }

    fun getSelectedViewsCount() = this.selectedViews.size


    private class ViewHolder {
        var eventNameTxt: TextView? = null
        var startTimeTxt: TextView? = null
        var endTimeTxt: TextView? = null
    }

    fun addEvent() {
        day.addEvent(Event("Empty event"))
    }
}