package com.okatanaa.timemanager.activity.presenter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.activity.adapters.ViewPagerAdapter
import com.okatanaa.timemanager.activity.adapters.WeekListAdapter
import com.okatanaa.timemanager.activity.presenter.week_ui_interaction.OnWeekUIClickListener
import com.okatanaa.timemanager.activity.views.MainView
import com.okatanaa.timemanager.activity.views.WeekViewImpl
import com.okatanaa.timemanager.interfaces.CurrentEventChangedListener
import com.okatanaa.timemanager.interfaces.GlobalModel
import com.okatanaa.timemanager.activity.presenter.week_ui_interaction.OnWeekUIClickListenerImpl
import com.okatanaa.timemanager.utils.TextClickedListener
import com.okatanaa.timemanager.controller.SettingsActivity
import com.okatanaa.timemanager.services.DataService
import com.okatanaa.timemanager.utils.EXTRA_EDITED_VALUE
import com.okatanaa.timemanager.utils.RC_TEXT_EDITOR_ACTIVITY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CurrentEventChangedListener, OnWeekUIClickListener.OnWeekSelectedListener {

    private lateinit var mainView: MainView
    private lateinit var viewPager: ViewPager
    private lateinit var globalModel: GlobalModel

    /*
    * This field is used when we choose option 'Rename week'
    * in OptionsMenu. Look at function onOptionsItemSelected
     */
    private var selectedWeekPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mainView = WeekViewImpl(this, null)
        setContentView(this.mainView.getRootView())

        this.viewPager = this.mainView.getViewPager()


        this.globalModel = DataService
        this.globalModel.initialize(this)
        //this.globalModel.startTimeSynchronizing(this)
        this.mainView.setOnWeekUIClickListener(OnWeekUIClickListenerImpl(
            this.globalModel, this.mainView, this))
        this.mainView.bindWeekList(this.globalModel.getWeekNameList())
    }

    override fun onBackPressed() {
        println("Back is pressed!")
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        this.globalModel.saveData(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_rename_week -> {
                // Week renaming is in onActivityResult function
                // This is because we start TextEditor activity for result
                val selectedWeek = this.globalModel.getWeek(this.selectedWeekPosition)
                TextClickedListener.onClick(this,
                    WEEK_NAME, selectedWeek.name)
                return true
            }
            R.id.action_copy_week -> {
                this.globalModel.copyWeek(this.selectedWeekPosition)
                (this.mainView.getWeekListView().adapter as WeekListAdapter)
                    .notifyDataSetChanged()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_TEXT_EDITOR_ACTIVITY -> {
                val newWeekName = data?.getStringExtra(EXTRA_EDITED_VALUE)
                val selectedWeek = this.globalModel.getWeek(this.selectedWeekPosition)
                selectedWeek.name = newWeekName!!
                (this.mainView.getWeekListView().adapter as WeekListAdapter)
                    .notifyDataSetChanged()
            }
        }
    }

    override fun currentEventChanged(dayPosition: Int) {
        (this.viewPager.adapter as ViewPagerAdapter)
            .getCurrentFragment().currentEventChanged()
    }

    override fun onWeekSelected(weekPosition: Int) {
        this.selectedWeekPosition = weekPosition
        val week = this.globalModel.getWeek(weekPosition)
        this.mainView.bindDayList(week.getDayList())
        this.viewPager.adapter!!.notifyDataSetChanged()
    }

    companion object {
        const val WEEK_NAME = "Week name"
    }
}
