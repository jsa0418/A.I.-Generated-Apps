package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.CarbonDatabase
import com.example.data.model.ActivityCategory
import com.example.data.model.ActivityPreset
import com.example.data.model.CarbonActivityEntity
import com.example.data.model.CarbonJustificationSummary
import com.example.data.model.EcoTip
import com.example.data.model.EcoTipsCatalog
import com.example.data.model.PledgedTipEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.CarbonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DailyCarbonStat(
  val epochDay: Long,
  val dayLabel: String,
  val dayShortDate: String,
  val totalEmittedKg: Double,
  val totalSavedKg: Double,
  val isToday: Boolean,
  val isSelected: Boolean
)

data class CarbonUiState(
  val currentEpochDay: Long = System.currentTimeMillis() / 86400000L,
  val selectedEpochDay: Long = System.currentTimeMillis() / 86400000L,
  val dailyBudgetKg: Double = 10.0,
  val allActivities: List<CarbonActivityEntity> = emptyList(),
  val selectedDayActivities: List<CarbonActivityEntity> = emptyList(),
  val selectedDayEmissionsKg: Double = 0.0,
  val selectedDaySavedKg: Double = 0.0,
  val todayEmissionsKg: Double = 0.0,
  val todaySavedKg: Double = 0.0,
  val categoryBreakdown: Map<ActivityCategory, Double> = emptyMap(),
  val highestEmissionCategory: ActivityCategory? = null,
  val weeklyStats: List<DailyCarbonStat> = emptyList(),
  val pledgedTipIds: Set<String> = emptySet(),
  val topRecommendedTip: EcoTip? = null,
  val pledgedTipsWithDetails: List<Pair<PledgedTipEntity, EcoTip>> = emptyList(),
  val userProfile: UserProfileEntity = UserProfileEntity(),
  val carbonJustification: CarbonJustificationSummary = UserProfileEntity().calculateJustification()
)

