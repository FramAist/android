package com.zss.base.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zss.base.databinding.FootNoMoreDataBinding
import com.zss.base.databinding.ItemCommonEmptyListBinding
import com.zss.base.mvvm.EmptyHolder
import com.zss.base.mvvm.ViewBindingHolder
import com.zss.base.util.setOnSingleClickedListener

abstract class ViewBindingAdapter<D, VB : ViewBinding> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    AdapterData<D>, IPartialRefreshAdapter, IOperateAbleAdapter<D> {

    companion object {
        /** 空视图 **/
        const val VIEW_TYPE_EMPTY = -1

        /** 内容识图 **/
        const val VIEW_TYPE_CONTENT = 2

        const val VIEW_TYPE_FOOTER = 1
    }

    /** 有效点击时间 **/
    open var clickInterVal = 1000L

    /** 是否使用空识图 **/
    open var useEmptyView = false

    /** 点击事件 **/
    open var itemClickListener: ItemClickListener? = null

    /** 长按事件 **/
    open var itemLongClickListener: ItemLongClickListener? = null
    private val list = mutableListOf<D>()
    protected var binding: VB? = null
    private var mFooterCount = 0

    init {

    }

    fun label(): String = this::class.java.simpleName

    fun isValid(position: Int): Boolean = position >= 0 && position < size()

    open fun createEmptyHolder(parent: ViewGroup): EmptyHolder {
        val emptyBinding = ItemCommonEmptyListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EmptyHolder(emptyBinding)
    }

    open fun createFooterHolder(parent: ViewGroup): ViewBindingHolder<ViewBinding> {
        val footNoMoreDataBinding = FootNoMoreDataBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewBindingHolder(footNoMoreDataBinding)
    }

    open fun removeFooter() {
        mFooterCount = 0
    }

    open fun setFooter() {
        mFooterCount = 1
    }

    abstract fun createHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder<VB>

    abstract fun onBind(binding: VB, item: D, position: Int)

    open fun onBindHolder(holder: ViewBindingHolder<VB>, item: D, position: Int) {

    }

    open fun onPartialRefresh(vb: VB?, item: D, position: Int) {}

    override fun onRefreshItem(position: Int, holder: RecyclerView.ViewHolder) {
        if (holder is ViewBindingHolder<*>) {
            val vb = holder.viewBinding
            try {
                onPartialRefresh(vb as VB?, getItem(position), position)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ViewBindingAdapter", "onRefreshItem:${e.message}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            useEmptyView && VIEW_TYPE_EMPTY == viewType -> createEmptyHolder(parent)
            VIEW_TYPE_FOOTER == viewType -> createFooterHolder(parent)
            else -> createHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position >= size()) {
            return
        }
        if (holder is ViewBindingHolder<*> && position >= 0) {
            (holder as ViewBindingHolder<VB>).viewBinding?.let { vb ->
                val view = vb.root
                //
                itemClickListener?.apply {
                    view.setOnSingleClickedListener(
                        clickInterVal,
                    ) {
                        onItemClick(view, position)
                    }
                }
                view.setOnLongClickListener {
                    itemLongClickListener?.onItemLongClick(it, position) ?: false
                }
                if (position < size()) {
                    onBindHolder(holder, list[position], position)
                    onBind(vb, list[position], position)
                }
            }
        } else if (isEmptyView(position)) {
            onBindEmptyView(holder, position)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    open fun onBindEmptyView(holder: RecyclerView.ViewHolder, position: Int) {}

    private fun isEmptyView(position: Int): Boolean =
        useEmptyView && list.isEmpty() && position == 0

    override fun getItemViewType(position: Int): Int {
        if (isEmptyView(position)) {
            return VIEW_TYPE_EMPTY
        } else if (mFooterCount != 0 && position >= size()) {
            return VIEW_TYPE_FOOTER
        } else {
            return VIEW_TYPE_CONTENT
        }
        // return if (isEmptyView(position)) VIEW_TYPE_EMPTY else VIEW_TYPE_CONTENT
    }

    override fun getItemCount(): Int {
        val size = size()
        return size + mFooterCount
    }

    override fun getItem(position: Int): D = list[position]

    override fun data(): MutableList<D> = list

    override fun isEmpty(): Boolean = size() == 0

    override fun viewSize(): Int = size()

    override fun add(item: D) {
        list.add(item)
        notifyItemInserted(list.size)
    }

    override fun indexOfData(data: D): Int {
        if (list.isEmpty()) return -1
        return list.indexOf(data)
    }

    override fun addAll(itemList: List<D>) {
        if (itemList.isEmpty()) return
        this.list.addAll(itemList)
        val newSize = itemList.size
        notifyItemRangeInserted(size() - newSize, newSize)
    }

    override fun replace(oldItem: D, newItem: D) {
        replace(list.indexOf(oldItem), newItem)
    }

    override fun replace(index: Int, newItem: D) {
        if (index >= 0 && index < size()) {
            list[index] = newItem
            notifyItemChanged(index)
        }
    }

    override fun remove(item: D): Boolean = remove(list.indexOf(item))

    override fun remove(index: Int): Boolean {

        var result = false
        if (index >= 0 && index < list.size) {
            result = list.removeAt(index) != null
            if (result) {
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, size() - index)
            }
        }
        if (mFooterCount != 0) {
            if (size() == 0) {
                removeFooter()
            }
        }
        return result
    }

    override fun replaceAll(newItemList: List<D>?) {
        replaceAll(newItemList, true)
    }

    override fun replaceAll(newItemList: List<D>?, notifyDataSetChanged: Boolean) {
        this.list.clear()
        if (!newItemList.isNullOrEmpty()) this.list.addAll(newItemList)
        if (notifyDataSetChanged) notifyDataSetChanged()
    }

    override fun contains(item: D): Boolean = list.contains(item)

    override fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun size(): Int = list.size

    override fun showFooter(flag: Boolean) {
        if (flag) {
            setFooter()
        } else {
            removeFooter()
        }
    }

    open fun itemNeedPressChange(item: D): Boolean = true
}