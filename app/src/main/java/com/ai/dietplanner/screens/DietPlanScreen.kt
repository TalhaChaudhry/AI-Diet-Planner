package com.ai.dietplanner.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.ai.dietplanner.data.DietPlan
import com.ai.dietplanner.data.FoodItem
import com.ai.dietplanner.data.Macronutrients
import com.ai.dietplanner.util.DietPlanCalculator
import com.ai.dietplanner.data.UserData
import com.ai.dietplanner.data.DietRecommendations
import com.ai.dietplanner.data.MealPlan
import com.ai.dietplanner.util.ActivityLevel
import com.ai.dietplanner.util.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(
    onNavigateBack: () -> Unit
) {
    // Create sample user data for demonstration
    val sampleUserData = UserData(
        heightFeet = 5,
        currentWeight = 70f,
        targetWeight = 65f,
        age = 30,
        gender = Gender.MALE,
        activityLevel = ActivityLevel.MODERATELY_ACTIVE
    )

    // Calculate diet plan
    val dietPlan = DietPlanCalculator.calculateDietPlan(sampleUserData)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Diet Plan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MacronutrientsCard(dietPlan.macronutrients)
            DailyMealsCard(dietPlan.mealPlan)
            RecommendationsCard(dietPlan.recommendations)
        }
    }
}

@Composable
fun MacronutrientsCard(macros: Macronutrients) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recommended Macronutrients",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PieChart(
                macros = macros,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            MacronutrientLegend(macros)
        }
    }
}

@Composable
fun PieChart(
    macros: Macronutrients,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val total = macros.proteins + macros.carbs + macros.fats
        var startAngle = 0f

        // Draw proteins section
        drawArc(
            color = Color(0xFF2196F3),
            startAngle = startAngle,
            sweepAngle = 360f * (macros.proteins / total),
            useCenter = true
        )
        startAngle += 360f * (macros.proteins / total)

        // Draw carbs section
        drawArc(
            color = Color(0xFF4CAF50),
            startAngle = startAngle,
            sweepAngle = 360f * (macros.carbs / total),
            useCenter = true
        )
        startAngle += 360f * (macros.carbs / total)

        // Draw fats section
        drawArc(
            color = Color(0xFFFFC107),
            startAngle = startAngle,
            sweepAngle = 360f * (macros.fats / total),
            useCenter = true
        )
    }
}

@Composable
fun MacronutrientLegend(macros: Macronutrients) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LegendItem(color = Color(0xFF2196F3), text = "Proteins: ${macros.proteins}g")
        LegendItem(color = Color(0xFF4CAF50), text = "Carbs: ${macros.carbs}g")
        LegendItem(color = Color(0xFFFFC107), text = "Fats: ${macros.fats}g")
    }
}

@Composable
fun DailyMealsCard(mealPlan: MealPlan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Daily Meal Plan",
                style = MaterialTheme.typography.titleLarge
            )
            
            MealSection("Breakfast", mealPlan.breakfast)
            MealSection("Morning Snack", mealPlan.morningSnack)
            MealSection("Lunch", mealPlan.lunch)
            MealSection("Evening Snack", mealPlan.eveningSnack)
            MealSection("Dinner", mealPlan.dinner)
        }
    }
}

@Composable
fun MealSection(title: String, foods: List<FoodItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        foods.forEach { food ->
            Text("• ${food.name} (${food.servingSize})")
        }
    }
}

@Composable
fun RecommendationsCard(recommendations: DietRecommendations) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Daily Recommendations",
                style = MaterialTheme.typography.titleLarge
            )
            
            Text("Water Intake: ${String.format("%.1f", recommendations.dailyWaterIntake)} liters")
            Text("Meal Frequency: ${recommendations.mealFrequency} times per day")
            
            Text(
                text = "Recommended Foods",
                style = MaterialTheme.typography.titleMedium
            )
            recommendations.recommendedFoods.take(5).forEach { food ->
                Text("• ${food.name}")
            }
            
            Text(
                text = "Foods to Avoid",
                style = MaterialTheme.typography.titleMedium
            )
            recommendations.foodsToAvoid.take(5).forEach { food ->
                Text("• ${food.name}")
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )
        Text(text)
    }
} 