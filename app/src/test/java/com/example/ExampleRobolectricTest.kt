package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Carbon Footprint Tracker", appName)
  }

  @Test
  fun `user profile justification calculates metabolic and medical allowances`() {
    val profile = com.example.data.model.UserProfileEntity(
      name = "Taylor",
      age = 30,
      heightCm = 175.0,
      weightKg = 70.0,
      medicalEquipment = true,
      climateMedicalNeed = true,
      mobilityNeeds = false,
      dietType = "Vegan"
    )

    val justification = profile.calculateJustification()
    // Vegan baseline scaled with BMR (~1648 kcal) -> ~1.48 kg CO₂e
    assert(justification.metabolicDietKg in 1.0..2.0)
    // Medical equipment (0.75) + Climate control (1.25) = 2.00 kg
    assertEquals(2.00, justification.medicalAllowanceKg, 0.01)
    assert(justification.totalRecommendedBudgetKg > 5.0)
    assert(justification.justificationPoints.isNotEmpty())
  }
}
