package com.okatanaa.timemanager.activity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.DataService

class WeekListAdapter(val context: Context) : BaseAdapter() {

    private var weekList: ArrayList<String>? = null
    private val selectedViews: MutableSet<Int> = mutableSetOf(0)

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

            holder.weekNameTxt?.text = this.weekList!![position]

            if(this.selectedViews.contains(position))
                weekView.setBackgroundResource(R.drawable.week_current_look)
            else
                weekView.setBackgroundResource(R.drawable.week_not_current_look)

            return weekView
    }

    fun bindWeekList(weekList: ArrayList<String>) {
        this.weekList = weekList
    }

    override fun getItem(position: Int): Any {
        return this.weekList!![position]
    }


    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        if(this.weekList == null)
            return 0
        else
            return this.weekList!!.size
    }

    /**
    * The adapter contains a list of selected views in order to draw them correctly.
    * After creating a view adapter checks if the view at this position is selected and if it is,
    * adapter changes view's background.
    * This functions add selection of a view at the particular position.
     */
    fun addSelectedView(position: Int) {
        this.selectedViews.add(position)
        println(this.selectedViews)
    }

    /**
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
        var weekNameTxt: TextView? = null
    }
}