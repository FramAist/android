package com.zss.base.recyclerView

interface AdapterData<T> {

    fun getItem(position: Int): T

    fun data(): MutableList<T>

    fun isEmpty(): Boolean

    /**
     * 识图条数(包含:空视图,头部识图)
     */
    fun viewSize(): Int

    fun add(item: T)

    fun replace(oldItem: T, newItem: T)

    fun replace(index: Int, newItem: T)

    /**
     * 替换所有数据
     */
    fun replaceAll(newItemList: List<T>?, notifyDataSetChanged: Boolean)

    /**
     * 是否存在某个对象
     */
    fun contains(item: T): Boolean

    /**
     * 清空数据
     */
    fun clear()



}