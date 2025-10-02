package com.example.bugs.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Класс данных для хранения информации об игроке.
 * @param id Уникальный идентификатор игрока.
 * @param name ФИО игрока.
 * @param gender Пол.
 * @param course Курс обучения.
 * @param birthDateMillis Дата рождения в миллисекундах.
 * @param zodiac Знак зодиака.
 */

@Parcelize
data class Player(
    val id: String = UUID.randomUUID().toString(), // Генерируем уникальный ID для каждого игрока
    val name: String,
    val gender: String,
    val course: String,
    val birthDateMillis: Long,
    val zodiac: String
): Parcelable