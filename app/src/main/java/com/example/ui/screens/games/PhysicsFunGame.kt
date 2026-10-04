package com.example.ui.screens.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueContainer
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.ui.theme.STEMYellow
import com.example.viewmodel.STEMViewModel
import kotlinx.coroutines.launch

@Composable
fun PhysicsFunGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedSurface by remember { mutableStateOf("Smooth Ice (Low Friction)") }
    var distanceTraveled by remember { mutableFloatStateOf(0f) }
    var isRolling by remember { mutableStateOf(false) }

    val carOffsetX = remember { Animatable(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("physics_fun_game")
    ) {
        // Back Top Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Physics Ramp & Friction Lab",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Which surface makes the car travel the farthest?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Ramp Track Stage
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                // Surface Track Bed
                val trackColor = when {
                    selectedSurface.startsWith("Smooth Ice") -> BrightCyan.copy(alpha = 0.4f)
                    selectedSurface.startsWith("Smooth Wood") -> Color(0xFFFED7AA)
                    else -> Color(0xFFCBD5E1) // Sandpaper
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(trackColor)
                        .align(Alignment.BottomCenter)
                )

                // Measurement tick marks
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0m", style = MaterialTheme.typography.labelSmall)
                    Text("5m", style = MaterialTheme.typography.labelSmall)
                    Text("10m", style = MaterialTheme.typography.labelSmall)
                    Text("15m", style = MaterialTheme.typography.labelSmall)
                    Text("20m", style = MaterialTheme.typography.labelSmall)
                }

                // Rolling Car
                Box(
                    modifier = Modifier
                        .offset { IntOffset(carOffsetX.value.toInt(), 0) }
                        .align(Alignment.BottomStart)
                        .padding(bottom = 12.dp)
                ) {
                    Text("🏎️", fontSize = 36.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Distance & Results Callout
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ElectricBlueContainer)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Distance Traveled:", style = MaterialTheme.typography.labelSmall, color = ElectricBlue)
                    Text(
                        text = "%.1f meters".format(distanceTraveled),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold, color = ElectricBlue)
                    )
                }
                Text(
                    text = when {
                        distanceTraveled >= 18f -> "🚀 Maximum Glide!"
                        distanceTraveled >= 10f -> "⚡ Good Momentum"
                        distanceTraveled > 0f -> "🛑 High Friction Stopped It"
                        else -> "Ready to launch"
                    },
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ramp Surface Selectors
        Text(text = "Select Ramp Surface Texture", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))

        val surfaces = listOf(
            Triple("Smooth Ice (Low Friction)", "Friction coefficient μ ≈ 0.05 • Slippery ice lets car glide far", 19.5f),
            Triple("Smooth Wood (Medium Friction)", "Friction coefficient μ ≈ 0.30 • Moderate rolling resistance", 11.8f),
            Triple("Rough Sandpaper (High Friction)", "Friction coefficient μ ≈ 0.85 • Microscopic bumps quickly brake wheels", 4.2f)
        )

        surfaces.forEach { (name, explanation, targetMeters) ->
            val isSelected = selectedSurface == name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(enabled = !isRolling) {
                        selectedSurface = name
                        coroutineScope.launch { carOffsetX.snapTo(0f); distanceTraveled = 0f }
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ElectricBlueContainer else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LAUNCH CAR BUTTON
        Button(
            onClick = {
                if (!isRolling) {
                    isRolling = true
                    coroutineScope.launch {
                        carOffsetX.snapTo(0f)
                        val targetDist = when {
                            selectedSurface.startsWith("Smooth Ice") -> 19.5f
                            selectedSurface.startsWith("Smooth Wood") -> 11.8f
                            else -> 4.2f
                        }
                        val targetPixel = targetDist * 14f

                        carOffsetX.animateTo(
                            targetValue = targetPixel,
                            animationSpec = tween(1200)
                        )
                        distanceTraveled = targetDist
                        isRolling = false
                        viewModel.addRewards("Friction Experiment Logged!", 40, 15, "physics_explorer")
                    }
                }
            },
            enabled = !isRolling,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().testTag("launch_car_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("RELEASE CAR DOWN RAMP 🏎️", fontWeight = FontWeight.Bold)
            }
        }
    }
}
