package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CarbonActivityEntity
import com.example.data.model.PledgedTipEntity
import com.example.data.model.UserProfileEntity

@Database(
  entities = [CarbonActivityEntity::class, PledgedTipEntity::class, UserProfileEntity::class],
  version = 2,
  exportSchema = false
)
abstract class CarbonDatabase : RoomDatabase() {
  abstract fun carbonDao(): CarbonDao

  companion object {
    @Volatile
    private var INSTANCE: CarbonDatabase? = null

    fun getDatabase(context: Context): CarbonDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          CarbonDatabase::class.java,
          "carbon_tracker_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
