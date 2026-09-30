package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityCategory
import com.example.data.model.ActivityPresetsCatalog
import com.example.data.model.CarbonActivityEntity
import com.example.ui.CarbonUiState
import com.example.ui.CarbonViewModel
import com.example.ui.DailyCarbonStat
import com.example.ui.components.CarbonGaugeCard
import com.example.ui.components.CategoryBreakdownSection
import com.example.ui.components.EcoTipCard
import com.example.ui.theme.NaturalBg
import com.example.ui.theme.NaturalDarkContainer
import com.example.ui.theme.NaturalOnSprout
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSproutAccent
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
  uiState: CarbonUiState,
  viewModel: CarbonViewModel,
  onNavigateToLog: () -> Unit,
  onNavigateToTips: () -> Unit,
  onNavigateToProfile: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showBudgetDialog by remember { mutableStateOf(false) }
  var newBudgetInput by remember { mutableStateOf(uiState.dailyBudgetKg.toString()) }

  if (showBudgetDialog) {
    AlertDialog(
      onDismissRequest = { showBudgetDialog = false },
      title = { Text("Set Daily Carbon Target") },
      text = {
        Column {
          Text(
            text = "Enter your personal daily CO₂e budget, or choose your justified baseline calibrated to your health and habits.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = NaturalSageLight,
            border = BorderStroke(1.dp, NaturalSageBorder),
            modifier = Modifier.fillMaxWidth().clickable {
              newBudgetInput = String.format("%.1f", uiState.carbonJustification.totalRecommendedBudgetKg)
            }
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Your Justified Target",
                  style = MaterialTheme.typography.labelSmall,
                  color = NaturalTextSecondary
                )
                Text(
                  text = "${String.format("%.1f", uiState.carbonJustification.totalRecommendedBudgetKg)} kg CO₂e / day",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  color = NaturalPrimary
                )
              }
              Text(
                text = "Use this",
                style = MaterialTheme.typography.labelSmall,
                color = NaturalPrimary,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = newBudgetInput,
            onValueChange = { newBudgetInput = it },
            label = { Text("Daily Budget (kg CO₂e)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("budget_input_field")
          )
        }
      },
      confirmButton = {
        TextButton(
          onClick = {
            newBudgetInput.toDoubleOrNull()?.let {
              viewModel.setDailyBudget(it)
            }
            showBudgetDialog = false
          },
          modifier = Modifier.testTag("budget_dialog_confirm")
        ) {
          Text("Save Target")
        }
      },
      dismissButton = {
        TextButton(onClick = { showBudgetDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("dashboard_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Bar with App Title & Target Settings (Natural Tones header)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.clickable { onNavigateToProfile() }
        ) {
          val todayDateStr = remember {
            LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d"))
          }
          Text(
            text = todayDateStr.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = NaturalTextSecondary,
            letterSpacing = 1.4.sp
          )
          Text(
            text = "Hello, ${uiState.userProfile.name}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = NaturalTextPrimary
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          IconButton(
            onClick = {
              newBudgetInput = uiState.dailyBudgetKg.toString()
              showBudgetDialog = true
            },
            modifier = Modifier.testTag("edit_budget_button")
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Edit Daily Carbon Target",
              tint = NaturalTextSecondary
            )
          }

          // Natural Avatar Pill (#DCE7D1 bg, #D7E8CD border, #386B1D text, clickable to profile)
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(NaturalSageBorder)
              .border(2.dp, NaturalSageLight, CircleShape)
              .clickable { onNavigateToProfile() }
              .testTag("avatar_profile_button"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = (uiState.userProfile.name.firstOrNull() ?: 'U').uppercase(),
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = NaturalPrimary
            )
          }
        }
      }
    }

    // 7-Day Horizontal Date Selector Pills
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        uiState.weeklyStats.forEach { stat ->
          DaySelectorPill(
            stat = stat,
            isSelected = stat.epochDay == uiState.selectedEpochDay,
            onSelect = { viewModel.selectDay(stat.epochDay) }
          )
        }
      }
    }

    // Hero Circular Carbon Gauge Card
    item {
      CarbonGaugeCard(
        emittedKg = uiState.selectedDayEmissionsKg,
        budgetKg = uiState.dailyBudgetKg,
        savedKg = uiState.selectedDaySavedKg
      )
    }

    // Personalized Carbon Footprint Justification Banner
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = NaturalSurface,
        border = BorderStroke(1.dp, NaturalSageBorder),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToProfile() }
          .testTag("dashboard_justification_banner")
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(NaturalSageLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.HealthAndSafety,
              contentDescription = null,
              tint = NaturalPrimary,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Personalized Footprint Justification",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = NaturalPrimary,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "Target: ${String.format("%.1f", uiState.carbonJustification.totalRecommendedBudgetKg)} kg/day (calibrated to ${uiState.userProfile.weightKg.toInt()}kg, ${uiState.userProfile.dietType})",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Medium,
              color = NaturalTextPrimary
            )
            if (uiState.carbonJustification.medicalAllowanceKg > 0) {
              Text(
                text = "+${String.format("%.2f", uiState.carbonJustification.medicalAllowanceKg)} kg health & accessibility accommodations justified",
                style = MaterialTheme.typography.labelSmall,
                color = NaturalTextSecondary,
                fontSize = 10.5.sp
              )
            }
          }
          Text(
            text = "Edit",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = NaturalPrimary
          )
        }
      }
    }

    // Quick Activity Log Shortcuts
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Quick Log (Common Activities)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          TextButton(
            onClick = onNavigateToLog,
            modifier = Modifier.testTag("open_full_logger_btn")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Full Log")
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          QuickLogButton(
            icon = Icons.Default.DirectionsBike,
            title = "5 km Walk/Bike",
            subtitle = "+0.96 kg saved",
            color = NaturalPrimary,
            onClick = {
              ActivityPresetsCatalog.getPresetById("trans_bike_walk")?.let {
                viewModel.logPresetActivity(it, 5.0)
              }
            }
          )
          QuickLogButton(
            icon = Icons.Default.NaturePeople,
            title = "Plant Meal",
            subtitle = "+5.75 kg saved",
            color = NaturalPrimary,
            onClick = {
              ActivityPresetsCatalog.getPresetById("food_vegan")?.let {
                viewModel.logPresetActivity(it, 1.0)
              }
            }
          )
          QuickLogButton(
            icon = Icons.Default.DirectionsBus,
            title = "15 km Bus",
            subtitle = "1.34 kg CO₂e",
            color = NaturalPrimary,
            onClick = {
              ActivityPresetsCatalog.getPresetById("trans_bus")?.let {
                viewModel.logPresetActivity(it, 15.0)
              }
            }
          )
          QuickLogButton(
            icon = Icons.Default.LocalLaundryService,
            title = "Cold Laundry",
            subtitle = "+0.40 kg saved",
            color = NaturalPrimary,
            onClick = {
              ActivityPresetsCatalog.getPresetById("energy_laundry_cold")?.let {
                viewModel.logPresetActivity(it, 1.0)
              }
            }
          )
        }
      }
    }

    // Category Breakdown Section
    item {
      CategoryBreakdownSection(
        categoryTotals = uiState.categoryBreakdown,
        onCategoryClick = { /* Could filter */ }
      )
    }

    // Dynamic Daily Insight & Actionable Tip (Dark Natural Card per design spec)
    uiState.topRecommendedTip?.let { topTip ->
      val isPledged = topTip.id in uiState.pledgedTipIds
      item {
        Card(
          shape = RoundedCornerShape(32.dp),
          colors = CardDefaults.cardColors(containerColor = NaturalDarkContainer),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_insight_card")
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp)
          ) {
            Column(
              modifier = Modifier.fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              // Daily Insight tag with sprout dot
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(NaturalSproutAccent)
                )
                Text(
                  text = "DAILY INSIGHT",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.5.sp,
                  color = Color(0xFFFDFDF6).copy(alpha = 0.7f)
                )
              }

              Text(
                text = topTip.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFDFDF6),
                lineHeight = 26.sp
              )

              Text(
                text = topTip.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFDFDF6).copy(alpha = 0.65f)
              )

              Spacer(modifier = Modifier.height(4.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Button(
                  onClick = { viewModel.togglePledgedTip(topTip.id) },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPledged) NaturalSageLight else NaturalSproutAccent,
                    contentColor = NaturalOnSprout
                  ),
                  shape = RoundedCornerShape(16.dp),
                  contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                  modifier = Modifier.testTag("pledge_habit_btn")
                ) {
                  if (isPledged) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pledged Habit", fontWeight = FontWeight.Bold)
                  } else {
                    Text("Adopt This Habit", fontWeight = FontWeight.Bold)
                  }
                }

                TextButton(
                  onClick = onNavigateToTips,
                  modifier = Modifier.testTag("view_all_tips_btn")
                ) {
                  Text(
                    text = "See All",
                    color = Color(0xFFFDFDF6).copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelMedium
                  )
                }
              }
            }
          }
        }
      }
    }

    // Today's Activity Feed
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (uiState.selectedEpochDay == uiState.currentEpochDay) "Today's Activities" else "Activities for Selected Day",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${uiState.selectedDayActivities.size} logged",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (uiState.selectedDayActivities.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Eco,
              contentDescription = null,
              tint = NaturalPrimary,
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "No activities logged yet for this day",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedButton(
              onClick = onNavigateToLog,
              modifier = Modifier.testTag("empty_state_log_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Log Activity")
            }
          }
        }
      }
    } else {
      items(
        items = uiState.selectedDayActivities,
        key = { it.id }
      ) { activity ->
        ActivityItemRow(
          activity = activity,
          onDelete = { viewModel.deleteActivity(activity.id) }
        )
      }
    }
  }
}

