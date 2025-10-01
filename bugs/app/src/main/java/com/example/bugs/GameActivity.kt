package com.example.bugs


import GameView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bugs.Cockroach
import com.example.bugs.Cockroach.CockroachClickListener
import java.util.stream.IntStream.range

class GameActivity: AppCompatActivity(), CockroachClickListener {
    private var gameView: GameView? = null
    private var cockroach: Cockroach? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)


        // Создаем таракана после того как View будет измерено
        for ( i in range(0,10)){
        gameView!!.post(Runnable {
            cockroach = Cockroach(
                this@GameActivity, gameView,
                gameView!!.getWidth(), gameView!!.getHeight()
            )
            cockroach!!.setCockroachClickListener(this@GameActivity)
            gameView!!.addCockroach(cockroach)
            gameView!!.startGame()
        })}
    }


    override fun onPause() {
        super.onPause()
        if (gameView != null) {
            gameView!!.stopGame()
        }
    }

    override fun onResume() {
        super.onResume()
        if (gameView != null) {
            gameView!!.startGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (gameView != null) {
            gameView!!.cleanup()
        }
    }

    override fun onCockroachClicked(cockroach: Cockroach?) {
        print("ААААААА")
    }
}