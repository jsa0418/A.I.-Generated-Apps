package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityCategory
import com.example.data.model.ActivityPreset
import com.example.data.model.ActivityPresetsCatalog
import com.example.ui.CarbonViewModel
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

@Composable
fun LogActivityScreen(
  viewModel: CarbonViewModel,
  onActivityLogged: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf(ActivityCategory.TRANSPORT) }
  val presets = remember(selectedCategory) {
    ActivityPresetsCatalog.getPresetsForCategory(selectedCategory)
  }
  var selectedPreset by remember { mutableStateOf(presets.firstOrNull()) }
  var amount by remember { mutableDoubleStateOf(selectedPreset?.defaultAmount ?: 10.0) }
  var noteText by remember { mutableStateOf("") }
  var showSuccessNotification by remember { mutableStateOf(false) }

  // Update selectedPreset when category changes
  fun onSelectCategory(category: ActivityCategory) {
    selectedCategory = category
    val newPresets = ActivityPresetsCatalog.getPresetsForCategory(category)
    val first = newPresets.firstOrNull()
    selectedPreset = first
    amount = first?.defaultAmount ?: 1.0
  }

  fun onSelectPreset(preset: ActivityPreset) {
    selectedPreset = preset
    amount = preset.defaultAmount
  }

  val emitted = (selectedPreset?.factorKgPerUnit ?: 0.0) * amount
  val saved = (selectedPreset?.savedKgPerUnit ?: 0.0) * amount

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("log_activity_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Log Activity",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Select an activity to calculate its exact carbon impact",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Success banner if just logged
    item {
      AnimatedVisibility(visible = showSuccessNotification) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = NaturalSageLight,
          border = BorderStroke(1.dp, NaturalSageBorder),
          modifier = Modifier.fillMaxWidth().testTag("log_success_banner")
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = NaturalPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Activity successfully added to your footprint!",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = NaturalPrimary
            )
          }
        }
      }
    }

    // Category Tabs
    item {
      ScrollableTabRow(
        selectedTabIndex = selectedCategory.ordinal,
        edgePadding = 0.dp,
        containerColor = Color.Transparent,
        divider = {}
      ) {
        ActivityCategory.values().forEach { category ->
          val isSelected = selectedCategory == category
          Tab(
            selected = isSelected,
            onClick = { onSelectCategory(category) },
            text = {
              Text(
                text = category.displayName,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) category.color else MaterialTheme.colorScheme.onSurfaceVariant
              )
            },
            modifier = Modifier.testTag("log_tab_${category.name.lowercase()}")
          )
        }
      }
    }

    // Interactive Calculator Card
    selectedPreset?.let { preset ->
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = NaturalSurface),
          border = BorderStroke(1.dp, NaturalSageBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier.fillMaxWidth().testTag("activity_calculator_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp)
          ) {
            // Preset Title & Icon
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(Color.White)
                  .border(1.dp, NaturalSageBorder.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = preset.icon,
                  contentDescription = preset.title,
                  tint = preset.category.color,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = preset.title,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = NaturalTextPrimary
                )
                Text(
                  text = preset.description,
                  style = MaterialTheme.typography.bodySmall,
                  color = NaturalTextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Amount / Quantity Stepper
            Text(
              text = "Quantity / Usage (${preset.unit}):",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              // Stepper Controls
              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = {
                    val step = if (preset.unit == "km") 5.0 else 1.0
                    if (amount > step) amount -= step
                  },
                  modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .testTag("stepper_decrement")
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                  text = if (amount % 1.0 == 0.0) "${amount.toInt()}" else String.format("%.1f", amount),
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                  text = preset.unit,
                  style = MaterialTheme.typography.titleSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                  onClick = {
                    val step = if (preset.unit == "km") 5.0 else 1.0
                    amount += step
                  },
                  modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .testTag("stepper_increment")
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Increase")
                }
              }

              // Preset Quick Jump Chips
              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(
                  if (preset.unit == "km") 10.0 else 1.0,
                  if (preset.unit == "km") 25.0 else 2.0,
                  if (preset.unit == "km") 50.0 else 5.0
                ).forEach { quickVal ->
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { amount = quickVal }
                  ) {
                    Text(
                      text = "${quickVal.toInt()}",
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.SemiBold,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Calculation Outcome Box
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = if (preset.isGreenAlternative) NaturalSageLight else NaturalSageBorder.copy(alpha = 0.35f),
              border = BorderStroke(1.dp, NaturalSageBorder),
              modifier = Modifier.fillMaxWidth().testTag("live_calculation_box")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Estimated Carbon Footprint",
                    style = MaterialTheme.typography.labelSmall,
                    color = NaturalTextSecondary
                  )
                  Text(
                    text = "${String.format("%.2f", emitted)} kg CO₂e",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (emitted == 0.0) NaturalPrimary else NaturalTextPrimary
                  )
                  Text(
                    text = "Formula: $amount ${preset.unit} × ${preset.factorKgPerUnit} kg/${preset.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = NaturalTextSecondary.copy(alpha = 0.8f)
                  )
                }

                if (preset.isGreenAlternative && saved > 0) {
                  Surface(
                    shape = CircleShape,
                    color = NaturalPrimary
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.Eco, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = "+${String.format("%.2f", saved)} kg saved",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                      )
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Optional note
            OutlinedTextField(
              value = noteText,
              onValueChange = { noteText = it },
              label = { Text("Note (Optional, e.g. location or meal details)") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth().testTag("activity_note_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Log Button
            Button(
              onClick = {
                viewModel.logPresetActivity(preset, amount, noteText)
                noteText = ""
                showSuccessNotification = true
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = NaturalPrimary,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_log_activity_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Log Activity",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Available presets for category
    item {
      Text(
        text = "Or Choose Another ${selectedCategory.displayName} Activity:",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    }

    items(presets) { preset ->
      val isSelected = preset.id == selectedPreset?.id
      PresetSelectionRow(
        preset = preset,
        isSelected = isSelected,
        onClick = { onSelectPreset(preset) }
      )
    }
  }
}

@Composable
fun PresetSelectionRow(
  preset: ActivityPreset,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) NaturalSageLight else NaturalSurface
    ),
    border = BorderStroke(1.dp, if (isSelected) NaturalPrimary else NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("preset_row_${preset.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Color.White)
          .border(1.dp, NaturalSageBorder.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = preset.icon,
          contentDescription = preset.title,
          tint = preset.category.color,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = preset.title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = NaturalTextPrimary
        )
        Text(
          text = "${preset.factorKgPerUnit} kg CO₂e / ${preset.unit}",
          style = MaterialTheme.typography.bodySmall,
          color = NaturalTextSecondary
        )
      }

      if (preset.isGreenAlternative) {
        Surface(
          shape = CircleShape,
          color = NaturalPrimary
        ) {
          Text(
            text = "Eco Choice",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }
  }
}
