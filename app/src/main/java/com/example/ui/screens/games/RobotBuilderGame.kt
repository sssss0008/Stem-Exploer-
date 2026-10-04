package com.example.ui.screens.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.ui.theme.ElectricBlueContainer
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.ui.theme.STEMYellow
import com.example.viewmodel.STEMViewModel
import kotlinx.coroutines.launch

@Composable
fun RobotBuilderGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    // Customization states
    var selectedHead by remember { mutableStateOf("Square Visor") }
    var selectedBody by remember { mutableStateOf("Armored Titanium") }
    var selectedArms by remember { mutableStateOf("Laser Grippers") }
    var selectedMobility by remember { mutableStateOf("All-Terrain Tracks") }
    var selectedColor by remember { mutableStateOf(Color(0xFF0284C7)) }
    var selectedSensor by remember { mutableStateOf("Lidar Scanner") }

    // Test simulation state
    var isTesting by remember { mutableStateOf(false) }
    var testSucceeded by remember { mutableStateOf(false) }
    val robotOffsetX = remember { Animatable(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("robot_builder_game")
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
                    text = "Robot Builder Lab",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Design, assemble, and test your robotic assistant!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Robot Preview Stage
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
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Testing track
                if (isTesting) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Start: Bay A", style = MaterialTheme.typography.labelSmall)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = STEMGreen)
                            Text("Charging Station", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }

                // Rendered Robot Composite
                Box(
                    modifier = Modifier
                        .offset { IntOffset(robotOffsetX.value.toInt(), 0) }
                        .align(if (isTesting) Alignment.CenterStart else Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Antenna / Sensor
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(if (selectedSensor == "Lidar Scanner") BrightCyan else STEMYellow)
                        )
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(8.dp)
                                .background(Color.Gray)
                        )

                        // Head
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(if (selectedHead == "Round Dome") 25.dp else 12.dp))
                                .background(selectedColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }

                        // Torso / Body + Arms
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Left Arm
                            Box(
                                modifier = Modifier
                                    .size(14.dp, 36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(selectedColor.copy(alpha = 0.8f))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Center Body
                            Box(
                                modifier = Modifier
                                    .size(64.dp, 56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(selectedColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp, 24.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.4f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "⚡",
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            // Right Arm
                            Box(
                                modifier = Modifier
                                    .size(14.dp, 36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(selectedColor.copy(alpha = 0.8f))
                            )
                        }

                        // Mobility (Legs or Tracks)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (selectedMobility == "All-Terrain Tracks") {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp, 16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.DarkGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("● ● ● ●", color = Color.LightGray, fontSize = 9.sp)
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp, 24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.DarkGray)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(18.dp, 24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.DarkGray)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Customization Selectors
        Text(
            text = "Robot Colors",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val colorsList = listOf(
            Color(0xFF0284C7) to "Electric Blue",
            Color(0xFF7C3AED) to "Cosmic Purple",
            Color(0xFF10B981) to "Neon Green",
            Color(0xFFF97316) to "Solar Orange",
            Color(0xFFE11D48) to "Crimson Red"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            colorsList.forEach { (color, name) ->
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (selectedColor == color) 3.dp else 0.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Heads Selector
        Text(
            text = "Head Chassis",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Square Visor", "Round Dome", "Cyber Hex").forEach { head ->
                val isSelected = selectedHead == head
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedHead = head }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = head,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mobility Selector
        Text(
            text = "Locomotion System",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All-Terrain Tracks", "Hydraulic Legs").forEach { mobility ->
                val isSelected = selectedMobility == mobility
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedMobility = mobility }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mobility,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TEST YOUR ROBOT CHALLENGE BUTTON
        Button(
            onClick = {
                if (!isTesting) {
                    isTesting = true
                    coroutineScope.launch {
                        robotOffsetX.animateTo(
                            targetValue = 400f,
                            animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
                        )
                        testSucceeded = true
                        viewModel.addRewards("Robot Test Complete!", 40, 15, "robot_builder")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = if (testSucceeded) STEMGreen else ElectricBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("test_robot_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = if (testSucceeded) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (testSucceeded) "TEST PASSED! (+40 XP)" else "TEST YOUR ROBOT ⚡",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "STEM Concept: Robotics combines structural chassis design, actuator torque, sensors for guidance, and onboard power distribution.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
