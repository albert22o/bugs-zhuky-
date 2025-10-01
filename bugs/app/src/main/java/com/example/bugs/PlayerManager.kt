package com.example.bugs

import android.content.Context
import androidx.room.*
import androidx.room.Entity
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// Entity для Room
@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val gender: String,
    val course: String,
    val difficulty: Int,
    val birthDate: String,
    val zodiac: String,
    var bestScore: Int
)

// DAO (Data Access Object)
@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAllPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE id = :id")
    suspend fun getPlayerById(id: Long): PlayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Update
    suspend fun updatePlayer(player: PlayerEntity)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()
}

// Database
@Database(entities = [PlayerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "players_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Основной класс для управления игроками
class PlayerManager(private val context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val playerDao = database.playerDao()

    // Получить всех игроков как Flow для наблюдения за изменениями
    fun getAllPlayers(): Flow<List<PlayerEntity>> {
        return playerDao.getAllPlayers()
    }

    // Добавить нового игрока
    suspend fun addPlayer(player: Player): Long {
        val entity = PlayerEntity(
            name = player.name,
            gender = player.gender,
            course = player.course,
            difficulty = player.difficulty,
            birthDate = player.birthDate,
            zodiac = player.zodiac,
            bestScore = player.bestScore
        )
        return playerDao.insertPlayer(entity)
    }

    // Обновить игрока
    suspend fun updatePlayer(player: PlayerEntity) {
        playerDao.updatePlayer(player)
    }

    // Удалить игрока
    suspend fun removePlayer(player: PlayerEntity) {
        playerDao.deletePlayer(player)
    }

    // Получить игрока по ID
    suspend fun getPlayer(id: Long): PlayerEntity? {
        return playerDao.getPlayerById(id)
    }

    // Обновить лучший счет игрока
    suspend fun updateBestScore(playerId: Long, newScore: Int) {
        val player = playerDao.getPlayerById(playerId)
        player?.let {
            it.bestScore = newScore
            playerDao.updatePlayer(it)
        }
    }

    // Конвертировать Player в PlayerEntity
    fun Player.toEntity(id: Long = 0): PlayerEntity {
        return PlayerEntity(
            id = id,
            name = this.name,
            gender = this.gender,
            course = this.course,
            difficulty = this.difficulty,
            birthDate = this.birthDate,
            zodiac = this.zodiac,
            bestScore = this.bestScore
        )
    }

    // Конвертировать PlayerEntity в Player
    fun PlayerEntity.toPlayer(): Player {
        return Player(
            name = this.name,
            gender = this.gender,
            course = this.course,
            difficulty = this.difficulty,
            birthDate = this.birthDate,
            zodiac = this.zodiac,
            bestScore = this.bestScore
        )
    }
}

fun Player.toEntity(id: Long = 0): PlayerEntity {
    return PlayerEntity(
        id = id,
        name = this.name,
        gender = this.gender,
        course = this.course,
        difficulty = this.difficulty,
        birthDate = this.birthDate,
        zodiac = this.zodiac,
        bestScore = this.bestScore
    )
}

fun PlayerEntity.toPlayer(): Player {
    return Player(
        name = this.name,
        gender = this.gender,
        course = this.course,
        difficulty = this.difficulty,
        birthDate = this.birthDate,
        zodiac = this.zodiac,
        bestScore = this.bestScore
    )
}