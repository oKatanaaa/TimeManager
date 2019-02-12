package com.okatanaa.timemanager.activity.MainActivity.views

import android.support.v4.view.ViewPager
import android.widget.ListView
import com.okatanaa.timemanager.activity.MainActivity.views.additional_interfaces.OnWeekUIClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.viewmvp.ViewMvp

interface MainView: ViewMvp {
    fun bindWeekList(weekList: ArrayList<String>)
    fun getViewPager(): ViewPager
    fun getWeekListView(): ListView
    fun setOnWeekUIClickListener(listener: OnWeekUIClickListener)
    fun bindDayList(dayList: ArrayList<Day>)
}