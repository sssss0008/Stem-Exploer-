package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

data class STEMCard(
    val term: String,
    val definition: String,
    val category: String,
    val emoji: String,
    val example: String
)

@Composable
fun FlashcardsScreen(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cards = remember {
        listOf(
            STEMCard("Photosynthesis", "The biological process where plants convert sunlight, water, and carbon dioxide into oxygen and glucose sugar.", "Biology & Science", "🌱", "Example: Trees producing the oxygen we breathe."),
            STEMCard("Algorithm", "A step-by-step sequence of instructions designed to solve a problem or perform a task in computer software.", "Coding & Tech", "⚡", "Example: A cake recipe or sorting numbers smallest to largest."),
            STEMCard("Inertia", "The tendency of an object to resist changes in its state of motion unless acted upon by an external force.", "Physics", "⚛️", "Example: Passengers leaning forward when a bus suddenly brakes."),
            STEMCard("Tension & Compression", "Tension pulls structural members apart; compression pushes and squeezes them together.", "Engineering", "🏗️", "Example: Bridge cables are in tension; bridge pillars are in compression."),
            STEMCard("Light-Year", "The astronomical distance that light travels in one Earth year (~9.46 trillion kilometers).", "Space & Astronomy", "🚀", "Example: Proxima Centauri is 4.24 light-years away from Earth."),
            STEMCard("Neural Network", "Computing systems inspired by the biological brains that learn to recognize patterns from large datasets.", "Artificial Intelligence", "🧠", "Example: Image recognition apps identifying dogs vs cats."),
            STEMCard("Conductor", "A material that allows electrical current or heat energy to flow freely through it.", "Electricity & Tech", "🔋", "Example: Copper and aluminum wires in electrical circuits.")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var knownCount by remember { mutableIntStateOf(0) }

    val currentCard = cards[currentIndex]
    val rotation by animateFloatAsState(targetValue = if (isFlipped) 180f else 0f, animationSpec = tween(400), label = "flip")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("flashcards_screen")
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
                    text = "STEM Flashcards",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Study concepts, test memory, and build mastery",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Card ${currentIndex + 1} of ${cards.size}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Mastered: $knownCount",
                style = MaterialTheme.typography.labelMedium.copy(color = STEMGreen, fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / cards.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ElectricBlue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // FLIP CARD CONTAINER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable { isFlipped = !isFlipped }
                .testTag("flashcard_flip_target"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isFlipped) Color(0xFFEDE9FE) else MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // FRONT OF CARD
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = currentCard.emoji, fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentCard.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = ElectricBlue
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentCard.term,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flip, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Tap to reveal definition", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                } else {
                    // BACK OF CARD (Flipped)
                    Column(
                        modifier = Modifier.graphicsLayer { rotationY = 180f },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentCard.definition,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = currentCard.example,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = CosmicPurple
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Known / Review Later Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    isFlipped = false
                    if (currentIndex + 1 < cards.size) currentIndex++ else currentIndex = 0
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f).testTag("flashcard_review_later_button")
            ) {
                Text("REVIEW LATER", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    knownCount++
                    isFlipped = false
                    viewModel.playSuccessSound()
                    if (currentIndex + 1 < cards.size) {
                        currentIndex++
                    } else {
                        viewModel.addRewards("Flashcard Set Mastered!", 35, 10, "science_explorer")
                        currentIndex = 0
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = STEMGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f).testTag("flashcard_known_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("I KNOW THIS!", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
