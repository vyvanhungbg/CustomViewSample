package com.milo.store.customview.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.milo.store.customview.R


/**
- Created by :Jung Kary
- Created at :11,March,2024
 **/
private const val TAG = "MyTextView"

class MyTextView : View {


    private var mText: String = ""
    private var mTextSize: Int = 15
    private var mTextColor: Int = Color.BLACK
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds: Rect = Rect()

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView)
        mText = typedArray.getString(R.styleable.MyTextView_myText) ?: ""
        mTextColor =
            typedArray.getColor(R.styleable.MyTextView_myTextColor, mTextColor) ?: Color.BLACK
        mTextSize =
            typedArray.getDimensionPixelSize(R.styleable.MyTextView_myTextSize, sp2px(mTextSize))
                ?: sp2px(12)
        typedArray.recycle()

        mPaint.apply {
            textSize = mTextSize.toFloat()
            color = mTextColor
        }.run {
            getTextBounds(mText, 0, mText.length, bounds)
        }
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView)
        mText = typedArray.getString(R.styleable.MyTextView_myText) ?: ""
        mTextColor =
            typedArray.getColor(R.styleable.MyTextView_myTextColor, mTextColor) ?: Color.BLACK
        mTextSize =
            typedArray.getDimensionPixelSize(R.styleable.MyTextView_myTextSize, sp2px(mTextSize))
                ?: sp2px(12)
        typedArray.recycle()

        mPaint.apply {
            textSize = mTextSize.toFloat()
            color = mTextColor
        }.run {
            getTextBounds(mText, 0, mText.length, bounds)
        }
    }

    init {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            resolveSize(bounds.width() + paddingLeft + paddingRight, widthMeasureSpec),
            resolveSize(bounds.height() + paddingTop + paddingBottom, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(
            mText,
            paddingLeft.toFloat(),
            height - bounds.bottom.toFloat() - paddingBottom,
            mPaint
        )
        Log.e(TAG, "onDraw measureText:  ${mPaint.measureText(mText)}")
        Log.e(TAG, "onDraw r.right: ${bounds.right} - r.left: ${bounds.left} =  ${bounds.width()}")
        Log.e(TAG, "onDraw r.bottom: ${bounds.bottom} - r.top: ${bounds.top} =  ${bounds.height()}")
        Log.e(TAG, "onDraw height:  ${height}")
        Log.e(TAG, "onDraw width:  ${width}")
        Log.e(
            TAG,
            "onDraw padding left: ${paddingLeft} - right: ${paddingRight} - top: ${paddingTop} - bottom: ${paddingBottom} - start: ${paddingStart} - end: ${paddingEnd}",
        )
    }

    private fun sp2px(sp: Int): Int {
        return TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics)
            .toInt()
    }
}