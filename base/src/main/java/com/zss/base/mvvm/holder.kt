package com.zss.base.mvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

open class ViewBindingHolder<VB : ViewBinding>(binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    var viewBinding: VB? = binding
}

class CountingDownViewBindingHolder<VB : ViewBinding>(binding: VB) :
    ViewBindingHolder<VB>(binding) {
    var countDownJob: Job? = null
}

class EmptyHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

inline fun <reified T : ViewBinding> createViewBindingHolder(
    parent: ViewGroup,
    block: (LayoutInflater, ViewGroup, Boolean) -> T,
): ViewBindingHolder<T> {
    val binding = block.invoke(LayoutInflater.from(parent.context), parent, false)
    binding.root.setOnClickListener {}
    return ViewBindingHolder(binding)
}

inline fun <reified T : ViewBinding> createCountingDownHolder(
    parent: ViewGroup,
    block: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding
): ViewBindingHolder<T> {
    val binding = block.invoke(LayoutInflater.from(parent.context), parent, false) as T
    binding.root.setOnClickListener {}
    return CountingDownViewBindingHolder(binding)
}