package com.example.data.repository

import com.example.data.db.CarbonDao
import com.example.data.model.ActivityCategory
import com.example.data.model.CarbonActivityEntity
import com.example.data.model.PledgedTipEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CarbonRepository(private val dao: CarbonDao) {

  val allActivities: Flow<List<CarbonActivityEntity>> = dao.getAllActivities()
  val pledgedTips: Flow<List<PledgedTipEntity>> = dao.getAllPledgedTips()
  val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()

  fun getActivitiesForDay(epochDay: Long): Flow<List<CarbonActivityEntity>> {
    return dao.getActivitiesForDay(epochDay)
  }

  suspend fun insertActivity(activity: CarbonActivityEntity): Long {
    return dao.insertActivity(activity)
  }

  suspend fun deleteActivity(id: Long) {
    dao.deleteActivityById(id)
  }

  suspend fun updateUserProfile(profile: UserProfileEntity) {
    dao.insertUserProfile(profile)
  }

  suspend fun togglePledgedTip(tipId: String) {
    val currentPledges = dao.getAllPledgedTips().first()
    val existing = currentPledges.find { it.tipId == tipId }
    if (existing != null) {
      dao.deletePledgedTip(tipId)
    } else {
      dao.insertPledgedTip(
        PledgedTipEntity(
          tipId = tipId,
          pledgedAt = System.currentTimeMillis(),
          isActive = true,
          streakDays = 1
        )
      )
    }
  }

  suspend fun seedStarterDataIfEmpty() {
    val existingProfile = dao.getUserProfile().first()
    if (existingProfile == null) {
      dao.insertUserProfile(
        UserProfileEntity(
          name = "Justine",
          age = 26,
          heightCm = 168.0,
          weightKg = 62.0,
          dietType = "Flexitarian",
          foodSourcing = "Conventional & Local",
          foodWasteLevel = "Low Waste",
          dailyCoffeeTeaCups = 2,
          drinkMilkType = "Oat Milk",
          waterHabit = "Reusable Bottle",
          primaryCommute = "Public Transit",
          dailyCommuteKm = 12.0,
          homeClimateControl = "Moderate Eco",
          laundryHabit = "Cold Water Wash",
          shoppingHabit = "Secondhand & Mindful",
          healthCondition = "Healthy",
          mobilityNeeds = false,
          climateMedicalNeed = false,
          medicalEquipment = false,
          dietaryMedicalRestriction = "None",
          specialNeedsNotes = ""
        )
      )
    }

    val existing = dao.getAllActivities().first()
    if (existing.isNotEmpty()) return

    val currentEpochDay = System.currentTimeMillis() / 86400000L
    val now = System.currentTimeMillis()
    val dayMillis = 86400000L

    val starterActivities = listOf(
      // Today activities
      CarbonActivityEntity(
        category = ActivityCategory.TRANSPORT.name,
        presetId = "trans_train",
        title = "Metro Commute (15 km)",
        quantity = 15.0,
        unit = "km",
        co2EmittedKg = 0.525,
        co2SavedKg = 2.355, // Saved vs solo car driving
        dateEpochDay = currentEpochDay,
        timestamp = now - (3 * 3600000L),
        note = "Morning commute by subway"
      ),
      CarbonActivityEntity(
        category = ActivityCategory.FOOD.name,
        presetId = "food_vegetarian",
        title = "Vegetarian Lunch Bowl",
        quantity = 1.0,
        unit = "meal",
        co2EmittedKg = 0.8,
        co2SavedKg = 5.4, // Saved vs beef meal
        dateEpochDay = currentEpochDay,
        timestamp = now - (2 * 3600000L),
        note = "Mediterranean grain & falafel bowl"
      ),
      CarbonActivityEntity(
        category = ActivityCategory.ENERGY.name,
        presetId = "energy_electricity",
        title = "Home Electricity (8 kWh)",
        quantity = 8.0,
        unit = "kWh",
        co2EmittedKg = 3.08,
        co2SavedKg = 0.0,
        dateEpochDay = currentEpochDay,
        timestamp = now - (1 * 3600000L),
        note = "Lighting & laptop workday"
      ),

      // Yesterday activities
      CarbonActivityEntity(
        category = ActivityCategory.TRANSPORT.name,
        presetId = "trans_petrol_car",
        title = "Car Ride to Grocery",
        quantity = 12.0,
        unit = "km",
        co2EmittedKg = 2.304,
        co2SavedKg = 0.0,
        dateEpochDay = currentEpochDay - 1,
        timestamp = now - dayMillis - (4 * 3600000L),
        note = "Weekly pantry run"
      ),
      CarbonActivityEntity(
        category = ActivityCategory.FOOD.name,
        presetId = "food_beef_lamb",
        title = "Steak Dinner",
        quantity = 1.0,
        unit = "meal",
        co2EmittedKg = 6.20,
        co2SavedKg = 0.0,
        dateEpochDay = currentEpochDay - 1,
        timestamp = now - dayMillis - (2 * 3600000L),
        note = "Family dinner"
      ),
      CarbonActivityEntity(
        category = ActivityCategory.SHOPPING.name,
        presetId = "shop_secondhand",
        title = "Thrift Store Jacket",
        quantity = 1.0,
        unit = "items",
        co2EmittedKg = 0.5,
        co2SavedKg = 11.5,
        dateEpochDay = currentEpochDay - 1,
        timestamp = now - dayMillis - (6 * 3600000L),
        note = "Pre-loved denim jacket"
      ),

      // 2 Days ago
      CarbonActivityEntity(
        category = ActivityCategory.TRANSPORT.name,
        presetId = "trans_bike_walk",
        title = "Bicycle Ride to Park",
        quantity = 8.0,
        unit = "km",
        co2EmittedKg = 0.0,
        co2SavedKg = 1.536,
        dateEpochDay = currentEpochDay - 2,
        timestamp = now - (2 * dayMillis),
        note = "Sunny afternoon bike"
      ),
      CarbonActivityEntity(
        category = ActivityCategory.ENERGY.name,
        presetId = "energy_laundry_cold",
        title = "Cold Water Laundry",
        quantity = 2.0,
        unit = "loads",
        co2EmittedKg = 0.40,
        co2SavedKg = 0.80,
        dateEpochDay = currentEpochDay - 2,
        timestamp = now - (2 * dayMillis),
        note = "Eco wash cycle"
      ),

      // 3 Days ago
      CarbonActivityEntity(
        category = ActivityCategory.FOOD.name,
        presetId = "food_vegan",
        title = "Plant-based Curry Dinner",
        quantity = 1.0,
        unit = "meal",
        co2EmittedKg = 0.45,
        co2SavedKg = 5.75,
        dateEpochDay = currentEpochDay - 3,
        timestamp = now - (3 * dayMillis),
        note = "Chickpea coconut curry"
      ),
      CarbonActivityEntity(
        category = ActivityCategory.ENERGY.name,
        presetId = "energy_ac_heating",
        title = "AC Cooling (5 hrs)",
        quantity = 5.0,
        unit = "hours",
        co2EmittedKg = 6.0,
        co2SavedKg = 0.0,
        dateEpochDay = currentEpochDay - 3,
        timestamp = now - (3 * dayMillis),
        note = "Warm afternoon cooling"
      ),

      // 4 Days ago
      CarbonActivityEntity(
        category = ActivityCategory.TRANSPORT.name,
        presetId = "trans_bus",
        title = "City Bus Commute",
        quantity = 14.0,
        unit = "km",
        co2EmittedKg = 1.246,
        co2SavedKg = 1.442,
        dateEpochDay = currentEpochDay - 4,
        timestamp = now - (4 * dayMillis),
        note = "Public bus ride"
      )
    )

    dao.insertActivities(starterActivities)

    // Seed starter pledged habit
    dao.insertPledgedTip(
      PledgedTipEntity(
        tipId = "tip_cold_water_wash",
        pledgedAt = now - (7 * dayMillis),
        isActive = true,
        streakDays = 5
      )
    )
    dao.insertPledgedTip(
      PledgedTipEntity(
        tipId = "tip_bike_short_trips",
        pledgedAt = now - (4 * dayMillis),
        isActive = true,
        streakDays = 3
      )
    )
  }
}
