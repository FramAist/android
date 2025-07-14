package com.zss.base.recyclerView

import androidx.recyclerview.widget.RecyclerView

interface IPartialRefreshAdapter {
    fun onRefreshItem(position: Int, holder: RecyclerView.ViewHolder)
}