package com.example.bugs

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bugs.models.Player

class GameActivity : AppCompatActivity() {

    companion object {
        // Ключи для передачи данных через Intent. Хорошая практика - выносить их в константы.
        const val EXTRA_PLAYER = "EXTRA_PLAYER"
        const val EXTRA_GAME_SPEED = "EXTRA_GAME_SPEED"
        const val EXTRA_MAX_COCKROACHES = "EXTRA_MAX_COCKROACHES"
        const val EXTRA_BONUS_INTERVAL = "EXTRA_BONUS_INTERVAL"
        const val EXTRA_ROUND_DURATION = "EXTRA_ROUND_DURATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Получаем данные из Intent
        val player = getPlayerFromIntent()
        val gameSpeed = intent.getIntExtra(EXTRA_GAME_SPEED, 5) // 5 - значение по умолчанию
        val maxCockroaches = intent.getIntExtra(EXTRA_MAX_COCKROACHES, 10)
        val bonusInterval = intent.getIntExtra(EXTRA_BONUS_INTERVAL, 15)
        val roundDuration = intent.getIntExtra(EXTRA_ROUND_DURATION, 120)

        if (player == null) {
            // Если данные игрока не пришли, показываем ошибку и закрываем активность
            Toast.makeText(this, "Ошибка: данные игрока не найдены", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Здесь начинается ваша игровая логика
        // Для примера просто выведем данные на экран
        val infoTextView: TextView = findViewById(R.id.textViewGameInfo)
        infoTextView.text = """
            Игрок: ${player.name}
            
            Настройки игры:
            Скорость: $gameSpeed
            Макс. тараканов: $maxCockroaches
            Интервал бонусов: $bonusInterval сек.
            Длительность раунда: $roundDuration сек.
        """.trimIndent()


    }

    private fun getPlayerFromIntent(): Player? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PLAYER, Player::class.java)
        } else {
            // Устаревшая версия для API < 33
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PLAYER)
        }
    }
}