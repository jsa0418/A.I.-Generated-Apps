package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityCategory
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTrack

@Composable
fun CategoryBreakdownSection(
  categoryTotals: Map<ActivityCategory, Double>,
  onCategoryClick: (ActivityCategory) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val totalEmissions = categoryTotals.values.sum().coerceAtLeast(0.001)

  Column(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "ACTIVE IMPACT",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = NaturalTextSecondary,
        letterSpacing = 1.5.sp
      )
      Text(
        text = "${String.format("%.1f", totalEmissions)} kg total",
        style = MaterialTheme.typography.bodySmall,
        color = NaturalTextSecondary
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Proportional Segmented Progress Bar with Natural Track
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(NaturalTrack)
        .testTag("breakdown_segmented_bar")
    ) {
      ActivityCategory.values().forEach { cat ->
        val catEmissions = categoryTotals[cat] ?: 0.0
        val weight = (catEmissions / totalEmissions).toFloat().coerceIn(0f, 1f)
        if (weight > 0.01f) {
          Box(
            modifier = Modifier
              .weight(weight)
              .height(10.dp)
              .background(cat.color)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Category Grid of 4 cards
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      ActivityCategory.values().take(2).forEach { cat ->
        CategoryStatCard(
          category = cat,
          emittedKg = categoryTotals[cat] ?: 0.0,
          totalEmittedKg = totalEmissions,
          onClick = { onCategoryClick(cat) },
          modifier = Modifier.weight(1f)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      ActivityCategory.values().drop(2).forEach { cat ->
        CategoryStatCard(
          category = cat,
          emittedKg = categoryTotals[cat] ?: 0.0,
          totalEmittedKg = totalEmissions,
          onClick = { onCategoryClick(cat) },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
fun CategoryStatCard(
  category: ActivityCategory,
  emittedKg: Double,
  totalEmittedKg: Double,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val percent = if (totalEmittedKg > 0.001) ((emittedKg / totalEmittedKg) * 100).toInt() else 0
  val icon = when (category) {
    ActivityCategory.TRANSPORT -> Icons.Default.DirectionsCar
    ActivityCategory.FOOD -> Icons.Default.Fastfood
    ActivityCategory.ENERGY -> Icons.Default.Power
    ActivityCategory.SHOPPING -> Icons.Default.ShoppingBag
  }

  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = NaturalSurface
    ),
    border = BorderStroke(1.dp, NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("category_card_${category.name.lowercase()}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // White circular icon badge (per Natural Tones design)
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
          contentDescription = category.displayName,
          tint = category.color,
          modifier = Modifier.size(20.dp)
        )
      }

      Column {
        Text(
          text = category.displayName,
          style = MaterialTheme.typography.titleSmall,
          color = NaturalTextPrimary,
          fontWeight = FontWeight.SemiBold
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "${String.format("%.1f", emittedKg)} kg",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = NaturalTextSecondary
          )
          Text(
            text = "• $percent%",
            style = MaterialTheme.typography.labelSmall,
            color = category.color,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}
