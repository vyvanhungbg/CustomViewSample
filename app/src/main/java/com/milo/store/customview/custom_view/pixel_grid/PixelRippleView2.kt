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

class PixelRippleView2 @JvmOverloads constructor(
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
    private lateinit var pixelCanvas: Canvas

    // Tọa độ chạm
    private var touchX =-1f
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Vẽ bitmap gốccanvas.drawBitmap(pixelBitmap, 0f, 0f, null)

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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                rippleRadius = 0f
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                rippleRadius += rippleSpeed
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                rippleRadius = 0f
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
}