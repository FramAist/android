package com.zss.base.tablayout

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import com.angcyo.tablayout.DslTabLayout
import kotlin.math.abs

class PxDslTabLayout(context: Context, attributeSet: AttributeSet? = null) :
    DslTabLayout(context, attributeSet) {
    //手势检测
    private val _pxGestureDetector: GestureDetector by lazy {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (isHorizontal()) {
                    val absX = abs(velocityX)
                    if (absX > _minFlingVelocity) {
                        onFlingChange(velocityX)
                    }
                } else {
                    val absY = abs(velocityY)
                    if (absY > _minFlingVelocity) {
                        onFlingChange(velocityY)
                    }
                }

                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                var handle = false
                if (isHorizontal()) {
                    val absX = abs(distanceX)
                    if (absX > _touchSlop) {
                        handle = onScrollChange(distanceX)
                    }
                } else {
                    val absY = abs(distanceY)
                    if (absY > _touchSlop) {
                        handle = onScrollChange(distanceY)
                    }
                }
                return handle
            }
        })
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        if (needScroll) {
            if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
                _overScroller.abortAnimation()
                _scrollAnimator.cancel()
            }
            if (isEnabled) {
                intercept = super.onInterceptTouchEvent(ev) || _pxGestureDetector.onTouchEvent(ev)
            }
        } else {
            if (isEnabled) {
                intercept = super.onInterceptTouchEvent(ev)
            }
        }
        return if (isEnabled) {
            if (itemEnableSelector) {
                intercept
            } else {
                true
            }
        } else {
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled) {
            if (needScroll) {
                _pxGestureDetector.onTouchEvent(event)
                if (event.actionMasked == MotionEvent.ACTION_CANCEL ||
                    event.actionMasked == MotionEvent.ACTION_UP
                ) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    _overScroller.abortAnimation()
                }
                return true
            } else {
                return (isEnabled && super.onTouchEvent(event))
            }
        } else {
            return false
        }
    }


}