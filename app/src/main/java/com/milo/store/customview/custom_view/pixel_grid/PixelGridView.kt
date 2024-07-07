package com.milo.store.customview.custom_view.pixel_grid

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class PixelGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val gridSize = 100
    private val pixels = Array(gridSize) { Array(gridSize) { Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)) } }
    private var scaleFactor = 1f
    private var touchX = 0f
    private var touchY = 0f
    private var isZooming = false

    init {
        paint.style = Paint.Style.FILL
    }

//    override fun onDraw(canvas: Canvas) {
//
//        super.onDraw(canvas)
//        val cellSize = width / gridSize.toFloat()
//        for (i in 0 until gridSize) {
//            for (j in 0 until gridSize) {
//                paint.color = pixels[i][j]
//                canvas.drawRect(i * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, paint)
//            }
//        }
//
//        if (isZooming) {
//            val zoomWidth = width / scaleFactor
//            val zoomHeight = height / scaleFactor
//            val left = touchX - zoomWidth / 2
//            val top = touchY - zoomHeight / 2
//            val right = touchX + zoomWidth / 2
//            val bottom = touchY + zoomHeight / 2
//
//            canvas.save()
//            //canvas.clipRect(left, top, right, bottom)
//            canvas.scale(scaleFactor, scaleFactor, touchX, touchY)
//            for (i in 0 until gridSize) {
//                for (j in 0 until gridSize) {
//                    paint.color = pixels[i][j]
//                    canvas.drawRect(i * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, paint)
//                }
//            }
//            canvas.restore()
//            invalidate()  // redraw to continue zooming
//        }
//    }


//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val cellSize = width / gridSize.toFloat()
//
//        // Vẽ lưới màu ban đầu
//        for (i in 0 until gridSize) {
//            for (j in 0 until gridSize) {
//                paint.color = pixels[i][j]
//                canvas.drawRect(i * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, paint)
//            }
//        }
//
//        if (isZooming) {
//            val zoomWidth = width / scaleFactor
//            val zoomHeight = height / scaleFactor
//            val left = touchX - zoomWidth / 2
//            val top = touchY - zoomHeight / 2
//            val right = touchX + zoomWidth / 2
//            val bottom = touchY + zoomHeight / 2
//
//            canvas.save()
//            canvas.scale(scaleFactor, scaleFactor, touchX, touchY)
//
//            // Vẽ lưới màu với hiệu ứng gradient khi zoom
//            for (i in 0 until gridSize) {
//                for (j in 0 until gridSize) {
//                    // Tính toán màu sắc gradient cho từng pixel
//                    val gradientColor = getZoomedColor(i, j, cellSize)
//                    paint.color = gradientColor
//                    canvas.drawRect(i * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, paint)
//                }
//            }
//
//            canvas.restore()
//            invalidate()  // Vẽ lại để tiếp tục zoom
//        }
//    }










    private fun getZoomedColor(x: Int, y: Int, cellSize: Float): Int {
        // Tính toán vị trí tâm của khu vực zoom
        val zoomCenterX = touchX / cellSize
        val zoomCenterY = touchY / cellSize

        // Tính toán khoảng cách từ pixel hiện tại đến tâm zoom
        val distance = Math.sqrt(((x - zoomCenterX) * (x - zoomCenterX) + (y - zoomCenterY) * (y - zoomCenterY)).toDouble()).toFloat()

        // Tính toán tỉ lệ gradient dựa trên khoảng cách
        val gradientRatio = 1.0f - Math.min(1.0f, distance / (gridSize / 2.0f))

        // Lấy màu sắc từ mảng pixels và áp dụng gradient
        val color = pixels[x][y]
        val adjacentColors = getAdjacentColors(x, y)

        // Trộn màu sắc với màu sắc lân cận dựa trên gradient
        val blendedRed = (Color.red(color) * gradientRatio + calculateAverageRed(adjacentColors) * (1 - gradientRatio)).toInt()
        val blendedGreen = (Color.green(color) * gradientRatio + calculateAverageGreen(adjacentColors) * (1 - gradientRatio)).toInt()
        val blendedBlue = (Color.blue(color) * gradientRatio + calculateAverageBlue(adjacentColors) * (1 - gradientRatio)).toInt()

        return Color.rgb(blendedRed, blendedGreen, blendedBlue)
    }

    private fun getAdjacentColors(x: Int, y: Int): List<Int> {
        val adjacentColors = mutableListOf<Int>()
        if (x > 0 && y > 0) adjacentColors.add(pixels[x - 1][y - 1]) // Top left
        if (y > 0) adjacentColors.add(pixels[x][y - 1]) // Top
        if (x < gridSize - 1 && y > 0) adjacentColors.add(pixels[x + 1][y - 1]) // Top right
        if (x > 0) adjacentColors.add(pixels[x - 1][y]) // Left
        if (x < gridSize - 1) adjacentColors.add(pixels[x + 1][y]) // Right
        if (x > 0 && y < gridSize - 1) adjacentColors.add(pixels[x - 1][y + 1]) // Bottom left
        if (y < gridSize - 1) adjacentColors.add(pixels[x][y + 1]) // Bottom
        if (x < gridSize - 1 && y < gridSize - 1) adjacentColors.add(pixels[x + 1][y + 1]) // Bottom right
        return adjacentColors
    }

    private fun calculateAverageRed(colors: List<Int>): Int {
        var sum = 0
        colors.forEach { sum += Color.red(it) }
        return sum / colors.size
    }

    private fun calculateAverageGreen(colors: List<Int>): Int {
        var sum = 0
        colors.forEach { sum += Color.green(it) }
        return sum / colors.size
    }

    private fun calculateAverageBlue(colors: List<Int>): Int {
        var sum = 0
        colors.forEach { sum += Color.blue(it) }
        return sum / colors.size
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("", "onTouchEvent: cc", )
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                isZooming = true
                Log.e("MMM", "onTouchEvent: ${x}_${y}", )
                post(zoomRunnable)
                return true
            }
//            MotionEvent.ACTION_MOVE->{
//                touchX = event.x
//                touchY = event.y
//            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isZooming = false
                scaleFactor = 1f
                invalidate()
                Log.e("MMM", "onTouchEvent: 2", )
                return true
            }
        }
        return true
    }

    private val zoomRunnable = object : Runnable {
        override fun run() {
            if (isZooming) {
                scaleFactor += 0.5f
                if (scaleFactor > 200f) { // giới hạn phóng to
                    scaleFactor = 5f
                }
                invalidate()
                postDelayed(this, 16)  // 60 FPS
            }
        }
    }
}
