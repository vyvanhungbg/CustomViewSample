package com.milo.store.customview.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import kotlin.math.ceil
import kotlin.math.roundToInt


/**
- Created by :Jung Kary
- Created at :05,March,2024
 **/

class TimerView : View {

    private val paintBackground = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 10f
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = Color.GREEN
        textSize = pixelOf(32F)
    }

    private val dateFormat = SimpleDateFormat("HH:mm:ss")
    var centerX = 0F
    var centerY = 0F
    var radius = 0F
    var text = ""
    var textOffsetX = 0F
    var textOffsetY = 0F
    private var updateRunnable: Runnable? = null
    private var time = System.currentTimeMillis()
    private val MAX_TIME = "00:00:00xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    init {
        updateRunnable = Runnable { update() }
        start()
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


    fun pixelOf(value: Float) = resources.displayMetrics.scaledDensity * value


    fun start() {
        time = System.currentTimeMillis()
        update()
    }

    fun stop() {
        removeCallbacks(updateRunnable)
    }


    private fun update() {
        time = System.currentTimeMillis()
        invalidate()
        postDelayed(updateRunnable, 1000L)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val metrics: Paint.FontMetrics = textPaint.fontMetrics
        val maxTextWidth = ceil(textPaint.measureText(MAX_TIME).toDouble()).toInt()
        val maxTextHeight = ceil((metrics.bottom - metrics.top).toDouble()).toInt()

        val contentWidth = maxTextWidth + paddingLeft + paddingRight
        val contentHeight = maxTextHeight + paddingTop + paddingBottom

        val contentSize = Math.max(contentWidth, contentHeight)

        val measuredWidth = resolveSize(contentSize, widthMeasureSpec) // Nó sẽ chọn kích thước lớn nhất mà view có thể có trong giới hạn được chỉ định. // nếu không nó sẽ vẽ tràn parent
        val measuredHeight = resolveSize(contentSize, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // The circle will be in the center of the canvas
        // The circle will be in the center of the canvas
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height

        centerX = (canvasWidth * 0.5f).roundToInt().toFloat()
        centerY = (canvasHeight * 0.5f).roundToInt().toFloat()

        radius = (if (canvasWidth < canvasHeight) canvasWidth else canvasHeight) * 0.5f
        text = dateFormat.format(System.currentTimeMillis())
        textOffsetX = textPaint.measureText(text) * -0.5f
        // for some reason the ascent is negative
        // for some reason the ascent is negative
        textOffsetY = textPaint.fontMetrics.ascent * -0.4f
        canvas.drawCircle(centerX, centerY, radius, paintBackground)
        canvas.drawText(text, centerX + textOffsetX, centerY + textOffsetY, textPaint)
    }
}