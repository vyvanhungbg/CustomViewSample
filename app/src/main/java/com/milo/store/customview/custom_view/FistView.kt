package com.milo.store.customview.custom_view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.milo.store.customview.R


/**
- Created by :Jung Kary
- Created at :03,March,2024
 **/

//https://proandroiddev.com/android-custom-view-level-3-81e767c8cc75
class FistView : View {

    private var paint:Paint = Paint()

    var centerOfX =  340F // center of circle on X axis
    var centerOfY =  340F // center of circle on Yaxis
    var radiusOfCircleView =  140F // radius of circle
    var isCenter = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        var attributeArray: TypedArray? = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.circle_view, 0, 0
        )
        paint.color = attributeArray?.getColor(
            R.styleable.circle_view_circle_background,
            ContextCompat.getColor(context, android.R.color.background_dark)
        ) ?: android.R.color.black
        isCenter = attributeArray?.getBoolean(R.styleable.circle_view_onCenter, false) ?: false;
        radiusOfCircleView =
            attributeArray?.getDimension(R.styleable.circle_view_circle_radius, 140F) ?: 140F

        attributeArray?.recycle()
    }


    init {
//        paint.color = Color.GREEN
//
//
        paint.strokeWidth = 40f
        paint.style = Paint.Style.STROKE // draw circle with line not filled

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
       // setMeasuredDimension(300,300)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        isCenter.let {
            centerOfX = (width / 2).toFloat()
            centerOfY = (height / 2).toFloat()
        }
        canvas.drawCircle(centerOfX, centerOfY,width/2.0F -paint.strokeWidth/2, paint)
    }
}