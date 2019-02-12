package com.okatanaa.timemanager.activity.MainActivity.views.additional_interfaces

import android.widget.AdapterView
import com.okatanaa.timemanager.activity.MainActivity.adapters.WeekListAdapter

interface OnWeekUIClickListener {

    interface OnWeekSelectedListener {
        fun onWeekSelected(weekPosition: Int)
    }

    fun onMoveWeekUpBtnClicked()
    fun onMoveWeekDownBtnClicked()
    fun onDeleteWeekBtnClicked()
    fun onAddWeekBtnClicked()
    fun onWeekItemSelected(parent: AdapterView<WeekListAdapter>, position: Int)
}