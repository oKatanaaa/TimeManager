package com.okatanaa.timemanager.fragments.DayFragment.views

import android.widget.ListView
import com.okatanaa.timemanager.fragments.DayFragment.views.additional_interfaces.OnEventUIClickListener
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.viewmvp.ViewMvp

interface DayFragmentView: ViewMvp {
    fun setDayName(name: String)
    fun setDayCurrent()
    fun getEventListView(): ListView
    fun bindEventList(eventList: ArrayList<Event>)
    fun hideEventInteractionUI()
    fun showEventInteractionUI()
    fun setOnEventUIClickListener(listener: OnEventUIClickListener)
}