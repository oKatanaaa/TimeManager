package com.okatanaa.timemanager.fragments.views

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.R.id.contentTestLayout
import com.okatanaa.timemanager.fragments.adapter.EventListAdapter
import com.okatanaa.timemanager.fragments.presenter.event_ui_interaction.OnEventUIClickListener
import com.okatanaa.timemanager.model.Event
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.day_fragment.view.*

class DayFragmentViewImpl: DayFragmentView {

    private val context: Context
    private val rootView: View
    private val dayName: TextView
    private val eventListView: ListView
    private val eventListAdapter: EventListAdapter
    private val dayLayout: ConstraintLayout
    private val moveEventUpBtn: Button
    private val moveEventDownBtn: Button
    private val deleteEventBtn: Button
    private val doneEventBtn: Button
    private val addEventBtn: Button
    private val moveLayout: ConstraintLayout
    private val fragmentLayout: ConstraintLayout

    constructor(context: Context, container: ViewGroup?) {
        this.context = context
        this.rootView = LayoutInflater.from(context).inflate(R.layout.day_fragment, container, false)
        this.dayName = this.rootView.findViewById(R.id.dayName)
        this.eventListView = this.rootView.findViewById(R.id.eventListView)
        this.eventListAdapter = EventListAdapter(context)
        this.eventListView.adapter = this.eventListAdapter
        this.dayLayout = this.rootView.dayLayout
        this.moveEventUpBtn = this.rootView.findViewById(R.id.moveEventUpBtn)
        this.moveEventDownBtn = this.rootView.findViewById(R.id.moveEventDownBtn)
        this.deleteEventBtn = this.rootView.findViewById(R.id.deleteEventBtn)
        this.doneEventBtn = this.rootView.findViewById(R.id.doneEventBtn)
        this.addEventBtn = this.rootView.findViewById(R.id.addEventBtn)
        this.moveLayout = this.rootView.moveLayout
        this.fragmentLayout = this.rootView.fragmentLayout
        hideEventInteractionUI()
    }

    override fun getRootView(): View {
        return this.rootView
    }

    override fun showToast(msg: String, length: Int) {
        Toast.makeText(this.context, msg, length).show()
    }

    override fun setDayName(name: String) {
        this.dayName.text = name
    }

    override fun setDayCurrent() {
        this.dayLayout.setBackgroundResource(R.drawable.week_item_look)
    }

    override fun getEventListView(): ListView {
        return this.eventListView
    }

    override fun bindEventList(eventList: ArrayList<Event>) {
        this.eventListAdapter.bindEventList(eventList)
    }

    override fun getViewState(): Bundle {
        return Bundle()
    }

    override fun hideEventInteractionUI() {
        this.moveLayout.alpha = 0F
        this.moveLayout.isEnabled = false

        this.moveEventUpBtn.alpha = 0F
        this.moveEventUpBtn.isEnabled = false
        this.doneEventBtn.alpha = 0F
        this.doneEventBtn.isEnabled = false
        this.moveEventDownBtn.alpha = 0F
        this.moveEventDownBtn.isEnabled = false
        disconnectMoveLayoutToDayLayout()
    }

    override fun showEventInteractionUI() {
        connectMoveLayoutToDayLayout()

        moveLayout.alpha = 1F
        moveLayout.isEnabled = true
        moveEventUpBtn.alpha = 1F
        moveEventUpBtn.isEnabled = true
        doneEventBtn.alpha = 1F
        doneEventBtn.isEnabled = true
        moveEventDownBtn.alpha = 1F
        moveEventDownBtn.isEnabled = true

        val animation = AnimationUtils.loadAnimation(context, R.anim.fade)

        this.moveEventUpBtn.startAnimation(animation)
        this.doneEventBtn.startAnimation(animation)
        this.moveEventDownBtn.startAnimation(animation)
        this.moveLayout.startAnimation(animation)
    }



    override fun setOnEventUIClickListener(listener: OnEventUIClickListener) {
        this.moveEventUpBtn.setOnClickListener { listener.onMoveEventUpBtnClicked() }
        this.moveEventDownBtn.setOnClickListener { listener.onMoveEventDownBtnClicked() }
        this.deleteEventBtn.setOnClickListener { listener.onDeleteEventBtnClicked() }
        this.doneEventBtn.setOnClickListener { listener.onDoneEventBtnClicked() }
        this.addEventBtn.setOnClickListener { listener.onAddEventBtnClicked(this.eventListAdapter) }
        this.eventListView.setOnItemLongClickListener { parent, _, position, _ -> listener.onEventSelected(parent as AdapterView<EventListAdapter>, position) }
    }

    private fun connectMoveLayoutToDayLayout() {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this.fragmentLayout)
        newConstraintSet.clear(R.id.dayLayout, ConstraintSet.BOTTOM)
        newConstraintSet.connect(R.id.dayLayout, ConstraintSet.BOTTOM, R.id.moveLayout, ConstraintSet.TOP, 10)
        TransitionManager.beginDelayedTransition(this.dayLayout)
        newConstraintSet.applyTo(this.fragmentLayout)
    }

    private fun disconnectMoveLayoutToDayLayout() {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this.fragmentLayout)
        newConstraintSet.clear(R.id.dayLayout, ConstraintSet.BOTTOM)
        newConstraintSet.connect(R.id.dayLayout, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 10)
        TransitionManager.beginDelayedTransition(this.dayLayout)
        newConstraintSet.applyTo(this.fragmentLayout)
    }
}