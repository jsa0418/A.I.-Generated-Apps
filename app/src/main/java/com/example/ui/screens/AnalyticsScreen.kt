package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityCategory
import com.example.data.model.CarbonActivityEntity
import com.example.ui.CarbonUiState
import com.example.ui.CarbonViewModel
import com.example.ui.DailyCarbonStat
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.FoodOrange
import com.example.ui.theme.NaturalDarkContainer
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

@Composable
fun AnalyticsScreen(
  uiState: CarbonUiState,
  viewModel: CarbonViewModel,
  modifier: Modifier = Modifier
) {
  var selectedCategoryFilter by remember { mutableStateOf<ActivityCategory?>(null) }

  val weeklyEmissions = uiState.weeklyStats.sumOf { it.totalEmittedKg }
  val weeklySaved = uiState.weeklyStats.sumOf { it.totalSavedKg }
  val dailyAverage = if (uiState.weeklyStats.isNotEmpty()) weeklyEmissions / uiState.weeklyStats.size else 0.0

  val filteredActivities = remember(uiState.allActivities, selectedCategoryFilter) {
    if (selectedCategoryFilter == null) {
      uiState.allActivities
    } else {
      uiState.allActivities.filter { it.category == selectedCategoryFilter!!.name }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("analytics_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Analytics & History",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Review your 7-day trends and environmental footprint",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Weekly Summary Metric Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        SummaryStatCard(
          title = "7-Day Total",
          value = "${String.format("%.1f", weeklyEmissions)} kg",
          subtitle = "Avg ${String.format("%.1f", dailyAverage)} kg/d",
          color = NaturalPrimary,
          modifier = Modifier.weight(1f)
        )
        SummaryStatCard(
          title = "CO₂e Prevented",
          value = "+${String.format("%.1f", weeklySaved)} kg",
          subtitle = "Green choices",
          color = NaturalPrimary,
          modifier = Modifier.weight(1f)
        )
        SummaryStatCard(
          title = "Justified Baseline",
          value = "${String.format("%.1f", uiState.carbonJustification.totalRecommendedBudgetKg)} kg",
          subtitle = "${uiState.userProfile.name}'s profile",
          color = NaturalPrimary,
          modifier = Modifier.weight(1.1f)
        )
      }
    }

    // Weekly Bar Chart Card
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalSurface),
        border = BorderStroke(1.dp, NaturalSageBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().testTag("weekly_trend_chart_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ShowChart,
                contentDescription = null,
                tint = NaturalPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "7-Day Carbon Emissions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
              )
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = NaturalSageLight,
              border = BorderStroke(1.dp, NaturalSageBorder)
            ) {
              Text(
                text = "Target: ${uiState.dailyBudgetKg} kg",
                style = MaterialTheme.typography.labelSmall,
                color = NaturalPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Custom 7-Day Bar Chart Canvas
          WeeklyBarChart(
            weeklyStats = uiState.weeklyStats,
            dailyBudget = uiState.dailyBudgetKg,
            onSelectDay = { viewModel.selectDay(it) },
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Chart legend
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(modifier = Modifier.size(10.dp).background(NaturalPrimary, CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Under Budget", style = MaterialTheme.typography.labelSmall, color = NaturalTextSecondary)
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(10.dp).background(FoodOrange, CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Over Budget", style = MaterialTheme.typography.labelSmall, color = NaturalTextSecondary)
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(16.dp, 2.dp).background(Color.Gray))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Daily Target Line", style = MaterialTheme.typography.labelSmall, color = NaturalTextSecondary)
          }
        }
      }
    }

    // Category Distribution Section
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalSurface),
        border = BorderStroke(1.dp, NaturalSageBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Text(
            text = "Lifetime Category Distribution",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(12.dp))

          val totalAll = uiState.allActivities.sumOf { it.co2EmittedKg }.coerceAtLeast(0.001)

          ActivityCategory.values().forEach { category ->
            val catSum = uiState.allActivities.filter { it.category == category.name }.sumOf { it.co2EmittedKg }
            val ratio = (catSum / totalAll).toFloat()

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = category.displayName,
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  text = "${String.format("%.1f", catSum)} kg (${(ratio * 100).toInt()}%)",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  color = category.color
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth(ratio.coerceIn(0f, 1f))
                    .height(8.dp)
                    .background(category.color)
                )
              }
            }
          }
        }
      }
    }

    // Activity Log History Header with Filter Chips
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Activity Log History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${filteredActivities.size} records",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FilterChip(
            selected = selectedCategoryFilter == null,
            onClick = { selectedCategoryFilter = null },
            label = { Text("All") },
            modifier = Modifier.testTag("filter_all")
          )
          ActivityCategory.values().forEach { cat ->
            FilterChip(
              selected = selectedCategoryFilter == cat,
              onClick = { selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat },
              label = { Text(cat.displayName) },
              modifier = Modifier.testTag("filter_${cat.name.lowercase()}")
            )
          }
        }
      }
    }

    // Items list
    if (filteredActivities.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No activities matching this filter.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(
        items = filteredActivities,
        key = { it.id }
      ) { activity ->
        ActivityHistoryCard(
          activity = activity,
          onDelete = { viewModel.deleteActivity(activity.id) }
        )
      }
    }
  }
}

