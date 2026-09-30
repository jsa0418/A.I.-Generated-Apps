package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NaturalDarkContainer
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTrack
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.FoodOrange

@Composable
fun CarbonGaugeCard(
  emittedKg: Double,
  budgetKg: Double,
  savedKg: Double,
  modifier: Modifier = Modifier
) {
  val ratio = if (budgetKg > 0) (emittedKg / budgetKg).toFloat().coerceIn(0f, 1.5f) else 0f
  val progressArc by animateFloatAsState(
    targetValue = (ratio.coerceAtMost(1f) * 240f),
    animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
    label = "gauge_arc"
  )

  // Status text and colors in natural tones
  val statusColor = when {
    emittedKg <= budgetKg * 0.75 -> NaturalPrimary
    emittedKg <= budgetKg -> EnergyAmber
    else -> FoodOrange
  }

  val statusText = when {
    emittedKg <= budgetKg * 0.70 -> "15% less than average"
    emittedKg <= budgetKg -> "On Track with Daily Target"
    else -> "Over Target Budget"
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("carbon_gauge_card"),
    shape = RoundedCornerShape(32.dp),
    colors = CardDefaults.cardColors(
      containerColor = NaturalSageLight
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Circular Speedometer / Arch Gauge styled per Natural Tones
      Box(
        modifier = Modifier.size(190.dp, 160.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(175.dp)) {
          val strokeWidth = 14.dp.toPx()
          val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
          val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

          // Natural background track (#C1D1B8)
          drawArc(
            color = NaturalTrack,
            startAngle = 150f,
            sweepAngle = 240f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
          )

          // Active Value Arc in Natural Primary Leaf Green (#386B1D)
          if (progressArc > 0f) {
            val strokeColor = if (emittedKg > budgetKg) FoodOrange else NaturalPrimary
            drawArc(
              color = strokeColor,
              startAngle = 150f,
              sweepAngle = progressArc,
              useCenter = false,
              topLeft = topLeft,
              size = arcSize,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
          }
        }

        // Center Value Display
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 8.dp)
        ) {
          Text(
            text = String.format("%.1f", emittedKg),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = NaturalTextPrimary,
            letterSpacing = (-1).sp
          )
          Text(
            text = "KG CO₂E",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = NaturalTextSecondary,
            letterSpacing = 1.2.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Natural Tones comparison pill: "15% less than yesterday" / status
      Surface(
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.5f),
        modifier = Modifier.testTag("status_badge")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (emittedKg <= budgetKg) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = statusText,
            color = statusColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
          )
        }
      }

      if (savedKg > 0) {
        Spacer(modifier = Modifier.height(10.dp))

        // Eco Savings Badge (pill in white/60% with #386B1D text)
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color.White.copy(alpha = 0.55f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .background(NaturalPrimary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = "Carbon Saved",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "+${String.format("%.2f", savedKg)} kg CO₂e Saved via Green Choices",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = NaturalPrimary
            )
          }
        }
      }
    }
  }
}
