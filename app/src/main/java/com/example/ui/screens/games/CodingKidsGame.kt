package com.example.ui.screens.games

import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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

enum class BlockType(val label: String, val emoji: String, val color: Color) {
    MOVE_FORWARD("MOVE FORWARD", "⬆️", ElectricBlue),
    TURN_LEFT("TURN LEFT", "⬅️", CosmicPurple),
    TURN_RIGHT("TURN RIGHT", "➡️", CosmicPurple),
    JUMP("JUMP", "🦘", STEMOrange),
    REPEAT_2X("REPEAT 2X", "🔁", BrightCyan),
    COLLECT("COLLECT", "⭐", STEMYellow),
    STOP("STOP", "🛑", Color(0xFFEF4444))
}

@Composable
fun CodingKidsGame(
    viewModel: STEMViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    // Program blocks assembled by child
    val programBlocks = remember { mutableStateListOf<BlockType>() }

    // Grid coordinates: 4x4 grid (row: 0..3, col: 0..3)
    // Starting at (3, 0), Target Star at (0, 3)
    var robotRow by remember { mutableIntStateOf(3) }
    var robotCol by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var gameCompleted by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Arrange blocks to help Rover reach the star!") }

    fun resetGrid() {
        robotRow = 3
        robotCol = 0
        isRunning = false
        gameCompleted = false
        statusMessage = "Arrange blocks to help Rover reach the star!"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("coding_kids_game")
    ) {
        // Back row
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
                    text = "Coding For Kids",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Algorithm & Logic Block Lab",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4x4 Playfield Grid
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (gameCompleted) STEMGreen else MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 4x4 Grid Board
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (r in 0..3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (c in 0..3) {
                                val isRover = robotRow == r && robotCol == c
                                val isTarget = r == 0 && c == 3
                                val isObstacle = (r == 1 && c == 1) || (r == 2 && c == 2)

                                val cellColor = when {
                                    isRover -> ElectricBlueContainer
                                    isTarget -> Color(0xFFFEF3C7)
                                    isObstacle -> Color(0xFFFEE2E2)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                }

                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(cellColor)
                                        .border(
                                            width = if (isRover) 2.dp else 1.dp,
                                            color = if (isRover) ElectricBlue else MaterialTheme.colorScheme.outlineVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when {
                                        isRover -> Text(text = "🤖", fontSize = 28.sp)
                                        isTarget -> Text(text = "⭐", fontSize = 28.sp)
                                        isObstacle -> Text(text = "🪨", fontSize = 24.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Program Sequence Tray
        Text(
            text = "Your Code Sequence (Algorithm)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            if (programBlocks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tap code blocks below to build your sequence!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(STEMGreen)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("START", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    programBlocks.forEachIndexed { idx, block ->
                        Text("➔", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(block.color)
                                .clickable { programBlocks.removeAt(idx) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${block.emoji} ${block.label}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = { programBlocks.clear(); resetGrid() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear all", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Available Blocks Palette
        Text(
            text = "Code Blocks Library",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val availableBlocks = listOf(
            BlockType.MOVE_FORWARD,
            BlockType.TURN_RIGHT,
            BlockType.TURN_LEFT,
            BlockType.REPEAT_2X,
            BlockType.COLLECT
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableBlocks.forEach { b ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(b.color)
                        .clickable(enabled = !isRunning) { programBlocks.add(b) }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("code_block_${b.name}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = b.emoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = b.label,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Action Buttons: RUN PROGRAM / RESET
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (!isRunning && programBlocks.isNotEmpty()) {
                        isRunning = true
                        coroutineScope.launch {
                            for (block in programBlocks) {
                                delay(600)
                                when (block) {
                                    BlockType.MOVE_FORWARD -> {
                                        if (robotRow > 0) robotRow--
                                    }
                                    BlockType.TURN_RIGHT -> {
                                        if (robotCol < 3) robotCol++
                                    }
                                    BlockType.TURN_LEFT -> {
                                        if (robotCol > 0) robotCol--
                                    }
                                    BlockType.REPEAT_2X -> {
                                        if (robotRow > 0) robotRow--
                                        delay(300)
                                        if (robotCol < 3) robotCol++
                                    }
                                    BlockType.COLLECT -> {
                                        if (robotRow == 0 && robotCol == 3) {
                                            gameCompleted = true
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            isRunning = false
                            if (robotRow == 0 && robotCol == 3) {
                                gameCompleted = true
                                statusMessage = "🎉 Success! Algorithm solved!"
                                viewModel.addRewards("Coding Puzzle Solved!", 50, 20, "coding_starter")
                            } else {
                                statusMessage = "Almost there! Adjust your algorithm and try again."
                            }
                        }
                    }
                },
                enabled = !isRunning && programBlocks.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("run_code_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("RUN CODE", fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = { resetGrid() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "STEM Concept: Sequencing is arranging steps in precise chronological order so computers can solve tasks predictably without error.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
