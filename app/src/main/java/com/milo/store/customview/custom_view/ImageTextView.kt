package com.milo.store.customview.custom_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.milo.store.customview.R

class ImageTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint = TextPaint()
    private var text =
        "Đôi khi bạn muốn kết hợp văn bản và hình ảnh, vẽ chữ xung quanh hình ảnh chẳng hạn. Có phương pháp tuyệt vời này breakText()có thể giúp ích. Hãy lấy một ví dụ:"
    private var image: Bitmap
    private var metrics = Paint.FontMetrics()
    private var measuredWidth = FloatArray(1)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(image, IMAGE_PADDING, IMAGE_PADDING, paint)
        val length = text.length
        var yOffset = paint.fontSpacing
        var usableWidth: Int
        var start = 0
        var count: Int

        while (start < length) {
            val textTop = yOffset + metrics.ascent
            val textBottom = yOffset + metrics.descent
            usableWidth = if (interfereWithImage(textTop, textBottom)) {
                (width - IMAGE_WIDTH - 2 * IMAGE_PADDING).toInt()
            } else {
                width
            }
            val x = if (interfereWithImage(textTop, textBottom)) {
                image.width + IMAGE_PADDING * 2
            } else {
                0.0f
            }

            count = paint.breakText(text, start, length, true, usableWidth.toFloat(), measuredWidth)
            canvas.drawText(text, start, start + count, x, yOffset, paint)
            start += count
            yOffset += paint.fontSpacing
        }
    }

    private fun interfereWithImage(textTop: Float, textBottom: Float) =
        textTop > IMAGE_PADDING && textTop < IMAGE_PADDING + IMAGE_WIDTH ||
                textBottom > IMAGE_PADDING && textBottom < IMAGE_PADDING + IMAGE_WIDTH


    private var IMAGE_WIDTH = dp2px(150)
    private var IMAGE_PADDING = dp2px(20)

    init {
        textPaint.textSize = dp2px(15)
        paint.textSize = dp2px(22)
        paint.getFontMetrics(metrics)
        image = getImage(IMAGE_WIDTH.toInt())
        IMAGE_WIDTH = image.width.toFloat()
    }

    private fun getImage(width: Int): Bitmap {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.img, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.img, options)
    }
    fun dp2px(sp: Int): Float {
        return TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, sp.toFloat(), resources.displayMetrics)
            .toFloat()
    }
}
