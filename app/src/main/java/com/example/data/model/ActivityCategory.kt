package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.EnergyAmberLight
import com.example.ui.theme.FoodOrange
import com.example.ui.theme.FoodOrangeLight
import com.example.ui.theme.ShoppingPurple
import com.example.ui.theme.ShoppingPurpleLight
import com.example.ui.theme.TransportBlue
import com.example.ui.theme.TransportBlueLight

enum class ActivityCategory(
  val displayName: String,
  val iconName: String,
  val color: Color,
  val lightColor: Color,
  val defaultUnit: String
) {
  TRANSPORT(
    displayName = "Transport",
    iconName = "transport",
    color = TransportBlue,
    lightColor = TransportBlueLight,
    defaultUnit = "km"
  ),
  FOOD(
    displayName = "Food & Diet",
    iconName = "food",
    color = FoodOrange,
    lightColor = FoodOrangeLight,
    defaultUnit = "meals"
  ),
  ENERGY(
    displayName = "Home Energy",
    iconName = "energy",
    color = EnergyAmber,
    lightColor = EnergyAmberLight,
    defaultUnit = "kWh"
  ),
  SHOPPING(
    displayName = "Goods & Waste",
    iconName = "shopping",
    color = ShoppingPurple,
    lightColor = ShoppingPurpleLight,
    defaultUnit = "items"
  )
}

data class ActivityPreset(
  val id: String,
  val category: ActivityCategory,
  val title: String,
  val description: String,
  val unit: String,
  val factorKgPerUnit: Double,
  val defaultAmount: Double,
  val isGreenAlternative: Boolean = false,
  val savedKgPerUnit: Double = 0.0,
  val icon: ImageVector
)

