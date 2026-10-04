package com.example.ui.screens.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RocketLaunch
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightCyan
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.ui.theme.STEMYellow
import com.example.ui.theme.SpaceDarkBg
import com.example.viewmodel.STEMViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RocketLaunchGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedStage by remember { mutableStateOf("Two-Stage Multi") }
    var selectedEngine by remember { mutableStateOf("Liquid Oxygen (Raptor)") }
    var selectedFuel by remember { mutableStateOf("Liquid Hydrogen & LOX") }
    var selectedNoseCone by remember { mutableStateOf("Aerodynamic Ogive") }

    var countdown by remember { mutableIntStateOf(3) }
    var isLaunching by remember { mutableStateOf(false) }
    var hasReachedOrbit by remember { mutableStateOf(false) }
    val rocketOffsetY = remember { Animatable(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("rocket_launch_game")
    ) {
        // Top Back Row
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
                    text = "Rocket Launch Sequence",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Aerodynamics, Thrust & Orbital Mechanics",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Launch Pad Stage Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = SpaceDarkBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Stars & Atmosphere Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF030712),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                )

                // Countdown overlay
                if (isLaunching && countdown > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$countdown",
                            color = STEMYellow,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Orbit reached banner
                if (hasReachedOrbit) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .clip(RoundedCornerShape(14.dp))
                            .background(STEMGreen.copy(alpha = 0.9f))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🛰️ Stable Earth Orbit Achieved! (300 km)",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Rocket Assembly
                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, rocketOffsetY.value.toInt()) }
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Nose Cone
                        Box(
                            modifier = Modifier
                                .size(24.dp, 28.dp)
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                .background(Color.White)
                        )
                        // Main Payload & Body
                        Box(
                            modifier = Modifier
                                .size(34.dp, 60.dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "USA", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        }
                        // First Stage Engine + Fins
                        Row(verticalAlignment = Alignment.Bottom) {
                            // Left fin
                            Box(
                                modifier = Modifier
                                    .size(12.dp, 24.dp)
                                    .clip(RoundedCornerShape(bottomStart = 8.dp))
                                    .background(ElectricBlue)
                            )
                            // Engine bell
                            Box(
                                modifier = Modifier
                                    .size(24.dp, 16.dp)
                                    .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                                    .background(Color.DarkGray)
                            )
                            // Right fin
                            Box(
                                modifier = Modifier
                                    .size(12.dp, 24.dp)
                                    .clip(RoundedCornerShape(bottomEnd = 8.dp))
                                    .background(ElectricBlue)
                            )
                        }
                        // Plume / Exhaust Fire if launching
                        if (isLaunching && countdown == 0) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp, 50.dp)
                                    .clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(STEMYellow, STEMOrange, Color.Transparent)
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Configuration Options
        Text(text = "Rocket Configuration", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Stage Architecture" to listOf("Two-Stage Multi", "Heavy Three-Core"),
            "Propellant Chemistry" to listOf("Liquid Hydrogen & LOX", "Solid Methane Booster")
        ).forEach { (label, options) ->
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { opt ->
                    val isSelected = (label.startsWith("Stage") && selectedStage == opt) ||
                            (label.startsWith("Propellant") && selectedFuel == opt)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                if (label.startsWith("Stage")) selectedStage = opt else selectedFuel = opt
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = opt,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        // LAUNCH BUTTON
        Button(
            onClick = {
                if (!isLaunching) {
                    isLaunching = true
                    countdown = 3
                    coroutineScope.launch {
                        while (countdown > 0) {
                            delay(900)
                            countdown--
                        }
                        // Ascend rocket
                        rocketOffsetY.animateTo(
                            targetValue = -500f,
                            animationSpec = tween(durationMillis = 2200, easing = FastOutSlowInEasing)
                        )
                        hasReachedOrbit = true
                        viewModel.addRewards("Orbital Rocket Launch!", 45, 15, "rocket_scientist", "rocket_booster")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = STEMOrange),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("launch_rocket_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.RocketLaunch, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasReachedOrbit) "MISSION SUCCESS! (+45 XP)" else "IGNITE & LAUNCH 🚀",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Educational Facts
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Physics Behind Rocketry:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Newton's 3rd Law: For every action, there is an equal and opposite reaction.\n• Thrust must overcome gravitational pull and atmospheric drag to reach orbital velocity (~28,000 km/h).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
