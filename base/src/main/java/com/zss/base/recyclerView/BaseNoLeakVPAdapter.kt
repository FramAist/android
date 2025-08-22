package com.zss.base.recyclerView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import javax.inject.Inject


class BaseNoLeakVPAdapter @Inject constructor(activity: FragmentActivity) :
    FragmentStateAdapter(activity.supportFragmentManager, activity.lifecycle) {

    /**
     * 持有的创建Fragment方法集合
     */
    private val mFragmentList = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun add(fragment: Fragment): BaseNoLeakVPAdapter {
        mFragmentList.add(fragment)
        return this
    }

    fun addAll(fragmentList: List<Fragment>): BaseNoLeakVPAdapter {
        mFragmentList.addAll(fragmentList)
        return this
    }

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 慎用，并不是真正的删除fragment，是删除fragment的创建方法
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    fun removeLast() {
        mFragmentList.removeAt(mFragmentList.lastIndex)
    }

    fun clear() {
        mFragmentList.clear()
    }

}