@Composable
fun SummaryStatCard(
  title: String,
  value: String,
  subtitle: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NaturalSurface),
    border = BorderStroke(1.dp, NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = NaturalTextSecondary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = color
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = NaturalTextSecondary
      )
    }
  }
}

@Composable
fun WeeklyBarChart(
  weeklyStats: List<DailyCarbonStat>,
  dailyBudget: Double,
  onSelectDay: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val maxVal = maxOf(
    (weeklyStats.maxOfOrNull { it.totalEmittedKg } ?: 10.0),
    dailyBudget
  ) * 1.2

  Box(modifier = modifier) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val canvasWidth = size.width
      val canvasHeight = size.height
      val bottomLabelSpace = 24.dp.toPx()
      val chartHeight = canvasHeight - bottomLabelSpace
      val barCount = weeklyStats.size
      if (barCount == 0) return@Canvas

      val spacing = canvasWidth / barCount
      val barWidth = spacing * 0.55f

      // Budget threshold line
      val budgetY = chartHeight * (1f - (dailyBudget / maxVal).toFloat())
      drawLine(
        color = Color.Gray.copy(alpha = 0.6f),
        start = Offset(0f, budgetY),
        end = Offset(canvasWidth, budgetY),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
      )

      // Draw Bars
      weeklyStats.forEachIndexed { index, stat ->
        val xCenter = spacing * index + (spacing / 2)
        val barHeight = chartHeight * ((stat.totalEmittedKg / maxVal).toFloat().coerceIn(0.04f, 1f))
        val barTop = chartHeight - barHeight

        val barColor = when {
          stat.isSelected -> NaturalDarkContainer
          stat.totalEmittedKg <= dailyBudget -> NaturalPrimary
          else -> FoodOrange
        }

        drawRoundRect(
          color = barColor,
          topLeft = Offset(xCenter - (barWidth / 2), barTop),
          size = Size(barWidth, barHeight),
          cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
      }
    }

    // Overlay clickable row and day labels
    Row(
      modifier = Modifier.fillMaxSize(),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.Bottom
    ) {
      weeklyStats.forEach { stat ->
        Column(
          modifier = Modifier
            .weight(1f)
            .clickable { onSelectDay(stat.epochDay) },
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = stat.dayLabel,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (stat.isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (stat.isSelected) NaturalPrimary else NaturalTextSecondary,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

@Composable
fun ActivityHistoryCard(
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
      .testTag("history_card_${activity.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(category.lightColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = category.displayName.take(1),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = category.color
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = activity.title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        if (activity.note.isNotBlank()) {
          Text(
            text = activity.note,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Text(
          text = category.displayName,
          style = MaterialTheme.typography.labelSmall,
          color = category.color,
          fontWeight = FontWeight.SemiBold
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        if (activity.co2EmittedKg > 0) {
          Text(
            text = "${String.format("%.2f", activity.co2EmittedKg)} kg",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
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
        modifier = Modifier.size(36.dp).testTag("delete_history_${activity.id}")
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "Delete",
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
