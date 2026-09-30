package com.example.data.model

enum class TipImpactLevel(val label: String, val badgeColorHex: Long) {
  HIGH_IMPACT("High Impact", 0xFF059669),
  MEDIUM_IMPACT("Medium Impact", 0xFF0284C7),
  QUICK_WIN("Quick Win", 0xFF7C3AED)
}

data class EcoTip(
  val id: String,
  val title: String,
  val category: ActivityCategory,
  val impactLevel: TipImpactLevel,
  val annualCo2SavingsKg: Double,
  val summary: String,
  val actionSteps: List<String>,
  val difficulty: String = "Easy" // "Easy", "Moderate", "High"
)

object EcoTipsCatalog {
  val tips: List<EcoTip> = listOf(
    EcoTip(
      id = "tip_plant_days",
      title = "Adopt 'Meatless Monday' or 2 Plant Days",
      category = ActivityCategory.FOOD,
      impactLevel = TipImpactLevel.HIGH_IMPACT,
      annualCo2SavingsKg = 380.0,
      summary = "Replacing 2 beef or lamb meals per week with legumes, tofu, or grains cuts hundreds of kg of methane & CO2 annually.",
      actionSteps = listOf(
        "Start by swapping Monday dinners to lentil curry, bean chili, or roasted veggies",
        "Keep plant protein staples (canned beans, lentils, chickpeas) in your pantry",
        "Explore delicious Mediterranean and Asian plant-forward recipes"
      ),
      difficulty = "Easy"
    ),
    EcoTip(
      id = "tip_bike_short_trips",
      title = "Walk or Bike for Trips Under 3 km",
      category = ActivityCategory.TRANSPORT,
      impactLevel = TipImpactLevel.HIGH_IMPACT,
      annualCo2SavingsKg = 420.0,
      summary = "Over 40% of urban car trips are under 3 miles. Swapping 3 short drives per week for a bike or walk makes a huge difference.",
      actionSteps = listOf(
        "Equip your bicycle with a rack or pannier bag for grocery runs",
        "Test a safe local cycle route on a relaxed weekend morning",
        "Combine short errands into a single walking loop"
      ),
      difficulty = "Moderate"
    ),
    EcoTip(
      id = "tip_cold_water_wash",
      title = "Wash Laundry at 30°C or Cold Water",
      category = ActivityCategory.ENERGY,
      impactLevel = TipImpactLevel.QUICK_WIN,
      annualCo2SavingsKg = 90.0,
      summary = "Up to 90% of a washing machine's power goes to heating water. Modern detergents clean just as effectively in cold water.",
      actionSteps = listOf(
        "Set your washing machine's default cycle to 30°C (86°F) or Eco-Cold",
        "Only wash full loads to optimize water and cycle efficiency",
        "Line dry or hang dry garments on a clothes rack whenever sunny or breezy"
      ),
      difficulty = "Easy"
    ),
    EcoTip(
      id = "tip_thermostat_tweak",
      title = "Dial Thermostat by 1°C (2°F)",
      category = ActivityCategory.ENERGY,
      impactLevel = TipImpactLevel.HIGH_IMPACT,
      annualCo2SavingsKg = 260.0,
      summary = "Adjusting your room temperature 1°C lower in winter and 1°C higher in summer lowers energy bills and home emissions by 8-10%.",
      actionSteps = listOf(
        "Wear cozy wool socks or a knit sweater indoors during cooler months",
        "Use programmable or smart thermostat schedules when sleeping or away",
        "Seal drafty windows and door bottoms with weather stripping"
      ),
      difficulty = "Easy"
    ),
    EcoTip(
      id = "tip_public_transit",
      title = "Replace 2 Solo Commutes with Transit",
      category = ActivityCategory.TRANSPORT,
      impactLevel = TipImpactLevel.HIGH_IMPACT,
      annualCo2SavingsKg = 510.0,
      summary = "Trains and city buses produce 65-80% fewer emissions per passenger kilometer than single-occupancy petrol vehicles.",
      actionSteps = listOf(
        "Get a transit smart card or download the municipal transit ticketing app",
        "Turn transit commute time into reading, podcasts, or relaxing downtime",
        "Check if your employer offers pre-tax commuter transit subsidies"
      ),
      difficulty = "Moderate"
    ),
    EcoTip(
      id = "tip_cut_food_waste",
      title = "Weekly Fridge 'Use-It-Up' Meals",
      category = ActivityCategory.FOOD,
      impactLevel = TipImpactLevel.MEDIUM_IMPACT,
      annualCo2SavingsKg = 210.0,
      summary = "Discarded food in landfills generates potent methane. The average household tosses 20-30% of edible groceries.",
      actionSteps = listOf(
        "Designate a 'Eat First' bin on the middle shelf for aging produce",
        "Turn leftover veggies into stir-fry, frittata, or freezer soup stocks",
        "Shop with a concise grocery list rather than buying bulk perishables"
      ),
      difficulty = "Easy"
    ),
    EcoTip(
      id = "tip_unplug_vampire",
      title = "Kill Vampire Standby Power",
      category = ActivityCategory.ENERGY,
      impactLevel = TipImpactLevel.QUICK_WIN,
      annualCo2SavingsKg = 65.0,
      summary = "Idle chargers, game consoles, and TV boxes draw phantom power 24/7, accounting for up to 10% of residential electric draw.",
      actionSteps = listOf(
        "Plug home entertainment gear and computer setups into switchable power strips",
        "Switch the power strip toggle OFF when turning in for the night",
        "Unplug phone & laptop chargers when devices finish charging"
      ),
      difficulty = "Easy"
    ),
    EcoTip(
      id = "tip_thrift_first",
      title = "Adopt the 'Thrift / Borrow First' Rule",
      category = ActivityCategory.SHOPPING,
      impactLevel = TipImpactLevel.HIGH_IMPACT,
      annualCo2SavingsKg = 310.0,
      summary = "The fashion and consumer electronics industries emit massive supply-chain carbon. Extending item life is the highest leverage move.",
      actionSteps = listOf(
        "Wait 48 hours before purchasing non-essential new clothing or gadgets",
        "Check local thrift stores, Poshmark, eBay, or Buy Nothing groups first",
        "Repair, patch, or alter high-quality clothing rather than tossing it"
      ),
      difficulty = "Moderate"
    ),
    EcoTip(
      id = "tip_slow_shipping",
      title = "Consolidate Deliveries & Choose Standard Shipping",
      category = ActivityCategory.SHOPPING,
      impactLevel = TipImpactLevel.QUICK_WIN,
      annualCo2SavingsKg = 55.0,
      summary = "Same-day and express air shipping rely on half-empty delivery vans and air freight with 6x the footprint of consolidated ground delivery.",
      actionSteps = listOf(
        "Batch online shopping items into a designated weekly 'Delivery Day'",
        "Select slower ground shipping at checkout instead of overnight air freight",
        "Pick up packages at local parcel lockers when already running errands"
      ),
      difficulty = "Easy"
    ),
    EcoTip(
      id = "tip_shower_timer",
      title = "Trim Shower Time by 3 Minutes",
      category = ActivityCategory.ENERGY,
      impactLevel = TipImpactLevel.QUICK_WIN,
      annualCo2SavingsKg = 120.0,
      summary = "Cutting hot shower duration from 10 minutes to 7 minutes saves thousands of liters of heated water every year.",
      actionSteps = listOf(
        "Listen to a 4-minute song playlist as a fun shower timer",
        "Install an aerating low-flow showerhead to cut water consumption without losing pressure",
        "Turn off the stream while lathering shampoo or soap"
      ),
      difficulty = "Easy"
    )
  )

  fun getTipsForCategory(category: ActivityCategory): List<EcoTip> {
    return tips.filter { it.category == category }
  }

  fun getTipById(id: String): EcoTip? {
    return tips.find { it.id == id }
  }
}
