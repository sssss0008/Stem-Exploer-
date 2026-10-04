package com.example.ui.screens.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueContainer
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.ui.theme.STEMYellow
import com.example.viewmodel.STEMViewModel

@Composable
fun CircuitPuzzleGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hasBattery by remember { mutableStateOf(true) }
    var hasWireA by remember { mutableStateOf(true) }
    var hasWireB by remember { mutableStateOf(true) }
    var isSwitchClosed by remember { mutableStateOf(false) }

    val isCircuitComplete = hasBattery && hasWireA && hasWireB && isSwitchClosed

    val bulbGlowColor by animateColorAsState(
        targetValue = if (isCircuitComplete) STEMYellow else Color.Gray,
        label = "bulb_glow"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("circuit_puzzle_game")
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
                    text = "Circuit Puzzle",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Electricity and Complete Circuit Simulator",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Circuit Diagram Visual Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Component: Light Bulb
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(bulbGlowColor.copy(alpha = if (isCircuitComplete) 0.3f else 0.1f))
                            .border(2.dp, bulbGlowColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Light Bulb",
                            tint = bulbGlowColor,
                            modifier = Modifier.size(46.dp)
                        )
                    }

                    // Middle Connecting Wires & Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Wire
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isCircuitComplete) BrightCyan else Color.LightGray)
                        )

                        // Center Switch
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { isSwitchClosed = !isSwitchClosed }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("circuit_switch_toggle"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isSwitchClosed) "SWITCH: CLOSED 🟢" else "SWITCH: OPEN 🔴",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (isSwitchClosed) "(Current flows)" else "(Gap in wire)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Right Wire
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isCircuitComplete) BrightCyan else Color.LightGray)
                        )
                    }

                    // Bottom Component: Battery
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(ElectricBlueContainer)
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔋", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "DC Battery (9V Source)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = ElectricBlue
                            )
                            Text(
                                text = "+ Positive | - Negative",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Feedback Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(if (isCircuitComplete) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (isCircuitComplete) "💡 Complete Circuit Activated!" else "⚠️ Incomplete Circuit",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCircuitComplete) Color(0xFF065F46) else Color(0xFF92400E)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isCircuitComplete)
                        "Electricity can flow through a complete circuit loop! Electrons travel from the battery through the closed switch to illuminate the bulb."
                    else
                        "Electricity needs an unbroken path from the power source through every component and back. Tap the switch to close the circuit loop!",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isCircuitComplete) Color(0xFF065F46) else Color(0xFF92400E)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Reward Button if Complete
        if (isCircuitComplete) {
            Button(
                onClick = {
                    viewModel.addRewards("Circuit Master!", 35, 10, "circuit_puzzle", "electric_motor")
                },
                colors = ButtonDefaults.buttonColors(containerColor = STEMGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("claim_circuit_reward_button")
            ) {
                Text(
                    text = "CLAIM REWARD (+35 XP, +10 Stars) 🎉",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
