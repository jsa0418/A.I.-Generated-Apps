package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carbon_activities")
data class CarbonActivityEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val category: String, // Transport, Food, Energy, Shopping
  val presetId: String,
  val title: String,
  val quantity: Double,
  val unit: String,
  val co2EmittedKg: Double,
  val co2SavedKg: Double = 0.0,
  val dateEpochDay: Long, // LocalDate.toEpochDay()
  val timestamp: Long = System.currentTimeMillis(),
  val note: String = ""
)

@Entity(tableName = "pledged_tips")
data class PledgedTipEntity(
  @PrimaryKey
  val tipId: String,
  val pledgedAt: Long = System.currentTimeMillis(),
  val isActive: Boolean = true,
  val streakDays: Int = 1
)
