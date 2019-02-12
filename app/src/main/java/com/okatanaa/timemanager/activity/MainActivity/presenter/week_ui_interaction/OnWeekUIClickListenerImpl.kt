package com.okatanaa.timemanager.activity.MainActivity.presenter.week_ui_interaction

import android.widget.AdapterView
import android.widget.Toast
import com.okatanaa.timemanager.activity.MainActivity.adapters.WeekListAdapter
import com.okatanaa.timemanager.activity.MainActivity.views.MainView
import com.okatanaa.timemanager.activity.MainActivity.views.additional_interfaces.OnWeekUIClickListener
import com.okatanaa.timemanager.services.GlobalModel.GlobalModel

class OnWeekUIClickListenerImpl:
    OnWeekUIClickListener {

    private lateinit var adapter: WeekListAdapter
    private var selectedWeekPosition = 0


    private val globalModel: GlobalModel
    private val mainView: MainView
    private val onWeekSelectedListener: OnWeekUIClickListener.OnWeekSelectedListener

    constructor(globalModel: GlobalModel,
                mainView: MainView,
                onWeekSelectedListener: OnWeekUIClickListener.OnWeekSelectedListener) {
        this.globalModel = globalModel
        this.mainView = mainView
        this.onWeekSelectedListener = onWeekSelectedListener
    }

    override fun onMoveWeekUpBtnClicked() {
        this.adapter.removeAllSelectedViews()
        if(this.globalModel.moveWeekUp(this.selectedWeekPosition))
            this.selectedWeekPosition -= 1

        this.adapter.addSelectedView(this.selectedWeekPosition)
        this.adapter.notifyDataSetChanged()
    }

    override fun onMoveWeekDownBtnClicked() {
        this.adapter.removeAllSelectedViews()
        if(this.globalModel.moveWeekDown(this.selectedWeekPosition))
            this.selectedWeekPosition += 1

        this.adapter.addSelectedView(this.selectedWeekPosition)
        this.adapter.notifyDataSetChanged()
    }

    override fun onDeleteWeekBtnClicked() {
        if(!this.globalModel.deleteWeek(this.selectedWeekPosition)) {
            this.mainView.showToast("Must be at least one week!", Toast.LENGTH_SHORT)
            return
        }

        if(this.selectedWeekPosition == this.globalModel.getWeekCount()) {
            this.adapter.removeAllSelectedViews()
            this.selectedWeekPosition = this.selectedWeekPosition - 1
            this.adapter.addSelectedView(this.selectedWeekPosition)
        }

        this.adapter.notifyDataSetChanged()
    }

    override fun onAddWeekBtnClicked() {
        this.globalModel.addWeek()
        this.adapter.notifyDataSetChanged()
        this.mainView.showToast("Week added", Toast.LENGTH_SHORT)
    }

    override fun onWeekItemSelected(parent: AdapterView<WeekListAdapter>, position: Int) {
        this.adapter = parent.adapter
        this.selectedWeekPosition = position

        if (this.adapter.getSelectedViewsCount() > 0)
            this.adapter.removeAllSelectedViews()

        this.adapter.addSelectedView(position)
        this.adapter.notifyDataSetChanged()

        this.onWeekSelectedListener.onWeekSelected(position)
    }

}