object ActivityPresetsCatalog {
  val presets: List<ActivityPreset> = listOf(
    // Transport presets
    ActivityPreset(
      id = "trans_petrol_car",
      category = ActivityCategory.TRANSPORT,
      title = "Petrol / Gas Car",
      description = "Average passenger combustion car",
      unit = "km",
      factorKgPerUnit = 0.192,
      defaultAmount = 20.0,
      icon = Icons.Default.DirectionsCar
    ),
    ActivityPreset(
      id = "trans_electric_car",
      category = ActivityCategory.TRANSPORT,
      title = "Electric Vehicle (EV)",
      description = "Battery EV (grid energy average)",
      unit = "km",
      factorKgPerUnit = 0.053,
      defaultAmount = 25.0,
      isGreenAlternative = true,
      savedKgPerUnit = 0.139, // Saved vs petrol car
      icon = Icons.Default.ElectricBolt
    ),
    ActivityPreset(
      id = "trans_bus",
      category = ActivityCategory.TRANSPORT,
      title = "Public Bus",
      description = "City commuter bus transit",
      unit = "km",
      factorKgPerUnit = 0.089,
      defaultAmount = 15.0,
      isGreenAlternative = true,
      savedKgPerUnit = 0.103, // Saved vs driving solo
      icon = Icons.Default.DirectionsBus
    ),
    ActivityPreset(
      id = "trans_train",
      category = ActivityCategory.TRANSPORT,
      title = "Train / Subway",
      description = "Rail transit or underground metro",
      unit = "km",
      factorKgPerUnit = 0.035,
      defaultAmount = 20.0,
      isGreenAlternative = true,
      savedKgPerUnit = 0.157, // Saved vs driving solo
      icon = Icons.Default.Train
    ),
    ActivityPreset(
      id = "trans_bike_walk",
      category = ActivityCategory.TRANSPORT,
      title = "Walk or Bicycle",
      description = "Zero emission active transit",
      unit = "km",
      factorKgPerUnit = 0.0,
      defaultAmount = 5.0,
      isGreenAlternative = true,
      savedKgPerUnit = 0.192, // 100% saved vs petrol car
      icon = Icons.Default.DirectionsBike
    ),
    ActivityPreset(
      id = "trans_flight_short",
      category = ActivityCategory.TRANSPORT,
      title = "Flight (Domestic)",
      description = "Short-haul domestic flight (<3 hrs)",
      unit = "km",
      factorKgPerUnit = 0.246,
      defaultAmount = 500.0,
      icon = Icons.Default.Flight
    ),

    // Food presets
    ActivityPreset(
      id = "food_beef_lamb",
      category = ActivityCategory.FOOD,
      title = "Red Meat (Beef / Lamb)",
      description = "High emission ruminant livestock meal",
      unit = "meal",
      factorKgPerUnit = 6.2,
      defaultAmount = 1.0,
      icon = Icons.Default.Restaurant
    ),
    ActivityPreset(
      id = "food_poultry_pork",
      category = ActivityCategory.FOOD,
      title = "Poultry / Pork Meal",
      description = "Chicken, turkey, or pork dish",
      unit = "meal",
      factorKgPerUnit = 1.8,
      defaultAmount = 1.0,
      icon = Icons.Default.Fastfood
    ),
    ActivityPreset(
      id = "food_vegetarian",
      category = ActivityCategory.FOOD,
      title = "Vegetarian Meal",
      description = "Plant-forward meal with dairy/eggs",
      unit = "meal",
      factorKgPerUnit = 0.8,
      defaultAmount = 1.0,
      isGreenAlternative = true,
      savedKgPerUnit = 5.4, // Saved compared to beef
      icon = Icons.Default.NaturePeople
    ),
    ActivityPreset(
      id = "food_vegan",
      category = ActivityCategory.FOOD,
      title = "Plant-Based (Vegan) Meal",
      description = "100% whole plant ingredients",
      unit = "meal",
      factorKgPerUnit = 0.45,
      defaultAmount = 1.0,
      isGreenAlternative = true,
      savedKgPerUnit = 5.75, // Saved compared to beef
      icon = Icons.Default.NaturePeople
    ),

    // Energy presets
    ActivityPreset(
      id = "energy_electricity",
      category = ActivityCategory.ENERGY,
      title = "Electricity Usage",
      description = "Grid electricity consumption",
      unit = "kWh",
      factorKgPerUnit = 0.385,
      defaultAmount = 10.0,
      icon = Icons.Default.Power
    ),
    ActivityPreset(
      id = "energy_ac_heating",
      category = ActivityCategory.ENERGY,
      title = "AC / Space Heater",
      description = "Climate control cooling or heating",
      unit = "hours",
      factorKgPerUnit = 1.2,
      defaultAmount = 4.0,
      icon = Icons.Default.WbSunny
    ),
    ActivityPreset(
      id = "energy_hot_shower",
      category = ActivityCategory.ENERGY,
      title = "Hot Shower",
      description = "Water heating energy",
      unit = "showers",
      factorKgPerUnit = 0.85,
      defaultAmount = 1.0,
      icon = Icons.Default.Shower
    ),
    ActivityPreset(
      id = "energy_laundry_cold",
      category = ActivityCategory.ENERGY,
      title = "Cold Water Laundry",
      description = "Eco cold wash cycle vs hot wash",
      unit = "loads",
      factorKgPerUnit = 0.20,
      defaultAmount = 1.0,
      isGreenAlternative = true,
      savedKgPerUnit = 0.40,
      icon = Icons.Default.LocalLaundryService
    ),

    // Shopping presets
    ActivityPreset(
      id = "shop_clothing",
      category = ActivityCategory.SHOPPING,
      title = "New Clothing Item",
      description = "Fast fashion retail apparel",
      unit = "items",
      factorKgPerUnit = 12.0,
      defaultAmount = 1.0,
      icon = Icons.Default.ShoppingBag
    ),
    ActivityPreset(
      id = "shop_secondhand",
      category = ActivityCategory.SHOPPING,
      title = "Thrift / Re-used Item",
      description = "Second-hand clothing or goods",
      unit = "items",
      factorKgPerUnit = 0.5,
      defaultAmount = 1.0,
      isGreenAlternative = true,
      savedKgPerUnit = 11.5,
      icon = Icons.Default.LocalMall
    ),
    ActivityPreset(
      id = "shop_delivery",
      category = ActivityCategory.SHOPPING,
      title = "Online Package Delivery",
      description = "E-commerce shipment & packaging",
      unit = "packages",
      factorKgPerUnit = 1.8,
      defaultAmount = 1.0,
      icon = Icons.Default.ShoppingBag
    ),
    ActivityPreset(
      id = "shop_electronics",
      category = ActivityCategory.SHOPPING,
      title = "Electronics / Tech Device",
      description = "New smartphone or small gadget",
      unit = "devices",
      factorKgPerUnit = 55.0,
      defaultAmount = 1.0,
      icon = Icons.Default.Smartphone
    )
  )

  fun getPresetsForCategory(category: ActivityCategory): List<ActivityPreset> {
    return presets.filter { it.category == category }
  }

  fun getPresetById(id: String): ActivityPreset? {
    return presets.find { it.id == id }
  }
}
