package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.STEMCategory
import com.example.data.ScreenRoute
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueContainer
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.viewmodel.STEMViewModel

data class PracticeModuleItem(
    val id: String,
    val title: String,
    val category: STEMCategory,
    val description: String,
    val emoji: String,
    val difficulty: String,
    val progress: Float,
    val xpReward: Int,
    val targetRoute: ScreenRoute
)

@Composable
fun PracticeScreen(
    viewModel: STEMViewModel,
    onNavigate: (ScreenRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<STEMCategory?>(null) }

    val modules = listOf(
        // SCIENCE
        PracticeModuleItem(
            id = "sci_lab",
            title = "Science Lab Experiments",
            category = STEMCategory.SCIENCE,
            description = "Virtual chemistry and density tests: test pH colors, mix solutions!",
            emoji = "🧪",
            difficulty = "Beginner",
            progress = 0.75f,
            xpReward = 40,
            targetRoute = ScreenRoute.SCIENCE_LAB
        ),
        PracticeModuleItem(
            id = "sci_gravity",
            title = "Gravity & Vacuum Test",
            category = STEMCategory.PHYSICS,
            description = "Drop feathers and bowling balls on Earth vs Moon vs Vacuum chamber!",
            emoji = "🍎",
            difficulty = "Easy",
            progress = 1.0f,
            xpReward = 30,
            targetRoute = ScreenRoute.GRAVITY_EXP
        ),
        PracticeModuleItem(
            id = "sci_physics_fun",
            title = "Physics For Kids",
            category = STEMCategory.PHYSICS,
            description = "Learn forces, ramps, friction, and magnetism through mini-experiments!",
            emoji = "🧲",
            difficulty = "Intermediate",
            progress = 0.5f,
            xpReward = 45,
            targetRoute = ScreenRoute.PHYSICS_FUN
        ),
        // TECHNOLOGY
        PracticeModuleItem(
            id = "tech_ai",
            title = "AI & Pattern Learning",
            category = STEMCategory.AI,
            description = "Understand how neural networks learn to classify shapes & animals!",
            emoji = "🧠",
            difficulty = "Medium",
            progress = 0.4f,
            xpReward = 50,
            targetRoute = ScreenRoute.AI_LEARNING
        ),
        PracticeModuleItem(
            id = "tech_circuit",
            title = "Circuit & Electricity Puzzle",
            category = STEMCategory.TECHNOLOGY,
            description = "Connect battery cells, conductive wires, and switches to power bulbs!",
            emoji = "⚡",
            difficulty = "Easy",
            progress = 0.6f,
            xpReward = 35,
            targetRoute = ScreenRoute.CIRCUIT_PUZZLE
        ),
        PracticeModuleItem(
            id = "tech_facts",
            title = "Technology Facts & Satellites",
            category = STEMCategory.TECHNOLOGY,
            description = "Swipeable trivia cards about microchips, internet, and supercomputers!",
            emoji = "📱",
            difficulty = "Beginner",
            progress = 0.8f,
            xpReward = 25,
            targetRoute = ScreenRoute.TECH_FACTS
        ),
        // ENGINEERING
        PracticeModuleItem(
            id = "eng_bridge",
            title = "Bridge Structural Builder",
            category = STEMCategory.ENGINEERING,
            description = "Construct triangular trusses and test load capacity with heavy trucks!",
            emoji = "🏗️",
            difficulty = "Medium",
            progress = 0.3f,
            xpReward = 50,
            targetRoute = ScreenRoute.BRIDGE_BUILDER
        ),
        PracticeModuleItem(
            id = "eng_machine",
            title = "Machine Maker & Gears",
            category = STEMCategory.ENGINEERING,
            description = "Combine intermeshing gears, pulleys, and levers to build machines!",
            emoji = "⚙️",
            difficulty = "Beginner",
            progress = 0.7f,
            xpReward = 35,
            targetRoute = ScreenRoute.MACHINE_MAKER
        ),
        PracticeModuleItem(
            id = "eng_robot",
            title = "Robot Customizer & Lab",
            category = STEMCategory.ROBOTICS,
            description = "Assemble heads, arm sensors, and wheels, then complete battery challenges!",
            emoji = "🤖",
            difficulty = "Easy",
            progress = 0.9f,
            xpReward = 45,
            targetRoute = ScreenRoute.ROBOT_BUILDER
        ),
        // SPACE
        PracticeModuleItem(
            id = "space_solar",
            title = "Solar System Explorer",
            category = STEMCategory.SPACE,
            description = "Touch and inspect all 8 planets from the Sun out to Neptune!",
            emoji = "🪐",
            difficulty = "Beginner",
            progress = 1.0f,
            xpReward = 35,
            targetRoute = ScreenRoute.SOLAR_SYSTEM
        ),
        PracticeModuleItem(
            id = "space_rocket",
            title = "Rocket Launch Sequence",
            category = STEMCategory.SPACE,
            description = "Configure thrust stages, payload fairings, and achieve stable Earth orbit!",
            emoji = "🚀",
            difficulty = "Intermediate",
            progress = 0.5f,
            xpReward = 45,
            targetRoute = ScreenRoute.ROCKET_LAUNCH
        ),
        PracticeModuleItem(
            id = "space_mission",
            title = "Planetary Exploration Mission",
            category = STEMCategory.SPACE,
            description = "Fly probes to Mars, Jupiter, and asteroids to gather scientific data!",
            emoji = "🛸",
            difficulty = "Easy",
            progress = 0.6f,
            xpReward = 45,
            targetRoute = ScreenRoute.SPACE_MISSION
        ),
        // CODING
        PracticeModuleItem(
            id = "coding_blocks",
            title = "Visual Block Coding",
            category = STEMCategory.CODING,
            description = "Drag & arrange MOVE, TURN, JUMP, and REPEAT blocks to guide the bot!",
            emoji = "💻",
            difficulty = "Intermediate",
            progress = 0.5f,
            xpReward = 50,
            targetRoute = ScreenRoute.CODING_GAME
        ),
        PracticeModuleItem(
            id = "stem_flashcards",
            title = "STEM Flashcards Deck",
            category = STEMCategory.SCIENCE,
            description = "Flip interactive concept flashcards and test your recall!",
            emoji = "🎴",
            difficulty = "Beginner",
            progress = 0.4f,
            xpReward = 30,
            targetRoute = ScreenRoute.FLASHCARDS
        )
    )

    val filteredModules = if (selectedCategoryFilter == null) {
        modules
    } else {
        modules.filter { it.category == selectedCategoryFilter }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("practice_screen")
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Explore & Learn",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Discover how the world works through hands-on experiments.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Filter Pills
        val filterCategories = listOf(
            null to "All Topics",
            STEMCategory.SCIENCE to "🔬 Science",
            STEMCategory.PHYSICS to "⚛️ Physics",
            STEMCategory.TECHNOLOGY to "💻 Tech",
            STEMCategory.ENGINEERING to "🏗️ Engineering",
            STEMCategory.SPACE to "🚀 Space",
            STEMCategory.CODING to "⚡ Coding",
            STEMCategory.AI to "🧠 AI"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterCategories.forEach { (cat, label) ->
                val isSelected = selectedCategoryFilter == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedCategoryFilter = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("filter_${label.replace(" ", "_")}")
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        // Modules List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredModules, key = { it.id }) { item ->
                PracticeModuleCard(
                    item = item,
                    onClick = { onNavigate(item.targetRoute) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PracticeModuleCard(
    item: PracticeModuleItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("practice_module_${item.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(item.category.colorHex).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.emoji, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item.category.displayName.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(item.category.colorHex),
                                letterSpacing = 0.5.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.difficulty,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar & Rewards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (item.progress >= 1f) "Completed! 🎉" else "Progress",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (item.progress >= 1f) STEMGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "${(item.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { item.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (item.progress >= 1f) STEMGreen else ElectricBlue,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElectricBlue)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "START",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
