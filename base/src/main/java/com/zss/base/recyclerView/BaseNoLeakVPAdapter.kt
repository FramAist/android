package com.zss.base.recyclerView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

typealias HandleFragment = () -> Fragment

open class BaseNoLeakVPAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    /**
     * 持有的创建Fragment方法集合
     */
    private val mFragmentList = mutableListOf<HandleFragment>()

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position].invoke()
    }

    fun add(fragment: HandleFragment): BaseNoLeakVPAdapter {
        mFragmentList.add(fragment)
        return this
    }

    fun addAll(fragmentList: List<HandleFragment>): BaseNoLeakVPAdapter {
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