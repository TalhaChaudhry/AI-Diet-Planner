package com.ai.dietplanner.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DietPlan(
    val macronutrients: Macronutrients,
    val calories: Int,
    val recommendations: DietRecommendations,
    val mealPlan: MealPlan
) : Parcelable

@Parcelize
data class Macronutrients(
    val proteins: Float, // in grams
    val carbs: Float,   // in grams
    val fats: Float     // in grams
) : Parcelable

@Parcelize
data class DietRecommendations(
    val recommendedFoods: List<FoodItem>,
    val foodsToAvoid: List<FoodItem>,
    val dailyWaterIntake: Float, // in liters
    val mealFrequency: Int
) : Parcelable

@Parcelize
data class MealPlan(
    val breakfast: List<FoodItem>,
    val morningSnack: List<FoodItem>,
    val lunch: List<FoodItem>,
    val eveningSnack: List<FoodItem>,
    val dinner: List<FoodItem>
) : Parcelable

@Parcelize
data class FoodItem(
    val name: String,
    val calories: Int,
    val proteins: Float,
    val carbs: Float,
    val fats: Float,
    val servingSize: String,
    val category: FoodCategory
) : Parcelable

enum class FoodCategory {
    PROTEIN,
    CARBS,
    FATS,
    VEGETABLES,
    FRUITS,
    DAIRY
} 