class CarbonViewModel(
  application: Application,
  private val repository: CarbonRepository
) : AndroidViewModel(application) {

  private val _selectedEpochDay = MutableStateFlow(System.currentTimeMillis() / 86400000L)
  private val _dailyBudgetKg = MutableStateFlow(10.0)

  val uiState: StateFlow<CarbonUiState> = combine(
    repository.allActivities,
    repository.pledgedTips,
    repository.userProfile,
    _selectedEpochDay,
    _dailyBudgetKg
  ) { activities, pledgedTips, profileFromDb, selectedEpochDay, budget ->
    val currentEpochDay = System.currentTimeMillis() / 86400000L
    val activeProfile = profileFromDb ?: UserProfileEntity()
    val justification = activeProfile.calculateJustification()

    // Filter activities for selected day
    val selectedDayActivities = activities.filter { it.dateEpochDay == selectedEpochDay }
    val selectedEmissions = selectedDayActivities.sumOf { it.co2EmittedKg }
    val selectedSaved = selectedDayActivities.sumOf { it.co2SavedKg }

    // Today's activities
    val todayActivities = activities.filter { it.dateEpochDay == currentEpochDay }
    val todayEmissions = todayActivities.sumOf { it.co2EmittedKg }
    val todaySaved = todayActivities.sumOf { it.co2SavedKg }

    // Category breakdown for selected day (or fallback to recent if empty)
    val breakdownSource = if (selectedDayActivities.isNotEmpty()) selectedDayActivities else activities
    val breakdown = ActivityCategory.values().associateWith { cat ->
      breakdownSource.filter { it.category == cat.name }.sumOf { it.co2EmittedKg }
    }

    val highestCat = breakdown.maxByOrNull { it.value }?.let {
      if (it.value > 0) it.key else null
    }

    // Weekly statistics for past 7 days (including today)
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val weeklyStats = (6 downTo 0).map { daysAgo ->
      val targetEpoch = currentEpochDay - daysAgo
      val targetDate = Date(targetEpoch * 86400000L + (12 * 3600000L)) // Midday
      val dayActivities = activities.filter { it.dateEpochDay == targetEpoch }
      DailyCarbonStat(
        epochDay = targetEpoch,
        dayLabel = if (daysAgo == 0) "Today" else dayFormat.format(targetDate),
        dayShortDate = dateFormat.format(targetDate),
        totalEmittedKg = dayActivities.sumOf { it.co2EmittedKg },
        totalSavedKg = dayActivities.sumOf { it.co2SavedKg },
        isToday = daysAgo == 0,
        isSelected = targetEpoch == selectedEpochDay
      )
    }

    // Pledged tips details
    val pledgedIds = pledgedTips.map { it.tipId }.toSet()
    val pledgedWithDetails = pledgedTips.mapNotNull { entity ->
      EcoTipsCatalog.getTipById(entity.tipId)?.let { tip ->
        entity to tip
      }
    }

    // Dynamic top recommendation based on user preferences and highest emission category
    val recommendedTip = if (highestCat != null) {
      EcoTipsCatalog.getTipsForCategory(highestCat).firstOrNull { it.id !in pledgedIds }
        ?: EcoTipsCatalog.tips.firstOrNull { it.id !in pledgedIds }
    } else {
      EcoTipsCatalog.tips.firstOrNull { it.id !in pledgedIds }
    }

    CarbonUiState(
      currentEpochDay = currentEpochDay,
      selectedEpochDay = selectedEpochDay,
      dailyBudgetKg = budget,
      allActivities = activities,
      selectedDayActivities = selectedDayActivities,
      selectedDayEmissionsKg = selectedEmissions,
      selectedDaySavedKg = selectedSaved,
      todayEmissionsKg = todayEmissions,
      todaySavedKg = todaySaved,
      categoryBreakdown = breakdown,
      highestEmissionCategory = highestCat,
      weeklyStats = weeklyStats,
      pledgedTipIds = pledgedIds,
      topRecommendedTip = recommendedTip,
      pledgedTipsWithDetails = pledgedWithDetails,
      userProfile = activeProfile,
      carbonJustification = justification
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = CarbonUiState()
  )

  init {
    viewModelScope.launch {
      repository.seedStarterDataIfEmpty()
    }
  }

  fun updateUserProfile(profile: UserProfileEntity, syncBudgetWithJustification: Boolean = false) {
    viewModelScope.launch {
      repository.updateUserProfile(profile)
      if (syncBudgetWithJustification) {
        val justification = profile.calculateJustification()
        _dailyBudgetKg.value = justification.totalRecommendedBudgetKg
      }
    }
  }

  fun applyJustifiedBudget() {
    val currentJustification = uiState.value.carbonJustification
    _dailyBudgetKg.value = currentJustification.totalRecommendedBudgetKg
  }

  fun selectDay(epochDay: Long) {
    _selectedEpochDay.value = epochDay
  }

  fun setDailyBudget(budget: Double) {
    if (budget > 0) {
      _dailyBudgetKg.value = budget
    }
  }

  fun logPresetActivity(
    preset: ActivityPreset,
    amount: Double,
    customNote: String = "",
    targetEpochDay: Long = _selectedEpochDay.value
  ) {
    viewModelScope.launch {
      val emitted = preset.factorKgPerUnit * amount
      val saved = preset.savedKgPerUnit * amount
      val entity = CarbonActivityEntity(
        category = preset.category.name,
        presetId = preset.id,
        title = "${preset.title} ($amount ${preset.unit})",
        quantity = amount,
        unit = preset.unit,
        co2EmittedKg = (emitted * 100.0).toLong() / 100.0,
        co2SavedKg = (saved * 100.0).toLong() / 100.0,
        dateEpochDay = targetEpochDay,
        timestamp = System.currentTimeMillis(),
        note = customNote.ifBlank { preset.description }
      )
      repository.insertActivity(entity)
    }
  }

  fun logCustomActivity(
    category: ActivityCategory,
    title: String,
    quantity: Double,
    unit: String,
    factorKgPerUnit: Double,
    isGreen: Boolean,
    note: String = "",
    targetEpochDay: Long = _selectedEpochDay.value
  ) {
    viewModelScope.launch {
      val emitted = if (isGreen) 0.0 else factorKgPerUnit * quantity
      val saved = if (isGreen) factorKgPerUnit * quantity else 0.0
      val entity = CarbonActivityEntity(
        category = category.name,
        presetId = "custom",
        title = "$title ($quantity $unit)",
        quantity = quantity,
        unit = unit,
        co2EmittedKg = (emitted * 100.0).toLong() / 100.0,
        co2SavedKg = (saved * 100.0).toLong() / 100.0,
        dateEpochDay = targetEpochDay,
        timestamp = System.currentTimeMillis(),
        note = note
      )
      repository.insertActivity(entity)
    }
  }

  fun deleteActivity(id: Long) {
    viewModelScope.launch {
      repository.deleteActivity(id)
    }
  }

  fun togglePledgedTip(tipId: String) {
    viewModelScope.launch {
      repository.togglePledgedTip(tipId)
    }
  }

  companion object {
    fun provideFactory(application: Application): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          val database = CarbonDatabase.getDatabase(application)
          val repository = CarbonRepository(database.carbonDao())
          return CarbonViewModel(application, repository) as T
        }
      }
  }
}
