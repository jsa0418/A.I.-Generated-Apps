package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityCategory
import com.example.data.model.EcoTip
import com.example.data.model.EcoTipsCatalog
import com.example.data.model.TipImpactLevel
import com.example.ui.CarbonUiState
import com.example.ui.CarbonViewModel
import com.example.ui.components.EcoTipCard
import com.example.ui.theme.NaturalDarkContainer
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSproutAccent
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

@Composable
fun EcoTipsScreen(
  uiState: CarbonUiState,
  viewModel: CarbonViewModel,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf("ALL") }

  // Total annual carbon saved from active pledges
  val totalPledgedAnnualSavings = remember(uiState.pledgedTipIds) {
    uiState.pledgedTipIds.mapNotNull { id ->
      EcoTipsCatalog.getTipById(id)?.annualCo2SavingsKg
    }.sum()
  }

  val filteredTips = remember(selectedFilter, uiState.pledgedTipIds) {
    when (selectedFilter) {
      "PLEDGED" -> EcoTipsCatalog.tips.filter { it.id in uiState.pledgedTipIds }
      "HIGH_IMPACT" -> EcoTipsCatalog.tips.filter { it.impactLevel == TipImpactLevel.HIGH_IMPACT }
      "QUICK_WIN" -> EcoTipsCatalog.tips.filter { it.impactLevel == TipImpactLevel.QUICK_WIN }
      "TRANSPORT" -> EcoTipsCatalog.getTipsForCategory(ActivityCategory.TRANSPORT)
      "FOOD" -> EcoTipsCatalog.getTipsForCategory(ActivityCategory.FOOD)
      "ENERGY" -> EcoTipsCatalog.getTipsForCategory(ActivityCategory.ENERGY)
      "SHOPPING" -> EcoTipsCatalog.getTipsForCategory(ActivityCategory.SHOPPING)
      else -> EcoTipsCatalog.tips
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("eco_tips_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Actionable Eco Tips",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = NaturalTextPrimary
        )
        Text(
          text = "Proven steps to cut your footprint and save energy",
          style = MaterialTheme.typography.bodyMedium,
          color = NaturalTextSecondary
        )
      }
    }

    // Impact Hero Card: Pledged Habits & Total Projected Savings (Dark Natural Container)
    item {
      Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalDarkContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().testTag("pledged_summary_banner")
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(NaturalSproutAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "ACTIVE COMMITMENTS",
                  style = MaterialTheme.typography.labelSmall,
                  color = NaturalSproutAccent,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.2.sp
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "-${totalPledgedAnnualSavings.toInt()} kg CO₂e / yr",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDFDF6)
              )
              Text(
                text = "${uiState.pledgedTipIds.size} sustainable habits in progress",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFDFDF6).copy(alpha = 0.7f)
              )
            }

            Box(
              modifier = Modifier
                .size(54.dp)
                .background(NaturalSproutAccent.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = null,
                tint = NaturalSproutAccent,
                modifier = Modifier.size(30.dp)
              )
            }
          }
        }
      }
    }

    // Filter Chips
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        FilterChip(
          selected = selectedFilter == "ALL",
          onClick = { selectedFilter = "ALL" },
          label = { Text("All (${EcoTipsCatalog.tips.size})") },
          modifier = Modifier.testTag("tips_filter_all")
        )
        FilterChip(
          selected = selectedFilter == "PLEDGED",
          onClick = { selectedFilter = "PLEDGED" },
          label = { Text("My Habits (${uiState.pledgedTipIds.size})") },
          leadingIcon = {
            Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(14.dp))
          },
          modifier = Modifier.testTag("tips_filter_pledged")
        )
        FilterChip(
          selected = selectedFilter == "HIGH_IMPACT",
          onClick = { selectedFilter = "HIGH_IMPACT" },
          label = { Text("High Impact") },
          modifier = Modifier.testTag("tips_filter_high_impact")
        )
        FilterChip(
          selected = selectedFilter == "QUICK_WIN",
          onClick = { selectedFilter = "QUICK_WIN" },
          label = { Text("Quick Wins") },
          modifier = Modifier.testTag("tips_filter_quick_win")
        )
        FilterChip(
          selected = selectedFilter == "FOOD",
          onClick = { selectedFilter = "FOOD" },
          label = { Text("Food") },
          modifier = Modifier.testTag("tips_filter_food")
        )
        FilterChip(
          selected = selectedFilter == "TRANSPORT",
          onClick = { selectedFilter = "TRANSPORT" },
          label = { Text("Transport") },
          modifier = Modifier.testTag("tips_filter_transport")
        )
        FilterChip(
          selected = selectedFilter == "ENERGY",
          onClick = { selectedFilter = "ENERGY" },
          label = { Text("Energy") },
          modifier = Modifier.testTag("tips_filter_energy")
        )
      }
    }

    // Tips List
    if (filteredTips.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = NaturalSurface),
          border = BorderStroke(1.dp, NaturalSageBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Stars,
              contentDescription = null,
              tint = NaturalPrimary,
              modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "No pledged habits yet!",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = NaturalTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Browse the tips library and tap 'Commit to Habit' on actions you'd like to take.",
              style = MaterialTheme.typography.bodyMedium,
              color = NaturalTextSecondary
            )
          }
        }
      }
    } else {
      items(
        items = filteredTips,
        key = { it.id }
      ) { tip ->
        EcoTipCard(
          tip = tip,
          isPledged = tip.id in uiState.pledgedTipIds,
          onTogglePledge = { viewModel.togglePledgedTip(it) },
          initiallyExpanded = tip.id in uiState.pledgedTipIds
        )
      }
    }
  }
}
