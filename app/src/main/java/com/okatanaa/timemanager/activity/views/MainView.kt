package com.okatanaa.timemanager.activity.views

import android.support.v4.view.ViewPager
import android.widget.ListView
import com.okatanaa.timemanager.fragments.presenter.DayFragment
import com.okatanaa.timemanager.activity.presenter.week_ui_interaction.OnWeekUIClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.viewmvp.ViewMvp

interface MainView: ViewMvp {
    fun bindWeekList(weekList: ArrayList<String>)
    fun getViewPager(): ViewPager
    fun getWeekListView(): ListView
    fun setOnWeekUIClickListener(listener: OnWeekUIClickListener)
    fun bindDayList(dayList: ArrayList<Day>)
}