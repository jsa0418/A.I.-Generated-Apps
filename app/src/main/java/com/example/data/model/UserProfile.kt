package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
  @PrimaryKey val id: Long = 1L,
  val name: String = "Eco Traveler",
  val age: Int = 28,
  val heightCm: Double = 170.0,
  val weightKg: Double = 65.0,
  // Food Habits & Preferences
  val dietType: String = "Omnivore", // Omnivore, Flexitarian, Pescatarian, Vegetarian, Vegan
  val foodSourcing: String = "Conventional & Local", // Mostly Local & Seasonal, Conventional & Local, Mostly Imported
  val foodWasteLevel: String = "Low Waste", // Low Waste, Moderate, High Waste
  // Drinks Habits & Preferences
  val dailyCoffeeTeaCups: Int = 2,
  val drinkMilkType: String = "Oat Milk", // None/Black, Oat Milk, Soy Milk, Dairy Milk
  val waterHabit: String = "Reusable Bottle", // Reusable Bottle, Tap Water, Bottled Water
  // Lifestyle Habits & Preferences
  val primaryCommute: String = "Public Transit", // Bicycle / Walk, Public Transit, Electric Vehicle, Carpool, Gasoline Car
  val dailyCommuteKm: Double = 15.0,
  val homeClimateControl: String = "Moderate Eco", // Minimal Eco, Moderate Eco, High Comfort
  val laundryHabit: String = "Cold Water Wash", // Cold Water Wash, Warm Wash & Line Dry, Tumble Dryer
  val shoppingHabit: String = "Secondhand & Mindful", // Secondhand & Mindful, Balanced, Frequent Retail
  // Health, Physiology & Special Needs Justification
  val healthCondition: String = "Healthy", // Healthy, Respiratory / Asthma, Mobility Limitation, Cardiovascular, Chronic Fatigue, Other
  val mobilityNeeds: Boolean = false, // Justifies vehicle transit over walking/biking
  val climateMedicalNeed: Boolean = false, // Justifies constant AC/heating or HEPA air filtration
  val medicalEquipment: Boolean = false, // Justifies electricity for CPAP, oxygen, power chair, medication fridge
  val dietaryMedicalRestriction: String = "None", // None, Celiac / Gluten-free, Renal Diet, High-Protein Prescribed, Hypoallergenic
  val specialNeedsNotes: String = ""
) {
  val bmi: Double
    get() {
      val heightM = heightCm / 100.0
      return if (heightM > 0) weightKg / (heightM * heightM) else 22.0
    }

  /**
   * Basal Metabolic Rate (Mifflin-St Jeor formula approximation)
   */
  val bmrCalories: Double
    get() {
      // 10 * weight(kg) + 6.25 * height(cm) - 5 * age + 5
      return (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age) + 5.0
    }

  /**
   * Evaluates personalized justified daily carbon baseline and justifications
   */
  fun calculateJustification(): CarbonJustificationSummary {
    val basalDietFactor = when (dietType) {
      "Vegan" -> 1.8
      "Vegetarian" -> 2.5
      "Pescatarian" -> 3.2
      "Flexitarian" -> 3.8
      else -> 4.8 // Omnivore
    }
    // Scale dietary carbon proportional to metabolic energy needs (standard reference 2000 kcal)
    val metabolicDietKg = (bmrCalories / 2000.0) * basalDietFactor

    // Drinks baseline
    val coffeeKg = when (drinkMilkType) {
      "Dairy Milk" -> dailyCoffeeTeaCups * 0.28
      "Oat Milk", "Soy Milk" -> dailyCoffeeTeaCups * 0.12
      else -> dailyCoffeeTeaCups * 0.05
    }
    val waterKg = if (waterHabit == "Bottled Water") 0.35 else 0.01
    val drinksBaselineKg = coffeeKg + waterKg

    // Medical & Special Needs Justifications (explicitly legitimized carbon footprint)
    val medicalEnergyKg = if (medicalEquipment) 0.75 else 0.0
    val medicalClimateKg = if (climateMedicalNeed) 1.25 else 0.0
    val mobilityTransitKg = if (mobilityNeeds) 1.60 else 0.0
    val totalMedicalAllowanceKg = medicalEnergyKg + medicalClimateKg + mobilityTransitKg

    // Justified Commute Baseline
    val commuteFactor = when (primaryCommute) {
      "Bicycle / Walk" -> 0.0
      "Public Transit" -> 0.089 * dailyCommuteKm
      "Electric Vehicle" -> 0.053 * dailyCommuteKm
      "Carpool" -> 0.096 * dailyCommuteKm
      else -> 0.192 * dailyCommuteKm // Gasoline car
    }

    // Home Baseline
    val homeKg = when (homeClimateControl) {
      "Minimal Eco" -> 1.5
      "Moderate Eco" -> 2.5
      else -> 4.0
    }

    val totalJustifiedBaseline = metabolicDietKg + drinksBaselineKg + totalMedicalAllowanceKg + commuteFactor + homeKg

    val justificationPoints = mutableListOf<String>()
    justificationPoints.add(
      "Metabolic baseline: ${String.format("%.1f", bmrCalories)} kcal/day requires ~${String.format("%.2f", metabolicDietKg)} kg CO₂e for dietary sustenance based on ${weightKg.toInt()}kg weight & age $age."
    )

    if (medicalEquipment) {
      justificationPoints.add(
        "Medical device electricity (+0.75 kg/day) is certified essential and justified for health maintenance."
      )
    }
    if (climateMedicalNeed) {
      justificationPoints.add(
        "Temperature & HEPA air filtration (+1.25 kg/day) justified for $healthCondition management."
      )
    }
    if (mobilityNeeds) {
      justificationPoints.add(
        "Accessible motorized transit (+1.60 kg/day) justified due to mobility accessibility requirements."
      )
    }
    if (dietaryMedicalRestriction != "None") {
      justificationPoints.add(
        "Specialized nutrition profile ($dietaryMedicalRestriction) acknowledged in dietary footprint calculation."
      )
    }
    if (specialNeedsNotes.isNotBlank()) {
      justificationPoints.add(
        "Personal accommodation note: \"$specialNeedsNotes\" taken into account."
      )
    }

    return CarbonJustificationSummary(
      metabolicDietKg = metabolicDietKg,
      drinksBaselineKg = drinksBaselineKg,
      medicalAllowanceKg = totalMedicalAllowanceKg,
      commuteBaselineKg = commuteFactor,
      homeBaselineKg = homeKg,
      totalRecommendedBudgetKg = (totalJustifiedBaseline * 10.0).toInt() / 10.0,
      justificationPoints = justificationPoints
    )
  }
}

data class CarbonJustificationSummary(
  val metabolicDietKg: Double,
  val drinksBaselineKg: Double,
  val medicalAllowanceKg: Double,
  val commuteBaselineKg: Double,
  val homeBaselineKg: Double,
  val totalRecommendedBudgetKg: Double,
  val justificationPoints: List<String>
)