@Composable
fun DaySelectorPill(
  stat: DailyCarbonStat,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
  val bgColor = if (isSelected) NaturalSageLight else NaturalSurface
  val textColor = if (isSelected) NaturalPrimary else NaturalTextPrimary

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = bgColor,
    border = BorderStroke(1.dp, if (isSelected) NaturalPrimary else NaturalSageBorder),
    modifier = Modifier
      .clickable(onClick = onSelect)
      .testTag("day_pill_${stat.epochDay}")
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stat.dayLabel,
        style = MaterialTheme.typography.labelSmall,
        color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = stat.dayShortDate,
        style = MaterialTheme.typography.bodySmall,
        color = textColor,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "${String.format("%.1f", stat.totalEmittedKg)}kg",
        style = MaterialTheme.typography.labelSmall,
        color = if (isSelected) NaturalPrimary else NaturalTextSecondary,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun QuickLogButton(
  icon: ImageVector,
  title: String,
  subtitle: String,
  color: Color,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NaturalSurface),
    border = BorderStroke(1.dp, NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = Modifier
      .width(140.dp)
      .clickable(onClick = onClick)
      .testTag("quick_log_${title.lowercase().replace(" ", "_")}")
  ) {
    Column(
      modifier = Modifier.padding(14.dp)
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
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = NaturalTextPrimary,
        maxLines = 1
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun ActivityItemRow(
  activity: CarbonActivityEntity,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val category = try {
    ActivityCategory.valueOf(activity.category)
  } catch (e: Exception) {
    ActivityCategory.TRANSPORT
  }

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NaturalSurface),
    border = BorderStroke(1.dp, NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("activity_row_${activity.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // White circular icon badge for category
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Color.White)
          .border(1.dp, NaturalSageBorder.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(category.color)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = activity.title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold,
          color = NaturalTextPrimary
        )
        if (activity.note.isNotBlank()) {
          Text(
            text = activity.note,
            style = MaterialTheme.typography.bodySmall,
            color = NaturalTextSecondary
          )
        }
      }

      // Emissions / Saved badge
      Column(horizontalAlignment = Alignment.End) {
        if (activity.co2EmittedKg > 0) {
          Text(
            text = "${String.format("%.2f", activity.co2EmittedKg)} kg",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = NaturalTextPrimary
          )
        }
        if (activity.co2SavedKg > 0) {
          Text(
            text = "-${String.format("%.2f", activity.co2SavedKg)} kg saved",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = NaturalPrimary
          )
        }
      }

      IconButton(
        onClick = onDelete,
        modifier = Modifier.size(36.dp).testTag("delete_activity_${activity.id}")
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "Delete activity",
          tint = NaturalTextSecondary.copy(alpha = 0.7f),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
