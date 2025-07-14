package com.zss.base.recyclerView

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator

interface ItemRemoveListener {
    fun onItemRemove(view: View, position: Int)
}

interface ItemClickListener {
    fun onItemClick(view: View, position: Int)
}

interface ItemLongClickListener {
    fun onItemLongClick(view: View, position: Int): Boolean
}

fun RecyclerView.applyLinearConfig(
    layoutManager: LayoutManager = LinearLayoutManager(context),
    hasFixedSize: Boolean = true,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    applyCustomConfig(layoutManager, hasFixedSize, supportsChangeAnimations, decoration, adapter)
}

/** 应用水平方向的列表配置 */
fun RecyclerView.applyHorizontalLinearConfig(
    hasFixedSize: Boolean = true,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    applyCustomConfig(
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false),
        hasFixedSize, supportsChangeAnimations, decoration, adapter
    )
}

/** 应用网格列表配置 */
fun RecyclerView.applyGridConfig(
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL,
    hasFixedSize: Boolean = true,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    val lm = GridLayoutManager(context, spanCount).also { it.orientation = orientation }
    applyCustomConfig(lm, hasFixedSize, supportsChangeAnimations, decoration, adapter)
}

fun RecyclerView.applyCustomConfig(
    layoutManager: LayoutManager,
    hasFixedSize: Boolean = true,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(hasFixedSize)
    this.layoutManager = layoutManager
    if (!supportsChangeAnimations) clearAnim()
    if (null != decoration) {
        clearItemDecoration()
        addItemDecoration(decoration)
    }
    if (null != adapter) this.adapter = adapter
}

fun RecyclerView?.clearAnim() {
    this?.itemAnimator?.let {
        if (it is SimpleItemAnimator) it.supportsChangeAnimations = false
        it.changeDuration = 0L
    }
}

fun RecyclerView?.clearItemDecoration() {
    this?.let {
        while (it.itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
    }
}

//fun getGridItemDecoration(rvMarginLeftDp: Int, itemMarginTopDp: Int, itemWidthDp: Int, itemCount: Int, rvMarginRightDp: Int = rvMarginLeftDp): GridSpaceDecoration {
//    val positionSpaceH = (com.pxb7.base.util.ScreenUtil.getRealScreenWidth() - itemCount * itemWidthDp.dp2px()-
//            (com.pxb7.base.util.DisplayUtils.dpToPx(rvMarginLeftDp + rvMarginRightDp))) / (itemCount - 1)
//    return GridSpaceDecoration(itemCount, positionSpaceH,
//        com.pxb7.base.util.DisplayUtils.dpToPx(itemMarginTopDp), 0)
//}

//fun getVerticalItemDecoration(offsetDp: Int = 0, firstOffsetDp: Int = 0): ItemDecoration {
//    return object : ItemDecoration() {
//        override fun getItemOffsets(
//            outRect: Rect,
//            view: View,
//            parent: RecyclerView,
//            state: RecyclerView.State
//        ) {
//            val position = parent.getChildLayoutPosition(view)
//            if (position == 0) {
//                outRect.top = DisplayUtils.dpToPx(firstOffsetDp)
//            } else {
//                outRect.top = DisplayUtils.dpToPx(offsetDp)
//            }
//        }
//    }
//}

//fun getHorItemDecoration(offsetDp: Int = 0, firstOffsetDp: Int = 0): ItemDecoration {
//    return object : ItemDecoration() {
//        override fun getItemOffsets(
//            outRect: Rect,
//            view: View,
//            parent: RecyclerView,
//            state: RecyclerView.State
//        ) {
//            val position = parent.getChildLayoutPosition(view)
//            if (position == 0) {
//                outRect.left = DisplayUtils.dpToPx(firstOffsetDp)
//            } else {
//                outRect.left = DisplayUtils.dpToPx(offsetDp)
//            }
//        }
//    }
//}

//fun getLastItemMarginBottomDecoration(marginBottomDp: Int): ItemDecoration {
//    return object : ItemDecoration() {
//        override fun getItemOffsets(
//            outRect: Rect,
//            view: View,
//            parent: RecyclerView,
//            state: RecyclerView.State
//        ) {
//            val position = parent.getChildAdapterPosition(view)
//            val itemCount = state.itemCount
//
//            if (position == itemCount - 1) {
//                outRect.bottom = com.pxb7.base.util.DisplayUtils.dpToPx(marginBottomDp)
//            }
//        }
//    }
//}

//fun getGridDecoration(offsetDp: Int): ItemDecoration {
//    return object : ItemDecoration() {
//        override fun getItemOffsets(
//            outRect: Rect,
//            view: View,
//            parent: RecyclerView,
//            state: RecyclerView.State
//        ) {
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1
//            val position = parent.getChildAdapterPosition(view)
//            val column = position % spanCount
//
//            // 为每个item设置底部间距，但最后一行的item不需要底部间距
//            if (position < parent.adapter!!.itemCount - spanCount) {
//                outRect.bottom = DisplayUtils.dpToPx(offsetDp)
//            }
//        }
//    }
//}