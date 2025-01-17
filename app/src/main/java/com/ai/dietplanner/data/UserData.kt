package com.ai.dietplanner.data

import android.os.Parcelable
import com.ai.dietplanner.util.ActivityLevel
import com.ai.dietplanner.util.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val heightFeet: Int,
    val currentWeight: Float,
    val targetWeight: Float,
    val age: Int,
    val gender: Gender,
    val activityLevel: ActivityLevel
) : Parcelable {
    fun getHeightInCm(): Float = (heightFeet * 30.48).toFloat()
} 