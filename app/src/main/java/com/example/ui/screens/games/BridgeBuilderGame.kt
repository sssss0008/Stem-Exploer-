package com.example.ui.screens.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BridgeBuilderGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var hasSteelBeams by remember { mutableStateOf(true) }
    var hasTrussTriangles by remember { mutableStateOf(false) }
    var hasSuspensionCables by remember { mutableStateOf(false) }
    var hasConcretePillars by remember { mutableStateOf(true) }

    var testCarsCount by remember { mutableIntStateOf(0) }
    var isTesting by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<String?>(null) }
    val carOffsetX = remember { Animatable(0f) }

    // Strength calculation
    val structuralStrength = when {
        hasSteelBeams && hasTrussTriangles && hasSuspensionCables -> "Strong (Holds 10 Cars)"
        hasSteelBeams && (hasTrussTriangles || hasSuspensionCables) -> "Moderate (Holds 6 Cars)"
        else -> "Weak (Needs Triangular Trusses)"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("bridge_builder_game")
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
                    text = "Bridge Builder Lab",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Structural Engineering & Stress Simulation",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bridge Visual Canvas Stage
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
                contentAlignment = Alignment.BottomCenter
            ) {
                // River water below
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .background(BrightCyan.copy(alpha = 0.3f))
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🌊 Blue Canyon River (Tension & Compression Zone) 🌊", style = MaterialTheme.typography.labelSmall)
                }

                // Bridge Deck
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Suspension Cables if active
                    if (hasSuspensionCables) {
                        Text(
                            text = "▲──────▲──────▲",
                            color = CosmicPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    // Triangular Trusses if active
                    if (hasTrussTriangles) {
                        Text(
                            text = "▲▼▲▼▲▼▲▼▲▼▲▼▲▼▲",
                            color = ElectricBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Road Deck Beams
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (hasSteelBeams) Color(0xFF334155) else Color.LightGray)
                    )

                    // Concrete Pillars
                    if (hasConcretePillars) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(modifier = Modifier.size(24.dp, 40.dp).background(Color.Gray))
                            Box(modifier = Modifier.size(24.dp, 40.dp).background(Color.Gray))
                        }
                    }
                }

                // Car moving across during test
                if (isTesting) {
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(carOffsetX.value.toInt(), 0) }
                            .align(Alignment.CenterStart)
                            .padding(bottom = 14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(testCarsCount) {
                                Text("🚗", fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Engineering Components Toggles
        Text(text = "Engineering Truss Components", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))

        val components = listOf(
            Triple("Steel Road Beams", "Provides horizontal vehicle surface", hasSteelBeams) to { hasSteelBeams = !hasSteelBeams },
            Triple("Triangular Trusses", "Distributes load tension and compression", hasTrussTriangles) to { hasTrussTriangles = !hasTrussTriangles },
            Triple("Suspension Cables", "Hangs deck from anchor towers", hasSuspensionCables) to { hasSuspensionCables = !hasSuspensionCables },
            Triple("Concrete Pillars", "Transfers vertical load down into bedrock", hasConcretePillars) to { hasConcretePillars = !hasConcretePillars }
        )

        components.forEach { (comp, toggle) ->
            val (title, desc, isEnabled) = comp
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { toggle() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isEnabled) ElectricBlueContainer else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = if (isEnabled) "ADDED ✓" else "+ ADD",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isEnabled) ElectricBlue else MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Strength Status Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "Structural Rating: $structuralStrength",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = ElectricBlue)
                )
                if (testResult != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = testResult!!,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (testResult!!.startsWith("Strong")) STEMGreen else STEMOrange
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TEST BUTTON: HOLD 10 CARS
        Button(
            onClick = {
                if (!isTesting) {
                    isTesting = true
                    testCarsCount = 1
                    coroutineScope.launch {
                        for (i in 1..10) {
                            delay(200)
                            testCarsCount = i
                        }
                        carOffsetX.animateTo(
                            targetValue = 280f,
                            animationSpec = tween(1200)
                        )
                        isTesting = false
                        if (hasSteelBeams && hasTrussTriangles) {
                            testResult = "Strong! Your bridge safely held all 10 cars!"
                            viewModel.addRewards("Bridge Engineer!", 50, 20, "young_engineer", "suspension_bridge")
                        } else {
                            testResult = "Needs Improvement: Add Triangular Trusses to handle tension!"
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("test_bridge_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("TEST BRIDGE WITH 10 CARS 🚗", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "STEM Concept: Triangles are the most rigid geometric structure in engineering because their angles cannot deform without breaking a side.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
