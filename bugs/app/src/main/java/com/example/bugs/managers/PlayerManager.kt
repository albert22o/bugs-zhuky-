package com.example.bugs.managers

import android.util.Log
import com.example.bugs.models.Player

/**
 * Синглтон для управления списком зарегистрированных игроков.
 * Данные хранятся в оперативной памяти и будут доступны в течение всего жизненного цикла приложения.
 */
object PlayerManager {

    private val registeredPlayers = mutableListOf<Player>()

    /**
     * Добавляет нового игрока в список.
     * @param player Объект игрока для добавления.
     */
    fun addPlayer(player: Player) {
        registeredPlayers.add(player)
        Log.i("PlayerManager", "Игрок добавлен: ${player.name}. Всего игроков: ${registeredPlayers.size}")
    }

    /**
     * Возвращает неизменяемый список всех зарегистрированных игроков.
     * @return List<Player> список игроков.
     */
    fun getPlayers(): List<Player> {
        return registeredPlayers.toList() // Возвращаем копию, чтобы нельзя было изменить список извне
    }

    /**
     * Очищает список всех игроков.
     */
    fun clearPlayers() {
        registeredPlayers.clear()
        Log.i("PlayerManager", "Список игроков очищен.")
    }

    /**
     * Обновляет максимальный счет игрока, если новый счет больше предыдущего рекорда.
     * @param playerName Имя игрока, чей счет нужно обновить.
     * @param newScore Новый счет, полученный в игре.
     */
    fun updatePlayerHighScore(playerName: String, newScore: Int) {
        val player = registeredPlayers.find { it.name == playerName }
        if (player != null) {
            if (newScore > player.highScore) {
                player.highScore = newScore
                Log.i("PlayerManager", "Новый рекорд для игрока ${player.name}: $newScore")
            } else {
                Log.i("PlayerManager", "Счет игрока ${player.name} ($newScore) не превысил рекорд (${player.highScore})")
            }
        } else {
            Log.w("PlayerManager", "Попытка обновить счет для несуществующего игрока: $playerName")
        }
    }
}