package com.okatanaa.timemanager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week


/*
*
* eventClick parameter is a lambda, that I pass into adapter in order
* to create a onClickListener for events lie in the lists(days) in the MainActivity.
* I need to do this to follow the ModelViewController pattern.
* Other words - all the UI interaction logic must be in the controller, that is
* MainActivity class. So I just declare the code that will be running at click event
* in the MainActivity and than pass it into this adapter as an argument.
* This adapter passes it into an inner adapter(for the lists of events) and
* in them I implement this listener code for each element in the list.
*
* create onClickListener fun in MainActivity;
* pass it into WeekRecycleAdapter;
* from WeekRecycleAdapter pass it into DayListAdapter;
*
* MainActivity -> WeekRecycleAdapter -> DayListAdapter
 */
class WeekRecycleAdapter(val context: Context, val week: Week, val eventClick: (Event) -> Unit, val addEventClick: (Day) -> Unit) : RecyclerView.Adapter<WeekRecycleAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.week_item, parent, false)

        return Holder(view, this.eventClick, this.addEventClick)
    }

    override fun getItemCount(): Int {
        return week.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindDay(week.getDay(position), context)
    }

    inner class Holder(itemView: View, val eventClick: (Event) -> Unit, val addEventClick: (Day) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val dayName = itemView?.findViewById<TextView>(R.id.day_name)
        val dayListView = itemView?.findViewById<ListView>(R.id.eventListView)
        val eventAddBtn = itemView?.findViewById<Button>(R.id.addEventBtn)

        fun bindDay(day: Day, context: Context) {
            dayListView?.adapter = DayListAdapter(context, day, eventClick)
            dayName?.text = day.title

            eventAddBtn.setOnClickListener { addEventClick(day) }
        }
    }
}