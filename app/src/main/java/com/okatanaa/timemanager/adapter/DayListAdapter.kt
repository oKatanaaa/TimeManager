package com.okatanaa.timemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.interfaces.OnEventClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import kotlinx.android.synthetic.main.day_item.view.*

class DayListAdapter(val context: Context, val day: Day, val onEventClickListener: OnEventClickListener) : BaseAdapter() {
    val selectedViews: MutableSet<Int> = mutableSetOf()
    var pickedEventPosition = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        synchronized(day) {
            val eventView: View
            val holder: ViewHolder


            if (convertView == null) {
                eventView = LayoutInflater.from(context).inflate(R.layout.day_item, null)
                holder = ViewHolder()
                holder.eventNameTxt = eventView.findViewById(R.id.eventNameTxt)
                holder.startTimeTxt = eventView.findViewById(R.id.startTimeLbl)
                holder.endTimeTxt = eventView.findViewById(R.id.endTimeLbl)
                holder.eventColor = eventView.findViewById(R.id.eventColor)
                eventView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                eventView = convertView
            }

            val event = day.getEvent(position)
            holder.eventNameTxt?.text = event.name
            holder.startTimeTxt?.text = event.startTime.toString()
            holder.endTimeTxt?.text = event.endTime.toString()
            when(event.color) {
                Event.BLUE -> holder.eventColor?.setImageResource(R.drawable.color_circle_blue)
                Event.GREEN -> holder.eventColor?.setImageResource(R.drawable.color_circle_green)
                Event.NONE -> holder.eventColor?.setImageResource(R.drawable.color_circle_null)
                Event.RED -> holder.eventColor?.setImageResource(R.drawable.color_circle_red)
                Event.YELLOW -> holder.eventColor?.setImageResource(R.drawable.color_circle_yellow)
            }
            // Make events in the list clickable!
            // We need to update listener each time because otherwise
            // event activity would get another event(not one you clicked on)
            eventView.setOnClickListener { onEventClickListener.onEventClicked(event, this, position) }
            eventView.isLongClickable = true



            if (this.selectedViews.contains(position))
                eventView.dayItemLayout.setBackgroundResource(R.drawable.day_item_selected)
            else if (event.isCurrent)
                eventView.dayItemLayout.setBackgroundResource(R.drawable.day_item_is_current)
            else
                eventView.dayItemLayout.setBackgroundResource(R.drawable.day_item_not_selected)


            //println("Event position ${position} isSelected: ${eventView.isSelected} setContains: ${this.selectedViews.contains(position)}")


            return eventView
        }
    }

    override fun getItem(position: Int): Any {
        synchronized(day) {
            return day.getEvent(position)
        }
    }


    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return day.eventCount()
    }


    /*
    * The adapter contains a list of selected views in order to draw them correctly.
    * After creating a view adapter checks if the view at this position is selected and if it is,
    * adapter changes view's background.
    * This functions add selection of a view at the particular position.
     */
    fun addSelectedView(position: Int) {
        this.selectedViews.add(position)
        println(this.selectedViews)
    }

    /*
    * The adapter contains a list of selected views in order to draw them correctly.
    * This functions deletes selection of a view at the particular position.
     */
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
        var eventColor: ImageView? = null
    }

    fun addEvent() {
        day.addNewEvent(Event("Empty event"))
    }
}