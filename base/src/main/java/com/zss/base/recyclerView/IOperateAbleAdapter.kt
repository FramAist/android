package com.zss.base.recyclerView


interface IOperateAbleAdapter<T> {

    /**
     * 替换所有数据
     */
    fun replaceAll(newItemList: List<T>?)

    fun addAll(itemList: List<T>)

    fun showFooter(flag: Boolean)

    /**
     * 真实数据条数(不包含:空视图,头部识图)
     */
    fun size(): Int

    fun remove(item: T): Boolean

    fun remove(index: Int): Boolean

    fun indexOfData(data: T): Int


}