package com.milo.store.customview.custom_view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class DraggableViewGroup(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs), View.OnTouchListener {

    private var lastTouchX: Float = 0.toFloat()
    private var lastTouchY: Float = 0.toFloat()

    init {
        setOnTouchListener(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Đặt vị trí cho các child views ở đây
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(0, 0, child.measuredWidth, child.measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Đo kích thước của view group ở đây
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Vẽ các child views nếu cần
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = x
                lastTouchY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastTouchX
                val deltaY = y - lastTouchY

                translationX += deltaX
                translationY += deltaY

                lastTouchX = x
                lastTouchY = y
            }
            else -> return false
        }
        return true
    }
}