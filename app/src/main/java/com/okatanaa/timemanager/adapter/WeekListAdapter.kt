package com.okatanaa.timemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.interfaces.OnWeekClickListener
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.DataService

class WeekListAdapter(val context: Context, val weeks: ArrayList<Week>, val onWeekClickListener: OnWeekClickListener) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val weekView: View
            val holder: ViewHolder


            if (convertView == null) {
                weekView = LayoutInflater.from(context).inflate(R.layout.week_list_item, null)
                holder = ViewHolder()
                holder.weekNameTxt = weekView.findViewById(R.id.weekNameTxt)
                weekView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                weekView = convertView
            }

            val week = this.weeks[position]
            holder.weekNameTxt?.text = week.name

            // Make events in the list clickable!
            // We need to update listener each time because otherwise
            // event activity would get another event(not one you clicked on)
            weekView.setOnClickListener { onWeekClickListener.onWeekClicked(week, this, position) }

            if(week == DataService.currentWeek)
                weekView.setBackgroundResource(R.drawable.week_current_look)
            else
                weekView.setBackgroundResource(R.drawable.week_not_current_look)

            return weekView
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