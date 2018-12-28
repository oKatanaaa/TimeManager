package com.okatanaa.timemanager.activity.presenter.week_ui_interaction

import android.widget.AdapterView
import com.okatanaa.timemanager.activity.adapters.WeekListAdapter
import com.okatanaa.timemanager.model.Week

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