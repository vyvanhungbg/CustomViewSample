package com.milo.store.customview.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class FingerPathView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var fingerPaths = mutableListOf<FingerPath>()
    private var currentPath: FingerPath? = null
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    init {
        isDrawingCacheEnabled = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (path in fingerPaths) {
            canvas.drawPath(path.path, paint)
        }
        currentPath?.let {
            canvas.drawPath(it.path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = FingerPath().apply {
                    path.moveTo(x, y)
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.let {
                    it.path.lineTo(x, y)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let {
                    fingerPaths.add(it)
                }
                currentPath = null
                invalidate()
            }
        }
        return true
    }

    fun clearCanvas() {
        fingerPaths.clear()
        invalidate()
    }

    inner class FingerPath {
        val path = Path()
    }
}