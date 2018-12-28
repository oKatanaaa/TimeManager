package com.okatanaa.timemanager.fragments.views

import android.widget.ListView
import com.okatanaa.timemanager.fragments.presenter.event_ui_interaction.OnEventUIClickListener
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.viewmvp.ViewMvp

interface DayFragmentView: ViewMvp {
    fun setDayName(name: String)
    fun setDayCurrent()
    fun getEventList(): ListView
    fun bindEventList(eventList: ArrayList<Event>)
    fun hideEventInteractionUI()
    fun showEventInteractionUI()
    fun setOnEventUIClickListener(listener: OnEventUIClickListener)
}