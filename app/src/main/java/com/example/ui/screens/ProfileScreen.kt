package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.ui.CarbonUiState
import com.example.ui.CarbonViewModel
import com.example.ui.theme.NaturalDarkContainer
import com.example.ui.theme.NaturalOnSprout
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSproutAccent
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
  uiState: CarbonUiState,
  viewModel: CarbonViewModel
) {
  val profile = uiState.userProfile

  // Editable Form State
  var name by remember(profile.name) { mutableStateOf(profile.name) }
  var ageText by remember(profile.age) { mutableStateOf(profile.age.toString()) }
  var heightText by remember(profile.heightCm) { mutableStateOf(profile.heightCm.toInt().toString()) }
  var weightText by remember(profile.weightKg) { mutableStateOf(profile.weightKg.toInt().toString()) }

  // Habits & Preferences State
  var dietType by remember(profile.dietType) { mutableStateOf(profile.dietType) }
  var foodSourcing by remember(profile.foodSourcing) { mutableStateOf(profile.foodSourcing) }
  var foodWasteLevel by remember(profile.foodWasteLevel) { mutableStateOf(profile.foodWasteLevel) }

  var dailyCups by remember(profile.dailyCoffeeTeaCups) { mutableIntStateOf(profile.dailyCoffeeTeaCups) }
  var drinkMilkType by remember(profile.drinkMilkType) { mutableStateOf(profile.drinkMilkType) }
  var waterHabit by remember(profile.waterHabit) { mutableStateOf(profile.waterHabit) }

  var primaryCommute by remember(profile.primaryCommute) { mutableStateOf(profile.primaryCommute) }
  var commuteDistanceKm by remember(profile.dailyCommuteKm) { mutableDoubleStateOf(profile.dailyCommuteKm) }
  var homeClimateControl by remember(profile.homeClimateControl) { mutableStateOf(profile.homeClimateControl) }
  var laundryHabit by remember(profile.laundryHabit) { mutableStateOf(profile.laundryHabit) }
  var shoppingHabit by remember(profile.shoppingHabit) { mutableStateOf(profile.shoppingHabit) }

  // Health & Special Needs
  var healthCondition by remember(profile.healthCondition) { mutableStateOf(profile.healthCondition) }
  var mobilityNeeds by remember(profile.mobilityNeeds) { mutableStateOf(profile.mobilityNeeds) }
  var climateMedicalNeed by remember(profile.climateMedicalNeed) { mutableStateOf(profile.climateMedicalNeed) }
  var medicalEquipment by remember(profile.medicalEquipment) { mutableStateOf(profile.medicalEquipment) }
  var dietaryMedicalRestriction by remember(profile.dietaryMedicalRestriction) { mutableStateOf(profile.dietaryMedicalRestriction) }
  var specialNeedsNotes by remember(profile.specialNeedsNotes) { mutableStateOf(profile.specialNeedsNotes) }

  var showSavedSuccess by remember { mutableStateOf(false) }

  // Compute live preview profile for dynamic justification preview
  val parsedAge = ageText.toIntOrNull() ?: profile.age
  val parsedHeight = heightText.toDoubleOrNull() ?: profile.heightCm
  val parsedWeight = weightText.toDoubleOrNull() ?: profile.weightKg

  val currentLiveProfile = remember(
    name, parsedAge, parsedHeight, parsedWeight,
    dietType, foodSourcing, foodWasteLevel,
    dailyCups, drinkMilkType, waterHabit,
    primaryCommute, commuteDistanceKm, homeClimateControl,
    laundryHabit, shoppingHabit,
    healthCondition, mobilityNeeds, climateMedicalNeed,
    medicalEquipment, dietaryMedicalRestriction, specialNeedsNotes
  ) {
    UserProfileEntity(
      id = 1L,
      name = name.ifBlank { "User" },
      age = parsedAge,
      heightCm = parsedHeight,
      weightKg = parsedWeight,
      dietType = dietType,
      foodSourcing = foodSourcing,
      foodWasteLevel = foodWasteLevel,
      dailyCoffeeTeaCups = dailyCups,
      drinkMilkType = drinkMilkType,
      waterHabit = waterHabit,
      primaryCommute = primaryCommute,
      dailyCommuteKm = commuteDistanceKm,
      homeClimateControl = homeClimateControl,
      laundryHabit = laundryHabit,
      shoppingHabit = shoppingHabit,
      healthCondition = healthCondition,
      mobilityNeeds = mobilityNeeds,
      climateMedicalNeed = climateMedicalNeed,
      medicalEquipment = medicalEquipment,
      dietaryMedicalRestriction = dietaryMedicalRestriction,
      specialNeedsNotes = specialNeedsNotes
    )
  }

  val liveJustification = remember(currentLiveProfile) {
    currentLiveProfile.calculateJustification()
  }

  LaunchedEffect(showSavedSuccess) {
    if (showSavedSuccess) {
      kotlinx.coroutines.delay(2500)
      showSavedSuccess = false
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFFDFDF6))
      .padding(horizontal = 16.dp)
      .testTag("profile_screen_scroll"),
    contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header & User Identity
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(NaturalPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = (name.firstOrNull() ?: 'U').uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Personal Profile & Baseline",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = NaturalTextPrimary
          )
          Text(
            text = "Tailored carbon allowances & health justifications",
            style = MaterialTheme.typography.bodySmall,
            color = NaturalTextSecondary
          )
        }
      }
    }

    // Success notification banner
    item {
      AnimatedVisibility(visible = showSavedSuccess) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = NaturalSageLight,
          border = BorderStroke(1.dp, NaturalSageBorder),
          modifier = Modifier.fillMaxWidth().testTag("profile_save_banner")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = NaturalPrimary)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Personalized profile and footprint baseline saved!",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = NaturalPrimary
            )
          }
        }
      }
    }

    // Carbon Footprint Justification Card (Hero Card in Dark Natural Container)
    item {
      Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalDarkContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().testTag("carbon_justification_card")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Filled.HealthAndSafety,
                contentDescription = null,
                tint = NaturalSproutAccent,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "JUSTIFIED CARBON BASELINE",
                style = MaterialTheme.typography.labelSmall,
                color = NaturalSproutAccent,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
              )
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = NaturalSproutAccent.copy(alpha = 0.15f)
            ) {
              Text(
                text = "Personalized",
                color = NaturalSproutAccent,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Text(
                text = "${String.format("%.1f", liveJustification.totalRecommendedBudgetKg)} kg",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDFDF6)
              )
              Text(
                text = "Recommended Daily Target for ${name.ifBlank { "You" }}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFDFDF6).copy(alpha = 0.75f)
              )
            }

            Button(
              onClick = {
                viewModel.updateUserProfile(currentLiveProfile, syncBudgetWithJustification = true)
                showSavedSuccess = true
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = NaturalSproutAccent,
                contentColor = NaturalOnSprout
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("apply_justified_budget_button")
            ) {
              Text(
                text = "Apply Target",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
          Spacer(modifier = Modifier.height(14.dp))

          // Baseline Allowances Breakdown
          Text(
            text = "Justified Daily Allowances",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFFFDFDF6).copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            JustificationAllowancePill(
              icon = Icons.Filled.Restaurant,
              label = "Diet BMR",
              value = "${String.format("%.1f", liveJustification.metabolicDietKg)} kg",
              modifier = Modifier.weight(1f)
            )
            JustificationAllowancePill(
              icon = Icons.Filled.LocalDrink,
              label = "Drinks",
              value = "${String.format("%.2f", liveJustification.drinksBaselineKg)} kg",
              modifier = Modifier.weight(1f)
            )
            JustificationAllowancePill(
              icon = Icons.Filled.MedicalServices,
              label = "Health & Needs",
              value = "+${String.format("%.2f", liveJustification.medicalAllowanceKg)} kg",
              isHighlight = liveJustification.medicalAllowanceKg > 0,
              modifier = Modifier.weight(1.2f)
            )
          }

          if (liveJustification.justificationPoints.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
              verticalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.07f), RoundedCornerShape(12.dp))
                .padding(12.dp)
            ) {
              Text(
                text = "Footprint Legitimacy & Accommodations:",
                style = MaterialTheme.typography.labelSmall,
                color = NaturalSproutAccent,
                fontWeight = FontWeight.Bold
              )
              liveJustification.justificationPoints.forEach { point ->
                Row(verticalAlignment = Alignment.Top) {
                  Text(
                    text = "• ",
                    color = Color(0xFFFDFDF6).copy(alpha = 0.8f),
                    fontSize = 12.sp
                  )
                  Text(
                    text = point,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFDFDF6).copy(alpha = 0.85f),
                    lineHeight = 16.sp
                  )
                }
              }
            }
          }
        }
      }
    }

    // Section 1: User Identity, Age, Height & Weight
    item {
      SectionCard(title = "Personal Physiology & Health", icon = Icons.Filled.Person) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Your Name") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NaturalPrimary,
            unfocusedBorderColor = NaturalSageBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = ageText,
            onValueChange = { if (it.length <= 3) ageText = it },
            label = { Text("Age (yrs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NaturalPrimary,
              unfocusedBorderColor = NaturalSageBorder
            ),
            modifier = Modifier.weight(1f).testTag("profile_age_input")
          )
          OutlinedTextField(
            value = heightText,
            onValueChange = { if (it.length <= 3) heightText = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NaturalPrimary,
              unfocusedBorderColor = NaturalSageBorder
            ),
            modifier = Modifier.weight(1f).testTag("profile_height_input")
          )
          OutlinedTextField(
            value = weightText,
            onValueChange = { if (it.length <= 3) weightText = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NaturalPrimary,
              unfocusedBorderColor = NaturalSageBorder
            ),
            modifier = Modifier.weight(1f).testTag("profile_weight_input")
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Live BMI and BMR calculated indicators
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = NaturalSageLight.copy(alpha = 0.5f),
          border = BorderStroke(1.dp, NaturalSageBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Body Mass Index", style = MaterialTheme.typography.labelSmall, color = NaturalTextSecondary)
              Text(
                "${String.format("%.1f", currentLiveProfile.bmi)} kg/m²",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
              )
            }
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(NaturalSageBorder))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Basal Metabolic Rate", style = MaterialTheme.typography.labelSmall, color = NaturalTextSecondary)
              Text(
                "${currentLiveProfile.bmrCalories.toInt()} kcal/day",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = NaturalPrimary
              )
            }
          }
        }
      }
    }

    // Section 2: Health Conditions & Special Needs Justification
    item {
      SectionCard(title = "Health Condition & Special Needs", icon = Icons.Filled.MedicalInformation) {
        Text(
          text = "Health accommodations justify necessary energy, transit, and nutritional requirements.",
          style = MaterialTheme.typography.bodySmall,
          color = NaturalTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Primary Health Profile", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf(
            "Healthy",
            "Respiratory / Asthma",
            "Mobility Limitation",
            "Cardiovascular",
            "Chronic Fatigue",
            "Other Condition"
          ).forEach { condition ->
            val selected = healthCondition == condition
            FilterChip(
              selected = selected,
              onClick = { healthCondition = condition },
              label = { Text(condition) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = NaturalSageBorder.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(12.dp))

        Text("Essential Accommodations (Footprint Justified)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        // Switch 1: Medical Equipment
        AccommodationToggle(
          title = "Medical Device & Refrigeration Power",
          subtitle = "Powers CPAP, oxygen concentrator, wheelchair, dialysis, or insulin cooler (+0.75 kg CO₂e/day justified)",
          checked = medicalEquipment,
          onCheckedChange = { medicalEquipment = it },
          testTag = "toggle_medical_equipment"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Switch 2: Climate & HEPA air filtration
        AccommodationToggle(
          title = "Mandatory Climate & Air Filtration",
          subtitle = "Continuous AC/heating or medical HEPA filtration for respiratory/cardiac care (+1.25 kg CO₂e/day justified)",
          checked = climateMedicalNeed,
          onCheckedChange = { climateMedicalNeed = it },
          testTag = "toggle_climate_medical"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Switch 3: Accessible Transit
        AccommodationToggle(
          title = "Accessible Motorized Transit",
          subtitle = "Physical condition requires vehicular transportation over active walking/biking (+1.60 kg CO₂e/day justified)",
          checked = mobilityNeeds,
          onCheckedChange = { mobilityNeeds = it },
          testTag = "toggle_mobility_needs"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Dietary Medical Restrictions", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("None", "Celiac / Gluten-free", "Renal Diet", "High-Protein Prescribed", "Hypoallergenic").forEach { restriction ->
            val selected = dietaryMedicalRestriction == restriction
            FilterChip(
              selected = selected,
              onClick = { dietaryMedicalRestriction = restriction },
              label = { Text(restriction) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = specialNeedsNotes,
          onValueChange = { specialNeedsNotes = it },
          label = { Text("Special Needs & Justification Notes (Optional)") },
          placeholder = { Text("e.g. daily medical clinic visits or specific thermal thresholds") },
          singleLine = false,
          maxLines = 3,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NaturalPrimary,
            unfocusedBorderColor = NaturalSageBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("profile_special_needs_notes")
        )
      }
    }

    // Section 3: Food Habits & Preferences
    item {
      SectionCard(title = "Food Habits & Preferences", icon = Icons.Filled.Restaurant) {
        Text("Dietary Pattern", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Vegan", "Vegetarian", "Pescatarian", "Flexitarian", "Omnivore").forEach { diet ->
            val selected = dietType == diet
            FilterChip(
              selected = selected,
              onClick = { dietType = diet },
              label = { Text(diet) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              ),
              modifier = Modifier.testTag("chip_diet_$diet")
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Food Sourcing Habit", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Mostly Local & Seasonal", "Conventional & Local", "Mostly Imported").forEach { sourcing ->
            val selected = foodSourcing == sourcing
            FilterChip(
              selected = selected,
              onClick = { foodSourcing = sourcing },
              label = { Text(sourcing) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Food Waste Management", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Low Waste (Composting / Plans)", "Moderate Waste", "Frequent Leftovers").forEach { waste ->
            val selected = foodWasteLevel.startsWith(waste.substring(0, 5))
            FilterChip(
              selected = selected,
              onClick = { foodWasteLevel = waste },
              label = { Text(waste) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }
      }
    }

    // Section 4: Drinks Habits & Preferences
    item {
      SectionCard(title = "Drinks & Beverage Habits", icon = Icons.Filled.LocalDrink) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Coffee / Tea Intake", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
          Text("$dailyCups cups/day", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NaturalPrimary)
        }
        Slider(
          value = dailyCups.toFloat(),
          onValueChange = { dailyCups = it.toInt() },
          valueRange = 0f..6f,
          steps = 5,
          colors = SliderDefaults.colors(
            thumbColor = NaturalPrimary,
            activeTrackColor = NaturalPrimary,
            inactiveTrackColor = NaturalSageBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("slider_coffee_cups")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Milk Preference for Beverages", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Black / None", "Oat Milk", "Soy Milk", "Dairy Milk").forEach { milk ->
            val selected = drinkMilkType == milk
            FilterChip(
              selected = selected,
              onClick = { drinkMilkType = milk },
              label = { Text(milk) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Drinking Water Preference", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Reusable Bottle", "Tap Water", "Bottled Water").forEach { water ->
            val selected = waterHabit == water
            FilterChip(
              selected = selected,
              onClick = { waterHabit = water },
              label = { Text(water) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }
      }
    }

    // Section 5: Lifestyle & Commute Habits
    item {
      SectionCard(title = "Lifestyle & Living Habits", icon = Icons.Filled.Tune) {
        Text("Primary Commute Mode", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Bicycle / Walk", "Public Transit", "Electric Vehicle", "Carpool", "Gasoline Car").forEach { commute ->
            val selected = primaryCommute == commute
            FilterChip(
              selected = selected,
              onClick = { primaryCommute = commute },
              label = { Text(commute) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Daily Commute Distance", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
          Text("${commuteDistanceKm.toInt()} km/day", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NaturalPrimary)
        }
        Slider(
          value = commuteDistanceKm.toFloat(),
          onValueChange = { commuteDistanceKm = it.toDouble() },
          valueRange = 0f..50f,
          steps = 9,
          colors = SliderDefaults.colors(
            thumbColor = NaturalPrimary,
            activeTrackColor = NaturalPrimary,
            inactiveTrackColor = NaturalSageBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("slider_commute_km")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Home Climate Regulation Habit", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Minimal Eco", "Moderate Eco", "High Comfort").forEach { climate ->
            val selected = homeClimateControl == climate
            FilterChip(
              selected = selected,
              onClick = { homeClimateControl = climate },
              label = { Text(climate) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Laundry Routine", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("Cold Water Wash", "Warm Wash & Line Dry", "Tumble Dryer").forEach { laundry ->
            val selected = laundryHabit == laundry
            FilterChip(
              selected = selected,
              onClick = { laundryHabit = laundry },
              label = { Text(laundry) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NaturalSageLight,
                selectedLabelColor = NaturalPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NaturalSageBorder,
                selectedBorderColor = NaturalPrimary,
                enabled = true,
                selected = selected
              )
            )
          }
        }
      }
    }

    // Save & Commit Button
    item {
      Button(
        onClick = {
          viewModel.updateUserProfile(currentLiveProfile, syncBudgetWithJustification = false)
          showSavedSuccess = true
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = NaturalPrimary,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("save_profile_button")
      ) {
        Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Save Personalized Profile & Preferences",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        )
      }
    }
  }
}

@Composable
private fun SectionCard(
  title: String,
  icon: ImageVector,
  content: @Composable () -> Unit
) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = NaturalSurface),
    border = BorderStroke(1.dp, NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, NaturalSageBorder, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NaturalPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NaturalTextPrimary
        )
      }
      Spacer(modifier = Modifier.height(14.dp))
      content()
    }
  }
}

@Composable
private fun AccommodationToggle(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  testTag: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White, RoundedCornerShape(14.dp))
      .border(1.dp, if (checked) NaturalPrimary else NaturalSageBorder.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
      .clickable { onCheckedChange(!checked) }
      .padding(12.dp)
      .testTag(testTag),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = NaturalTextPrimary
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = NaturalTextSecondary,
        fontSize = 11.5.sp,
        lineHeight = 15.sp
      )
    }
    Spacer(modifier = Modifier.width(10.dp))
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = NaturalPrimary,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = NaturalSageBorder
      )
    )
  }
}

@Composable
private fun JustificationAllowancePill(
  icon: ImageVector,
  label: String,
  value: String,
  modifier: Modifier = Modifier,
  isHighlight: Boolean = false
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isHighlight) NaturalSproutAccent.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.1f),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isHighlight) NaturalSproutAccent else Color(0xFFFDFDF6),
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFFFDFDF6).copy(alpha = 0.8f),
        fontSize = 10.sp
      )
      Text(
        text = value,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = if (isHighlight) NaturalSproutAccent else Color(0xFFFDFDF6),
        fontSize = 11.5.sp
      )
    }
  }
}
