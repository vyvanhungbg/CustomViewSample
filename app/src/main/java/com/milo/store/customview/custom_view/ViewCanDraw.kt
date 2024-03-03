package com.milo.store.customview.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.milo.store.customview.R


/**
- Created by :Jung Kary
- Created at :03,March,2024
 **/

private const val TAG = "ViewCanDraw"
class ViewCanDraw : View {

    var arrayPoints = mutableListOf<Float>()
    var length = 0
    var paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {

    }

    init {
        paint.color = Color.GREEN
        paint.strokeWidth = 20F
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLines(arrayPoints.toFloatArray(), paint)
        //Log.e(TAG, "onDraw: ${arrayPoints.size}", )
        super.onDraw(canvas)
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        arrayPoints.add(event.x)
        arrayPoints.add(event.y)
        invalidate()

        logeEvent(event)
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {

                Log.e(TAG, "onTouchEvent: ${event.x} - ${event.y} - ${event.eventTime} - ${event.downTime}", )
            }
            MotionEvent.ACTION_UP ->{

            }
            else -> {

            }
        }

       // return super.onTouchEvent(event)
        return true
    }

    private fun logeEvent(event: MotionEvent) {
        val eventName = when(event.action){
            MotionEvent.ACTION_UP -> "ACTION_UP"
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_BUTTON_PRESS -> "ACTION_BUTTON_PRESS"
            MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
            MotionEvent.ACTION_BUTTON_RELEASE -> "ACTION_BUTTON_RELEASE"
            MotionEvent.ACTION_HOVER_ENTER -> "ACTION_HOVER_ENTER"
            MotionEvent.ACTION_HOVER_EXIT -> "ACTION_HOVER_EXIT"
            MotionEvent.ACTION_MASK -> "ACTION_MASK"
            MotionEvent.ACTION_OUTSIDE -> "ACTION_OUTSIDE"
            MotionEvent.ACTION_POINTER_DOWN -> "ACTION_POINTER_DOWN"
            MotionEvent.ACTION_POINTER_INDEX_MASK -> "ACTION_POINTER_INDEX_MASK"
            MotionEvent.ACTION_POINTER_UP -> "ACTION_POINTER_UP"
            MotionEvent.ACTION_SCROLL -> "ACTION_SCROLL"
            else -> "?"
        }
        Log.e(TAG, "logeEvent: event -  $eventName" )
    }
}