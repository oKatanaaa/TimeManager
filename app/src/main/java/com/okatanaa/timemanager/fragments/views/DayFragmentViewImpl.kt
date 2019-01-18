package com.okatanaa.timemanager.fragments.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.util.TypedValue
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

@Suppress("ConvertSecondaryConstructorToPrimary")
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
    private val singleToast: Toast

    @SuppressLint("ShowToast")
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
        this.singleToast = Toast.makeText(this.context, "", Toast.LENGTH_SHORT)
        hideEventInteractionUI()
    }

    override fun getRootView(): View {
        return this.rootView
    }

    override fun showToast(msg: String, length: Int) {
        if(this.singleToast.view.windowVisibility == View.VISIBLE) {
            this.singleToast.cancel()
        }
        this.singleToast.duration = length
        this.singleToast.setText(msg)
        this.singleToast.show()
    }

    override fun setDayName(name: String) {
        this.dayName.text = name
    }

    override fun setDayCurrent() {
        this.dayLayout.setBackgroundResource(R.drawable.week_item_today_look)
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

    /**
     * This function changes moveLayout params because it
     * does not disappear completely when it is hidden and it causes a problem,
     * that add button is covered by other button are on moveLayout.
     * Even though buttons on moveLayout aren't visible they are still clickable
     * and sometimes it causes an exception, because no event is selected, but these
     * buttons require selected event.
     * Because of that the layout height is changed in order to hide it completely
     * so the add button isn't covered by anything.
     */
    override fun hideEventInteractionUI() {
        disconnectMoveLayoutToDayLayout()
        val params = this.moveLayout.layoutParams
        params.height = ConstraintLayout.LayoutParams.BOTTOM
        this.moveLayout.layoutParams = params
        //this.moveLayout.alpha = 0F

        this.moveEventUpBtn.alpha = 0F
        this.moveEventUpBtn.isEnabled = false

        this.doneEventBtn.alpha = 0F
        this.doneEventBtn.isEnabled = false

        this.moveEventDownBtn.alpha = 0F
        this.moveEventDownBtn.isEnabled = false

        this.deleteEventBtn.alpha = 0F
        this.deleteEventBtn.isEnabled = false
    }

    /**
     * This function shows the layout with event interaction buttons.
     * The code changes layout parameters is necessary because of the problem
     * described in the description of hideEventInteractionUI function.
     */
    override fun showEventInteractionUI() {
        connectMoveLayoutToDayLayout()

        val params = this.moveLayout.layoutParams
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        this.moveLayout.layoutParams = params
        this.moveLayout.alpha = 1F
        this.moveLayout.isEnabled = true

        this.moveEventUpBtn.alpha = 1F
        this.moveEventUpBtn.isEnabled = true

        this.doneEventBtn.alpha = 1F
        this.doneEventBtn.isEnabled = true

        this.moveEventDownBtn.alpha = 1F
        this.moveEventDownBtn.isEnabled = true

        this.deleteEventBtn.alpha = 1F
        this.deleteEventBtn.isEnabled = true

        val animation = AnimationUtils.loadAnimation(context, R.anim.fade)
        /*
        this.moveEventUpBtn.startAnimation(animation)
        this.doneEventBtn.startAnimation(animation)
        this.moveEventDownBtn.startAnimation(animation)
        this.deleteEventBtn.startAnimation(animation)
        */
        this.moveLayout.startAnimation(animation)
    }



    @Suppress("UNCHECKED_CAST")
    override fun setOnEventUIClickListener(listener: OnEventUIClickListener) {
        this.moveEventUpBtn.setOnClickListener { listener.onMoveEventUpBtnClicked() }
        this.moveEventDownBtn.setOnClickListener { listener.onMoveEventDownBtnClicked() }
        this.deleteEventBtn.setOnClickListener { listener.onDeleteEventBtnClicked() }
        this.doneEventBtn.setOnClickListener { listener.onDoneEventBtnClicked() }
        this.addEventBtn.setOnClickListener { listener.onAddEventBtnClicked(this.eventListAdapter) }
        this.eventListView.setOnItemLongClickListener { parent, _, position, _ ->
            listener.onEventSelected(parent as AdapterView<EventListAdapter>, position) }
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