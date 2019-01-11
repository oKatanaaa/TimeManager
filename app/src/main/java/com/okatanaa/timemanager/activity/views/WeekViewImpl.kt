package com.okatanaa.timemanager.activity.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.activity.adapters.ViewPagerAdapter
import com.okatanaa.timemanager.activity.adapters.WeekListAdapter
import com.okatanaa.timemanager.fragments.presenter.DayFragment
import com.okatanaa.timemanager.activity.presenter.week_ui_interaction.OnWeekUIClickListener
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Week

class WeekViewImpl: MainView {

    private val rootView: View
    private val toolbar: Toolbar
    private val viewPager: ViewPager
    private val viewPagerAdapter: ViewPagerAdapter
    private val weekListView: ListView
    private val weekListAdapter: WeekListAdapter
    private val context: Context
    private val moveWeekUpBtn: ImageButton
    private val moveWeekDownBtn: ImageButton
    private val deleteWeekBtn: ImageButton
    private val addWeekBtn: ImageButton


    constructor(context: Context, container: ViewGroup?) {
        this.context = context
        this.rootView = LayoutInflater.from(context).inflate(R.layout.activity_main, container)

        this.toolbar = this.rootView.findViewById(R.id.toolbar)
        (context as AppCompatActivity).setSupportActionBar(this.toolbar)
        val drawer = this.rootView.findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            context as Activity,
            drawer,
            this.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        this.viewPager = this.rootView.findViewById(R.id.weekViewPager)
        this.viewPagerAdapter = ViewPagerAdapter(
            context.supportFragmentManager
        )
        this.viewPager.adapter = this.viewPagerAdapter

        this.weekListView = this.rootView.findViewById(R.id.weekListView)
        this.weekListAdapter = WeekListAdapter(context)
        this.weekListView.adapter = this.weekListAdapter

        this.moveWeekUpBtn = this.rootView.findViewById(R.id.moveWeekUpBtn)
        this.moveWeekDownBtn = this.rootView.findViewById(R.id.moveWeekDownBtn)
        this.deleteWeekBtn = this.rootView.findViewById(R.id.deleteWeekBtn)
        this.addWeekBtn = this.rootView.findViewById(R.id.addWeekBtn)
        

    }

    override fun getRootView(): View {
        return this.rootView
    }

    override fun getViewState(): Bundle {
        return Bundle()
    }

    override fun showToast(msg: String, length: Int) {
        Toast.makeText(this.context, msg, length).show()
    }

    override fun getViewPager(): ViewPager {
        return this.viewPager
    }

    override fun getWeekListView(): ListView {
        return this.weekListView
    }

    override fun bindWeekList(weekList: ArrayList<String>) {
        this.weekListAdapter.bindWeekList(weekList)
        this.weekListAdapter.notifyDataSetChanged()
    }

    override fun setOnWeekUIClickListener(listener: OnWeekUIClickListener) {
        this.moveWeekUpBtn.setOnClickListener { listener.onMoveWeekUpBtnClicked() }
        this.moveWeekDownBtn.setOnClickListener { listener.onMoveWeekDownBtnClicked() }
        this.deleteWeekBtn.setOnClickListener { listener.onDeleteWeekBtnClicked() }
        this.addWeekBtn.setOnClickListener { listener.onAddWeekBtnClicked() }
        this.weekListView.setOnItemClickListener { parent, view, position, id ->
            listener.onWeekItemSelected(parent as AdapterView<WeekListAdapter>, position) }
    }

    override fun bindDayList(dayList: ArrayList<Day>) {
        this.viewPagerAdapter.bindDayList(dayList)
        this.viewPagerAdapter.notifyDataSetChanged()
    }


}