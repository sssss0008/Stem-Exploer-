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
import androidx.compose.material.icons.filled.Refresh
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
import com.example.viewmodel.STEMViewModel
import kotlinx.coroutines.launch

@Composable
fun GravityExperimentGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedEnvironment by remember { mutableStateOf("Vacuum Chamber") } // "Earth Atmosphere", "Moon Surface", "Vacuum Chamber"

    val objectAOffset = remember { Animatable(0f) }
    val objectBOffset = remember { Animatable(0f) }
    var isDropping by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("gravity_experiment_game")
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
                    text = "Gravity Drop Lab",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Galileo's Free Fall & Vacuum Physics",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Virtual Drop Tower Stage
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
                    .padding(16.dp)
            ) {
                // Ground baseline
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF334155))
                        .align(Alignment.BottomCenter)
                )

                // Objects Dropping
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Object A: Heavy Bowling Ball
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.offset { IntOffset(0, objectAOffset.value.toInt()) }
                    ) {
                        Text(text = "🎳", fontSize = 36.sp)
                        Text(text = "Bowling Ball (7 kg)", style = MaterialTheme.typography.labelSmall)
                    }

                    // Object B: Light Feather
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.offset { IntOffset(0, objectBOffset.value.toInt()) }
                    ) {
                        Text(text = "🪶", fontSize = 36.sp)
                        Text(text = "Feather (0.005 kg)", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Environment Selector
        Text(
            text = "Select Environment",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val envs = listOf("Vacuum Chamber", "Earth Atmosphere", "Moon Surface")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            envs.forEach { env ->
                val isSelected = selectedEnvironment == env
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(enabled = !isDropping) {
                            selectedEnvironment = env
                            coroutineScope.launch {
                                objectAOffset.snapTo(0f)
                                objectBOffset.snapTo(0f)
                            }
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = env,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // DROP BUTTON
        Button(
            onClick = {
                if (!isDropping) {
                    isDropping = true
                    coroutineScope.launch {
                        objectAOffset.snapTo(0f)
                        objectBOffset.snapTo(0f)

                        when (selectedEnvironment) {
                            "Vacuum Chamber" -> {
                                // Both fall at same acceleration g = 9.8 m/s²
                                launch { objectAOffset.animateTo(150f, tween(1000)) }
                                launch { objectBOffset.animateTo(150f, tween(1000)) }
                            }
                            "Earth Atmosphere" -> {
                                // Air resistance slows down the feather
                                launch { objectAOffset.animateTo(150f, tween(1000)) }
                                launch { objectBOffset.animateTo(150f, tween(2400)) }
                            }
                            "Moon Surface" -> {
                                // Lower gravity (1.6 m/s²), no air resistance
                                launch { objectAOffset.animateTo(150f, tween(1900)) }
                                launch { objectBOffset.animateTo(150f, tween(1900)) }
                            }
                        }
                        isDropping = false
                        viewModel.addRewards("Gravity Experiment Complete!", 30, 10, "physics_explorer")
                    }
                }
            },
            enabled = !isDropping,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().testTag("drop_objects_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("RELEASE OBJECTS & TEST GRAVITY 🍎", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Educational Insight
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Discovery of Galileo & Apollo 15:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "In 1971 on the Moon, astronaut David Scott dropped a falcon feather and a geology hammer simultaneously. Because the Moon has no atmosphere, both hit the lunar dust at the exact same moment!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
