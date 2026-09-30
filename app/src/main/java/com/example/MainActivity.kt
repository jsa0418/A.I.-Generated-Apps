package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CarbonViewModel
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import com.example.ui.screens.EcoTipsScreen
import com.example.ui.screens.LogActivityScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.NaturalPrimary
import com.example.ui.theme.NaturalSageBorder
import com.example.ui.theme.NaturalSageLight
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.MyApplicationTheme

enum class NavigationDestination(
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  DASHBOARD(
    title = "Tracker",
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home,
    testTag = "nav_tracker"
  ),
  LOG(
    title = "Log Activity",
    selectedIcon = Icons.Filled.AddCircle,
    unselectedIcon = Icons.Outlined.AddCircleOutline,
    testTag = "nav_log"
  ),
  ANALYTICS(
    title = "Analytics",
    selectedIcon = Icons.Filled.BarChart,
    unselectedIcon = Icons.Outlined.BarChart,
    testTag = "nav_analytics"
  ),
  TIPS(
    title = "Eco Tips",
    selectedIcon = Icons.Filled.Lightbulb,
    unselectedIcon = Icons.Outlined.Lightbulb,
    testTag = "nav_tips"
  ),
  PROFILE(
    title = "Profile",
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person,
    testTag = "nav_profile"
  )
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CarbonApp()
      }
    }
  }
}

@Composable
fun CarbonApp() {
  val viewModel: CarbonViewModel = viewModel(
    factory = CarbonViewModel.provideFactory(
      androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
    )
  )
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar(
        containerColor = NaturalSurface,
        tonalElevation = 0.dp,
        modifier = Modifier
          .drawBehind {
            // Natural border-t (#DCE7D1)
            drawLine(
              color = NaturalSageBorder,
              start = Offset(0f, 0f),
              end = Offset(size.width, 0f),
              strokeWidth = 1.dp.toPx()
            )
          }
          .testTag("main_bottom_nav")
      ) {
        NavigationDestination.values().forEachIndexed { index, destination ->
          val isSelected = selectedIndex == index
          NavigationBarItem(
            selected = isSelected,
            onClick = { selectedIndex = index },
            icon = {
              Icon(
                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                contentDescription = destination.title
              )
            },
            label = {
              Text(
                text = destination.title,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = NaturalPrimary,
              selectedTextColor = NaturalPrimary,
              indicatorColor = NaturalSageLight,
              unselectedIconColor = NaturalTextSecondary.copy(alpha = 0.7f),
              unselectedTextColor = NaturalTextSecondary.copy(alpha = 0.7f)
            ),
            modifier = Modifier.testTag(destination.testTag)
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (selectedIndex) {
        0 -> DashboardScreen(
          uiState = uiState,
          viewModel = viewModel,
          onNavigateToLog = { selectedIndex = 1 },
          onNavigateToTips = { selectedIndex = 3 },
          onNavigateToProfile = { selectedIndex = 4 }
        )
        1 -> LogActivityScreen(
          viewModel = viewModel,
          onActivityLogged = { selectedIndex = 0 }
        )
        2 -> AnalyticsScreen(
          uiState = uiState,
          viewModel = viewModel
        )
        3 -> EcoTipsScreen(
          uiState = uiState,
          viewModel = viewModel
        )
        4 -> ProfileScreen(
          uiState = uiState,
          viewModel = viewModel
        )
      }
    }
  }
}
