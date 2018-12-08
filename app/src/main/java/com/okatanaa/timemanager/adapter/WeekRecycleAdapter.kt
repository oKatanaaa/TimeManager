package com.okatanaa.timemanager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.interfaces.OnEventLongClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week
import kotlinx.android.synthetic.main.week_item.view.*


/*
*
* eventClick parameter is a lambda, that I pass into weekAdapter in order
* to create a onClickListener for events lie in the lists(days) in the MainActivity.
* I need to do this to follow the ModelViewController pattern.
* Other words - all the UI interaction logic must be in the controller, that is
* MainActivity class. So I just declare the code that will be running at click event
* in the MainActivity and than pass it into this weekAdapter as an argument.
* This weekAdapter passes it into an inner weekAdapter(for the lists of events) and
* in them I implement this listener code for each element in the list.
*
* create onClickListener fun in MainActivity;
* pass it into WeekRecycleAdapter;
* from WeekRecycleAdapter pass it into DayListAdapter;
*
* MainActivity -> WeekRecycleAdapter -> DayListAdapter
 */
class WeekRecycleAdapter(val context: Context, val week: Week,
                         val onEventClickListener: OnEventClickListener,
                         val onEventLongClickListener: OnEventLongClickListener)
    : RecyclerView.Adapter<WeekRecycleAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.week_item, parent, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return week.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindDay(week.getDay(position), context, onEventClickListener)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayName = itemView?.findViewById<TextView>(R.id.day_name)
        public val dayListView = itemView?.findViewById<ListView>(R.id.eventListView)
        val eventAddBtn = itemView?.findViewById<Button>(R.id.addEventBtn)
        var isBinded = false

        fun bindDay(day: Day, context: Context, onEventClickListener: OnEventClickListener) {
            dayName?.text = "${day.title}, ${day.todaysDate} ${day.month}"

            if(day.isToday)
                this.itemView.dayLayout.setBackgroundResource(R.drawable.week_item_today_look)
            else
                this.itemView.dayLayout.setBackgroundResource(R.drawable.week_item_look)

            val adapter = DayListAdapter(context, day, onEventClickListener)
            dayListView?.adapter = adapter
            dayListView.setOnItemLongClickListener { parent, view, position, id ->
                onEventLongClickListener.onEventLongClicked(parent as @kotlin.ParameterName(name = "parent") AdapterView<DayListAdapter>, view, position, id)}

            eventAddBtn.setOnClickListener{
                val newEvent = Event("Event ${day.eventCount()}")
                day.addNewEvent(newEvent)
                (dayListView.adapter as DayListAdapter).notifyDataSetChanged()
                Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()
            }
            isBinded = true
        }
         fun updateWithPayLoad(payloads: MutableList<Any>) {
             if(payloads.isEmpty())
                 return

             (dayListView.adapter as DayListAdapter).notifyDataSetChanged()
         }
    }
}