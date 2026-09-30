package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CarbonActivityEntity
import com.example.data.model.PledgedTipEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarbonDao {
  @Query("SELECT * FROM carbon_activities ORDER BY timestamp DESC")
  fun getAllActivities(): Flow<List<CarbonActivityEntity>>

  @Query("SELECT * FROM carbon_activities WHERE dateEpochDay = :epochDay ORDER BY timestamp DESC")
  fun getActivitiesForDay(epochDay: Long): Flow<List<CarbonActivityEntity>>

  @Query("SELECT * FROM carbon_activities WHERE dateEpochDay >= :startEpochDay AND dateEpochDay <= :endEpochDay ORDER BY timestamp DESC")
  fun getActivitiesBetween(startEpochDay: Long, endEpochDay: Long): Flow<List<CarbonActivityEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertActivity(activity: CarbonActivityEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertActivities(activities: List<CarbonActivityEntity>)

  @Query("DELETE FROM carbon_activities WHERE id = :id")
  suspend fun deleteActivityById(id: Long)

  @Query("DELETE FROM carbon_activities")
  suspend fun clearAllActivities()

  // Pledged Tips / Habits
  @Query("SELECT * FROM pledged_tips ORDER BY pledgedAt DESC")
  fun getAllPledgedTips(): Flow<List<PledgedTipEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPledgedTip(pledgedTip: PledgedTipEntity)

  @Query("DELETE FROM pledged_tips WHERE tipId = :tipId")
  suspend fun deletePledgedTip(tipId: String)

  // User Profile & Personalization
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfile(): Flow<UserProfileEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserProfile(profile: UserProfileEntity)
}
