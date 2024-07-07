package com.milo.store.customview.custom_view.pixel_grid

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class PixelRippleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Kích thước pixel
    private var pixelSize = 20

    // Màu sắc ngẫu nhiên
    private val randomColors = mutableListOf<Int>()

    // Bitmap chứa lưới pixel
    private lateinit var pixelBitmap: Bitmap
    private lateinit var pixelCanvas: Canvas// Tọa độ chạm
    private var touchX = -1f
    private var touchY = -1f

    // Bán kính loang
    private var rippleRadius = 0f

    // Tốc độ loang
    private val rippleSpeed = 10f

    init {
        // Khởi tạo màu sắc ngẫu nhiên
        for (i in 0 until 10) {
            randomColors.add(Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Khởi tạo Bitmap và Canvas
        pixelBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        pixelCanvas = Canvas(pixelBitmap)

        // Vẽ lưới pixel ban đầu
        drawPixelGrid()
    }
    private val zoomMatrix = Matrix()
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
////        // Vẽ Bitmap lên Canvas
////        canvas.drawBitmap(pixelBitmap, 0f, 0f, null)
////        canvas.drawBitmap(pixelBitmap, zoomMatrix, null)
////        // Cập nhật hiệu ứng loang
//        if (rippleRadius > 0) {
//            updateRipple()
//            invalidate()
//        }
////
////        // Áp dụng phép biến đổi zoom trong vùng giới hạn
////        if (rippleRadius > 0) {
////            val zoomArea = RectF(
////                touchX - rippleRadius,
////                touchY - rippleRadius,
////                touchX + rippleRadius,
////                touchY + rippleRadius
////            )
////            canvas.save()
////            canvas.clipRect(zoomArea)
////            canvas.drawBitmap(pixelBitmap, zoomMatrix, null)
////            canvas.restore()
////        }
//
//        // Áp dụng phép biến đổi zoom trong vùng giới hạn
//        if (rippleRadius > 0) {
//            val zoomArea = RectF(
//                touchX - rippleRadius,
//                touchY - rippleRadius,
//                touchX + rippleRadius,
//                touchY + rippleRadius
//            )
//            canvas.save()
//            canvas.clipRect(zoomArea)
//            canvas.drawBitmap(pixelBitmap, zoomMatrix, null)
//            canvas.restore()
//        }
//
//        // Vẽ bitmap gốc lên trên để các pixel không bị ảnh hưởng bởi zoom vẫn hiển thị
//        canvas.drawBitmap(pixelBitmap, 0f, 0f, null)
//    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rippleRadius > 0) {
            updateRipple()
            invalidate()
        }
        // Vẽ bitmap gốc
        canvas.drawBitmap(pixelBitmap, 0f, 0f, null)

        // Áp dụng hiệu ứng zoom
        if (rippleRadius > 0) {
            val zoomArea = RectF(
                touchX - rippleRadius,
                touchY - rippleRadius,
                touchX + rippleRadius,
                touchY + rippleRadius
            )

            val numColumns = width / pixelSize
            val numRows = height / pixelSize

            for (row in 0 until numRows) {
                for (col in 0 until numColumns) {
                    val pixelCenterX = col * pixelSize + pixelSize / 2f
                    val pixelCenterY = row * pixelSize + pixelSize / 2f

                    if (zoomArea.contains(pixelCenterX, pixelCenterY)) {
                        val distanceToCenter = sqrt(
                            (pixelCenterX - touchX).pow(2) + (pixelCenterY - touchY).pow(2)
                        )

                        if (distanceToCenter <= rippleRadius) {
                            // Tính toán vector hướng ra từ tâm
                            val directionX = (pixelCenterX - touchX) / distanceToCenter
                            val directionY = (pixelCenterY - touchY) / distanceToCenter

                            // Tính toán độ dịch chuyển
                            val displacement = rippleRadius - distanceToCenter

                            // Cập nhật vị trí pixel
                            val newX = (pixelCenterX + directionX * displacement).toInt()
                            val newY = (pixelCenterY + directionY * displacement).toInt()

                            // Vẽ pixel tại vị trí mới
                            val color = pixelBitmap.getPixel(col * pixelSize + pixelSize / 2, row * pixelSize + pixelSize / 2)
                            val paint = Paint().apply { this.color = color }
                            canvas.drawRect(
                                Rect(
                                    newX - pixelSize / 2,
                                    newY - pixelSize / 2,
                                    newX + pixelSize / 2,
                                    newY + pixelSize / 2
                                ),
                                paint
                            )
                        }
                    }
                }
            }
        }
    }



    private var rippleAnimator: ValueAnimator? = null
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y

                // Khởi tạo và bắt đầu ValueAnimator
                rippleRadius += 0.1F
                updateZoomMatrix()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                // Nếu muốn hiệu ứng loang theo ngón tay di chuyển, cập nhật touchX, touchY ở đây
                touchX = event.x
                touchY = event.y
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                rippleRadius =0F
                invalidate()
            }
        }
        return true
    }

    // Vẽ lưới pixel
    private fun drawPixelGrid() {
        val numColumns = width / pixelSize
        val numRows = height / pixelSize

        for (row in 0 until numRows) {
            for (col in 0 until numColumns) {
                val color = randomColors[Random.nextInt(randomColors.size)]
                val left = col * pixelSize
                val top = row * pixelSize
                val right = left + pixelSize
                val bottom = top + pixelSize
                val rect = Rect(left, top, right, bottom)
                val paint = Paint().apply { this.color = color }
                pixelCanvas.drawRect(rect, paint)
            }
        }
    }

    private fun updateZoomMatrix() {
        val scaleFactor = 1 + rippleRadius / (pixelSize / 2f)
        zoomMatrix.reset()
        zoomMatrix.postScale(scaleFactor, scaleFactor, touchX, touchY)
    }

    // Cập nhật hiệu ứng loang
    private fun updateRipple() {
        rippleRadius += rippleSpeed

        // Tính toán vùng ảnh hưởng của loang
        // Tính toán vùng ảnh hưởng của loang
        val affectedPixels = mutableListOf<Pair<Int, Int>>()
        val rippleArea = RectF(
            touchX - rippleRadius,
            touchY - rippleRadius,
            touchX + rippleRadius,
            touchY + rippleRadius
        )

        val numColumns = width / pixelSize
        val numRows = height / pixelSize

        for (row in 0 until numRows) {
            for (col in 0 until numColumns) {
                val pixelCenterX = col * pixelSize + pixelSize / 2f
                val pixelCenterY = row * pixelSize + pixelSize / 2f
                if (rippleArea.contains(pixelCenterX, pixelCenterY)) {
                    affectedPixels.add(Pair(col, row))
                }
            }
        }

        val centerPixelColor = pixelBitmap.getPixel((touchX / pixelSize).toInt() * pixelSize + pixelSize / 2, (touchY / pixelSize).toInt() * pixelSize + pixelSize / 2)
        val paint = Paint().apply { color = Color.RED }

        // Cập nhật vị trí của các pixel xung quanh
        for ((col, row) in affectedPixels) {
            val pixelCenterX = col * pixelSize + pixelSize / 2f
            val pixelCenterY = row * pixelSize + pixelSize / 2f
            val distanceToCenter = sqrt(
                (pixelCenterX - touchX).pow(2) + (pixelCenterY - touchY).pow(2)
            )

            if (distanceToCenter > 0 && distanceToCenter <= rippleRadius) {
                // Tính toán vector hướng ra từ tâm
                val directionX = (pixelCenterX - touchX) / distanceToCenter
                val directionY = (pixelCenterY - touchY) / distanceToCenter

                // Tính toán độ dịch chuyển
                val displacement = rippleRadius - distanceToCenter

                // Cập nhật vị trí pixel trên bitmap
                val newX = (pixelCenterX + directionX * displacement).toInt()
                val newY = (pixelCenterY + directionY * displacement).toInt()

                // Vẽ pixel tại vị trí mới
               // val color = pixelBitmap.getPixel(pixelCenterX.toInt(), pixelCenterY.toInt())
               // val paint = Paint().apply { this.color = color }
                pixelCanvas.drawRect(
                    Rect(
                        newX - pixelSize / 2,
                        newY - pixelSize / 2,
                        newX + pixelSize / 2,
                        newY + pixelSize / 2
                    ),
                    paint
                )
            }
        }
    }
}