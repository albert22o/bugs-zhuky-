import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.bugs.Cockroach

class GameView : View {
    private var cockroaches: MutableList<Cockroach>? = null
    private var gameHandler: Handler? = null
    private lateinit var gameLoop: Runnable

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        cockroaches = ArrayList<Cockroach>()
        gameHandler = Handler()

        gameLoop = object : Runnable {
            override fun run() {
                update()
                invalidate()
                gameHandler?.postDelayed(this, 16) // ~60 FPS
            }
        }
    }

    fun startGame() {
        gameHandler?.post(gameLoop)
    }

    fun stopGame() {
        gameHandler?.removeCallbacks(gameLoop)
    }

    fun addCockroach(cockroach: Cockroach?) {
        cockroaches!!.add(cockroach!!)
    }

    private fun update() {
        for (cockroach in cockroaches!!) {
            cockroach.update()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (cockroach in cockroaches!!) {
            cockroach.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            val touchX = event.getX().toInt()
            val touchY = event.getY().toInt()

            for (cockroach in cockroaches!!) {
                if (cockroach.isTouched(touchX, touchY)) {
                    cockroach.onTouch()
                    return true
                }
            }
        }
        return true
    }

    fun cleanup() {
        stopGame()
        for (cockroach in cockroaches!!) {
            cockroach.cleanup()
        }
        cockroaches!!.clear()
    }
}