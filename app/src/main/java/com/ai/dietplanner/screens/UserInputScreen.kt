package com.ai.dietplanner.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ai.dietplanner.data.UserData
import com.ai.dietplanner.util.ActivityLevel
import com.ai.dietplanner.util.Gender
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputScreen(
    onNavigateToDietPlan: (UserData) -> Unit
) {
    var heightFeet by remember { mutableStateOf("") }
    var currentWeight by remember { mutableStateOf("") }
    var targetWeight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var activityLevel by remember { mutableStateOf(ActivityLevel.MODERATELY_ACTIVE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diet Planner") },
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Enter Your Details",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    OutlinedTextField(
                        value = heightFeet,
                        onValueChange = { 
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                heightFeet = it
                            }
                        },
                        label = { Text("Height (feet)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = currentWeight,
                        onValueChange = { currentWeight = it },
                        label = { Text("Current Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = targetWeight,
                        onValueChange = { targetWeight = it },
                        label = { Text("Target Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = age,
                        onValueChange = { 
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                age = it
                            }
                        },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Gender Selection
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Gender", style = MaterialTheme.typography.titleMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Gender.values().forEach { genderOption ->
                                FilterChip(
                                    selected = gender == genderOption,
                                    onClick = { gender = genderOption },
                                    label = { Text(genderOption.name.lowercase().capitalize()) }
                                )
                            }
                        }
                    }

                    // Activity Level Selection
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Activity Level", style = MaterialTheme.typography.titleMedium)
                        ActivityLevel.values().forEach { level ->
                            FilterChip(
                                selected = activityLevel == level,
                                onClick = { activityLevel = level },
                                label = { 
                                    Text(level.name.replace("_", " ").lowercase().capitalize())
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val userData = UserData(
                        heightFeet = heightFeet.toIntOrNull() ?: 0,
                        currentWeight = currentWeight.toFloatOrNull() ?: 0f,
                        targetWeight = targetWeight.toFloatOrNull() ?: 0f,
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        activityLevel = activityLevel
                    )
                    onNavigateToDietPlan(userData)
                },
                enabled = heightFeet.isNotEmpty() && 
                         currentWeight.isNotEmpty() && 
                         targetWeight.isNotEmpty() &&
                         age.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Generate Diet Plan")
            }
        }
    }
}

private fun String.capitalize() = replaceFirstChar { 
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
} 