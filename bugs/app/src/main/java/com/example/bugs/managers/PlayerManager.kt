package com.example.bugs.managers

import com.example.bugs.models.Player
import android.util.Log

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
}