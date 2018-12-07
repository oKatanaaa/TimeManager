package com.okatanaa.timemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.interfaces.OnWeekClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week
import kotlinx.android.synthetic.main.day_item.view.*

class WeekListAdapter(val context: Context, val weeks: ArrayList<Week>, val onWeekClickListener: OnWeekClickListener) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val eventView: View
            val holder: ViewHolder


            if (convertView == null) {
                eventView = LayoutInflater.from(context).inflate(R.layout.week_list_item, null)
                holder = ViewHolder()
                holder.weekNameTxt = eventView.findViewById(R.id.weekNameTxt)
                eventView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                eventView = convertView
            }

            val week = this.weeks[position]
            holder.weekNameTxt?.text = week.name

            // Make events in the list clickable!
            // We need to update listener each time because otherwise
            // event activity would get another event(not one you clicked on)
            eventView.setOnClickListener { onWeekClickListener.onWeekClicked(week, this, position) }


            //println("Event position ${position} isSelected: ${eventView.isSelected} setContains: ${this.selectedViews.contains(position)}")


            return eventView
    }

    override fun getItem(position: Int): Any {
        return this.weeks[position]
    }


    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return weeks.count()
    }

    private class ViewHolder {
        var weekNameTxt: TextView? = null
    }
}