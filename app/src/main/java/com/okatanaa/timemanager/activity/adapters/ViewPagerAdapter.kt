package com.okatanaa.timemanager.activity.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import com.okatanaa.timemanager.fragments.presenter.DayFragment
import com.okatanaa.timemanager.fragments.presenter.DayFragmentImpl
import com.okatanaa.timemanager.model.Day

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var currentFragment: DayFragment? = null
    private var dayList: ArrayList<Day>? = null

    fun getCurrentFragment(): DayFragment? {
        return this.currentFragment
    }

    fun bindDayList(dayList: ArrayList<Day>) {
        this.dayList = dayList
    }

    override fun getItem(position: Int): Fragment {
        if(this.dayList == null)
            return Fragment()
        else
            return DayFragmentImpl.newInstance(
                position + 1,
                this.dayList!![position]
            )
    }

    override fun getCount(): Int {
        return 7
    }

    /**
     * This function is overrided in order to have possibility to get focused(current) fragment
     */
    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if(this.currentFragment != `object`)
            this.currentFragment = `object` as DayFragment
        super.setPrimaryItem(container, position, `object`)
    }

    /*
      * This method is overrided in order to have all fragments to be updated after changing current week.
    */
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}