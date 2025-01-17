package com.ai.dietplanner.util

import com.ai.dietplanner.data.*

object DietPlanCalculator {
    fun calculateDietPlan(userData: UserData): DietPlan {
        // Calculate BMR using Harris-Benedict equation
        val bmr = calculateBMR(userData)
        
        // Calculate daily calorie needs
        val dailyCalories = calculateDailyCalories(bmr, userData)
        
        // Calculate macronutrient distribution
        val macros = calculateMacronutrients(dailyCalories, userData)
        
        return DietPlan(
            macronutrients = macros,
            calories = dailyCalories,
            recommendations = generateRecommendations(userData),
            mealPlan = generateMealPlan(dailyCalories, macros)
        )
    }

    private fun calculateBMR(userData: UserData): Int {
        val heightCm = userData.getHeightInCm()
        return if (userData.gender == Gender.MALE) {
            (88.362 + (13.397 * userData.currentWeight) + (4.799 * heightCm) - (5.677 * userData.age)).toInt()
        } else {
            (447.593 + (9.247 * userData.currentWeight) + (3.098 * heightCm) - (4.330 * userData.age)).toInt()
        }
    }

    private fun calculateDailyCalories(bmr: Int, userData: UserData): Int {
        val activityMultiplier = when (userData.activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2f
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375f
            ActivityLevel.MODERATELY_ACTIVE -> 1.55f
            ActivityLevel.VERY_ACTIVE -> 1.725f
            ActivityLevel.EXTRA_ACTIVE -> 1.9f
        }

        val maintenanceCalories = (bmr * activityMultiplier).toInt()
        
        return when {
            userData.currentWeight > userData.targetWeight -> (maintenanceCalories * 0.85).toInt()
            userData.currentWeight < userData.targetWeight -> (maintenanceCalories * 1.1).toInt()
            else -> maintenanceCalories
        }
    }

    private fun calculateMacronutrients(calories: Int, userData: UserData): Macronutrients {
        val proteinMultiplier = if (userData.currentWeight > userData.targetWeight) 2.0f else 2.2f
        val proteins = userData.currentWeight * proteinMultiplier
        val fats = (calories * 0.25 / 9).toFloat()
        val carbCalories = calories - (proteins * 4) - (fats * 9)
        val carbs = (carbCalories / 4).toFloat()

        return Macronutrients(
            proteins = proteins,
            carbs = carbs,
            fats = fats
        )
    }

    private fun generateRecommendations(userData: UserData): DietRecommendations {
        return DietRecommendations(
            recommendedFoods = getRecommendedFoods(),
            foodsToAvoid = getFoodsToAvoid(),
            dailyWaterIntake = userData.currentWeight * 0.033f,
            mealFrequency = 5
        )
    }

    private fun generateMealPlan(calories: Int, macros: Macronutrients): MealPlan {
        return MealPlan(
            breakfast = getBreakfastFoods(),
            morningSnack = getSnackFoods(),
            lunch = getLunchFoods(),
            eveningSnack = getSnackFoods(),
            dinner = getDinnerFoods()
        )
    }

    private fun getRecommendedFoods(): List<FoodItem> = listOf(
        FoodItem("Chicken Breast", 165, 31f, 0f, 3.6f, "100g", FoodCategory.PROTEIN),
        FoodItem("Salmon", 208, 22f, 0f, 13f, "100g", FoodCategory.PROTEIN),
        FoodItem("Eggs", 140, 12f, 0f, 10f, "2 eggs", FoodCategory.PROTEIN),
        FoodItem("Quinoa", 120, 4.4f, 21.3f, 1.9f, "100g", FoodCategory.CARBS),
        FoodItem("Sweet Potato", 86, 1.6f, 20f, 0.1f, "100g", FoodCategory.CARBS),
        FoodItem("Brown Rice", 110, 2.5f, 23f, 0.9f, "100g", FoodCategory.CARBS)
    )

    private fun getFoodsToAvoid(): List<FoodItem> = listOf(
        FoodItem("Processed Sugar", 387, 0f, 100f, 0f, "100g", FoodCategory.CARBS),
        FoodItem("Fried Foods", 312, 14f, 35f, 15f, "100g", FoodCategory.FATS),
        FoodItem("Soda", 150, 0f, 39f, 0f, "355ml", FoodCategory.CARBS)
    )

    private fun getBreakfastFoods(): List<FoodItem> = listOf(
        FoodItem("Oatmeal with Berries", 150, 6f, 27f, 3f, "1 cup", FoodCategory.CARBS),
        FoodItem("Eggs", 140, 12f, 0f, 10f, "2 whole eggs", FoodCategory.PROTEIN)
    )

    private fun getSnackFoods(): List<FoodItem> = listOf(
        FoodItem("Almonds", 160, 6f, 6f, 14f, "28g", FoodCategory.FATS),
        FoodItem("Apple", 95, 0.5f, 25f, 0.3f, "1 medium", FoodCategory.FRUITS)
    )

    private fun getLunchFoods(): List<FoodItem> = listOf(
        FoodItem("Grilled Chicken Breast", 165, 31f, 0f, 3.6f, "100g", FoodCategory.PROTEIN),
        FoodItem("Brown Rice", 110, 2.5f, 23f, 0.9f, "100g", FoodCategory.CARBS)
    )

    private fun getDinnerFoods(): List<FoodItem> = listOf(
        FoodItem("Salmon", 208, 22f, 0f, 13f, "100g", FoodCategory.PROTEIN),
        FoodItem("Quinoa", 120, 4.4f, 21.3f, 1.9f, "100g", FoodCategory.CARBS)
    )
}

enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTRA_ACTIVE
}

enum class Gender {
    MALE,
    FEMALE
} 