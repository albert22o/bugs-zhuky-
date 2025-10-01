package com.example.bugs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.View
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class Cockroach(context: Context, parentView: View?, width: Int, height: Int) {
    interface CockroachClickListener {
        fun onCockroachClicked(cockroach: Cockroach?)
    }

    private var cockroachBitmap: Bitmap?
    var x: Int
        private set
    var y: Int
        private set
    private val width: Int
    private val height: Int
    private var velocityX: Int
    private var velocityY: Int
    var isVisible: Boolean
        private set
    private val context: Context?
    private var listener: CockroachClickListener? = null
    private val handler: Handler
    private val parentView: View?

    init {
        this.context = context
        this.parentView = parentView
        this.width = width
        this.height = height
        this.isVisible = true
        this.handler = Handler(Looper.getMainLooper())


        // Загрузка изображения таракана (создайте файл в res/drawable)
        cockroachBitmap = BitmapFactory.decodeResource(context.getResources(),
            R.drawable.cockroach
        )
        cockroachBitmap = Bitmap.createScaledBitmap(cockroachBitmap!!, 100, 80, true)


        // Начальная позиция
        x = (Math.random() * (width - cockroachBitmap!!.getWidth())).toInt()
        y = (Math.random() * (height - cockroachBitmap!!.getHeight())).toInt()


        // Начальная скорость
        velocityX = (Math.random() * 11 - 5).toInt() // от -5 до 5
        velocityY = (Math.random() * 11 - 5).toInt()


        // Если скорость слишком мала, увеличиваем её
        if (abs(velocityX) < 2) velocityX = 2 * (if (Math.random() > 0.5) 1 else -1)
        if (abs(velocityY) < 2) velocityY = 2 * (if (Math.random() > 0.5) 1 else -1)
    }

    fun setCockroachClickListener(listener: CockroachClickListener?) {
        this.listener = listener
    }

    fun update() {
        if (!isVisible) return

        // Обновление позиции
        x += velocityX
        y += velocityY

        // Отражение от границ
        if (x <= 0 || x >= width - cockroachBitmap!!.getWidth()) {
            velocityX = -velocityX
            x = max(0, min(x, width - cockroachBitmap!!.getWidth()))
        }

        if (y <= 0 || y >= height - cockroachBitmap!!.getHeight()) {
            velocityY = -velocityY
            y = max(0, min(y, height - cockroachBitmap!!.getHeight()))
        }
    }

    fun draw(canvas: Canvas) {
        if (isVisible && cockroachBitmap != null) {
            canvas.drawBitmap(cockroachBitmap!!, x.toFloat(), y.toFloat(), null)
        }
    }

    fun isTouched(touchX: Int, touchY: Int): Boolean {
        if (!isVisible) return false

        val bounds = Rect(x, y, x + cockroachBitmap!!.getWidth(), y + cockroachBitmap!!.getHeight())
        return bounds.contains(touchX, touchY)
    }

    fun onTouch() {
        if (isVisible) {
            isVisible = false


            // Вызываем событие с небольшой задержкой
            handler.postDelayed(Runnable {
                if (listener != null) {
                    listener!!.onCockroachClicked(this)
                }
            }, 100)
        }
    }

    fun respawn() {
        isVisible = true
        x = (Math.random() * (width - cockroachBitmap!!.getWidth())).toInt()
        y = (Math.random() * (height - cockroachBitmap!!.getHeight())).toInt()

        velocityX = (Math.random() * 11 - 5).toInt()
        velocityY = (Math.random() * 11 - 5).toInt()
    }

    fun cleanup() {
        if (cockroachBitmap != null) {
            cockroachBitmap!!.recycle()
            cockroachBitmap = null
        }
    }
}