package com.lorna.domain.data

import android.content.Context
import androidx.room.*
import com.lorna.domain.model.MessageEntity
import com.lorna.domain.model.ModelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LornaDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages")
    suspend fun clearMessages()

    @Query("SELECT * FROM models")
    fun getAllModels(): Flow<List<ModelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: ModelEntity)
}

@Database(entities = [MessageEntity::class, ModelEntity::class], version = 1, exportSchema = false)
abstract class LornaDatabase : RoomDatabase() {
    abstract fun lornaDao(): LornaDao

    companion object {
        @Volatile
        private var INSTANCE: LornaDatabase? = null

        fun getDatabase(context: Context): LornaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LornaDatabase::class.java,
                    "lorna_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
