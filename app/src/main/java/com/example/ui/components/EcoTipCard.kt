package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EcoTip
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary

@Composable
fun EcoTipCard(
  tip: EcoTip,
  isPledged: Boolean,
  onTogglePledge: (String) -> Unit,
  modifier: Modifier = Modifier,
  initiallyExpanded: Boolean = false
) {
  var expanded by remember { mutableStateOf(initiallyExpanded) }

  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isPledged) NaturalSageLight else NaturalSurface
    ),
    border = BorderStroke(1.dp, if (isPledged) NaturalPrimary else NaturalSageBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("eco_tip_card_${tip.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Top header tags: Category + Impact Level + Annual Savings
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Category Tag
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = tip.category.lightColor
          ) {
            Text(
              text = tip.category.displayName,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.SemiBold,
              color = tip.category.color,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          // Impact Level Tag
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(tip.impactLevel.badgeColorHex).copy(alpha = 0.12f)
          ) {
            Text(
              text = tip.impactLevel.label,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.SemiBold,
              color = Color(tip.impactLevel.badgeColorHex),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        // Annual CO2 reduction
        Surface(
          shape = CircleShape,
          color = NaturalPrimary
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Eco,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "-${tip.annualCo2SavingsKg.toInt()} kg/yr",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title & Expand chevron
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Text(
          text = tip.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NaturalTextPrimary,
          modifier = Modifier.weight(1f)
        )
        IconButton(
          onClick = { expanded = !expanded },
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (expanded) "Collapse" else "Expand details",
            tint = NaturalTextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = tip.summary,
        style = MaterialTheme.typography.bodyMedium,
        color = NaturalTextSecondary,
        lineHeight = 20.sp
      )

      // Expandable Action Steps
      AnimatedVisibility(visible = expanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
        ) {
          Text(
            text = "Actionable Steps:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = NaturalTextPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))
          tip.actionSteps.forEachIndexed { index, step ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(18.dp)
                  .background(NaturalPrimary.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${index + 1}",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = NaturalPrimary,
                  fontSize = 10.sp
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = step,
                style = MaterialTheme.typography.bodySmall,
                color = NaturalTextPrimary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Bottom Action: Pledge / Active Habit button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Difficulty: ${tip.difficulty}",
          style = MaterialTheme.typography.labelSmall,
          color = NaturalTextSecondary
        )

        if (isPledged) {
          Button(
            onClick = { onTogglePledge(tip.id) },
            colors = ButtonDefaults.buttonColors(
              containerColor = NaturalPrimary,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.testTag("pledge_button_${tip.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Active Habit",
              fontWeight = FontWeight.SemiBold
            )
          }
        } else {
          OutlinedButton(
            onClick = { onTogglePledge(tip.id) },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, NaturalSageBorder),
            modifier = Modifier.testTag("pledge_button_${tip.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Stars,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = NaturalPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Commit to Habit",
              fontWeight = FontWeight.SemiBold,
              color = NaturalPrimary
            )
          }
        }
      }
    }
  }
}
