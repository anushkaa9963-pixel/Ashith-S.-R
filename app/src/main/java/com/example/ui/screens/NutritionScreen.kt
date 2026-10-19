package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NutritionLog
import com.example.ui.MainViewModel
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NutritionScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val todayNutrition by viewModel.todayNutrition.collectAsStateWithLifecycle()

    val calcium = todayNutrition?.calciumMg ?: 0
    val vitD = todayNutrition?.vitaminDIu ?: 0
    val protein = todayNutrition?.proteinG ?: 0
    val water = todayNutrition?.waterMl ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "NUTRIENT GROWTH TRACKER",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Height Essential Nutrients",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nutrient 1: Calcium (mg)
            item {
                NutrientProgressCard(
                    title = "Calcium",
                    subtitle = "Bone Matrix & Epiphyseal Mineralization",
                    current = calcium,
                    target = NutritionLog.TARGET_CALCIUM_MG,
                    unit = "mg",
                    accentColor = NeonCyan,
                    icon = Icons.Default.Science,
                    quickAddLabel = "+300 mg",
                    onQuickAdd = { viewModel.addNutritionIntake(calciumMg = 300) }
                )
            }

            // Nutrient 2: Vitamin D (IU)
            item {
                NutrientProgressCard(
                    title = "Vitamin D3",
                    subtitle = "Stimulates Active Calcium Absorption",
                    current = vitD,
                    target = NutritionLog.TARGET_VITAMIN_D_IU,
                    unit = "IU",
                    accentColor = NeonAmber,
                    icon = Icons.Default.WbSunny,
                    quickAddLabel = "+400 IU",
                    onQuickAdd = { viewModel.addNutritionIntake(vitDIu = 400) }
                )
            }

            // Nutrient 3: Protein (g)
            item {
                NutrientProgressCard(
                    title = "Protein & Collagen",
                    subtitle = "Spinal Disc Nucleus Pulposus Collagen Matrix",
                    current = protein,
                    target = NutritionLog.TARGET_PROTEIN_G,
                    unit = "g",
                    accentColor = NeonLime,
                    icon = Icons.Default.Egg,
                    quickAddLabel = "+25 g",
                    onQuickAdd = { viewModel.addNutritionIntake(proteinG = 25) }
                )
            }

            // Nutrient 4: Water & Hydration (ml)
            item {
                NutrientProgressCard(
                    title = "Daily Water Consumption",
                    subtitle = "Essential for Nighttime Disc Rehydration",
                    current = water,
                    target = NutritionLog.TARGET_WATER_ML,
                    unit = "ml",
                    accentColor = NeonPurple,
                    icon = Icons.Default.WaterDrop,
                    quickAddLabel = "+250 ml",
                    onQuickAdd = { viewModel.addNutritionIntake(waterMl = 250) }
                )
            }

            // Scientific Growth Plate Nutrition Guide
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(NeonCyan.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "BONE & DISC NUTRITION SCIENCE",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "• Intervertebral discs are 80% water. Dehydration causes discs to thin out, losing up to 1-2 cm of temporary height over the course of the day.\n" +
                                    "• Vitamin D converts into calcitriol to transport circulating calcium into bone and cartilage.\n" +
                                    "• Dietary protein provides proline and glycine, the raw building blocks for annular collagen rings surrounding each spinal disc.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NutrientProgressCard(
    title: String,
    subtitle: String,
    current: Int,
    target: Int,
    unit: String,
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    quickAddLabel: String,
    onQuickAdd: () -> Unit
) {
    val progress = (current.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(accentColor.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$percentage%",
                        color = accentColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Values row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$current",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " / $target $unit",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                // 1-Tap Quick Add Button
                Button(
                    onClick = onQuickAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .testTag("quick_add_${title.lowercase().replace(" ", "_")}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = quickAddLabel,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = accentColor,
                trackColor = DarkSurfaceVariant,
                strokeCap = StrokeCap.Round
            )
        }
    }
}
