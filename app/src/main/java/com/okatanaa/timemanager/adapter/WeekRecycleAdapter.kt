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

class WeekRecycleAdapter(val context: Context, val week: ArrayList<Day>) : RecyclerView.Adapter<WeekRecycleAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.week_item, parent, false)

        val eventListView = view.findViewById<ListView>(R.id.eventListView)


        return Holder(view)
    }

    override fun getItemCount(): Int {
        return week.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindDay(week[position], context)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayName = itemView?.findViewById<TextView>(R.id.day_name)
        val dayListView = itemView?.findViewById<ListView>(R.id.eventListView)
        val eventAddBtn = itemView?.findViewById<Button>(R.id.addEventBtn)

        fun bindDay(day: Day, context: Context) {
            dayListView?.adapter = DayListAdapter(context, day)
            dayName?.text = day.title

            eventAddBtn.setOnClickListener {
                Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()
            }
        }
    }
}