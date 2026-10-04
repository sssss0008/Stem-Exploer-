package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.STEMRank
import com.example.data.ScreenRoute
import com.example.data.UserProfile
import com.example.ui.theme.BrightCyan
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueContainer
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.ui.theme.STEMYellow

@Composable
fun STEMDrawerContent(
    userProfile: UserProfile,
    onNavigate: (ScreenRoute) -> Unit,
    onCloseDrawer: () -> Unit,
    onOpenParentGate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rank = STEMRank.fromXp(userProfile.xp)

    Surface(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(bottom = 32.dp)
        ) {
            // Header Profile Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ElectricBlueContainer)
                    .clickable {
                        onCloseDrawer()
                        onNavigate(ScreenRoute.PROFILE)
                    }
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        val avatarEmoji = when (userProfile.avatarId) {
                            "scientist" -> "🧑‍🔬"
                            "engineer" -> "👷"
                            "astronaut" -> "👨‍🚀"
                            "inventor" -> "🧑‍🏭"
                            else -> "🤖"
                        }
                        Text(text = avatarEmoji, fontSize = 30.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${rank.badgeEmoji} ${rank.title} • ${userProfile.xp} XP",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ElectricBlue
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⭐ ${userProfile.stars} Stars",
                                style = MaterialTheme.typography.labelSmall,
                                color = STEMOrange
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🔥 ${userProfile.streakDays} Day Streak",
                                style = MaterialTheme.typography.labelSmall,
                                color = STEMGreen
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))

            // SECTION: EXPLORE
            DrawerSectionHeader(title = "EXPLORE", icon = Icons.Default.Explore, tint = ElectricBlue)
            DrawerMenuItem(title = "All STEM Games", emoji = "🎮", subtitle = "12 Interactive Games") {
                onCloseDrawer(); onNavigate(ScreenRoute.PRACTICE)
            }
            DrawerMenuItem(title = "Space Explorer", emoji = "🚀", subtitle = "Planets & Space Flight") {
                onCloseDrawer(); onNavigate(ScreenRoute.SPACE_MISSION)
            }
            DrawerMenuItem(title = "Coding Adventures", emoji = "💻", subtitle = "Logic & Programming Blocks") {
                onCloseDrawer(); onNavigate(ScreenRoute.CODING_GAME)
            }
            DrawerMenuItem(title = "Robotics Hub", emoji = "🤖", subtitle = "Design & Test Robots") {
                onCloseDrawer(); onNavigate(ScreenRoute.ROBOT_BUILDER)
            }
            DrawerMenuItem(title = "Science Lab", emoji = "🔬", subtitle = "Virtual Experiments") {
                onCloseDrawer(); onNavigate(ScreenRoute.SCIENCE_LAB)
            }

            // SECTION: LEARN
            DrawerSectionHeader(title = "LEARN", icon = Icons.Default.School, tint = CosmicPurple)
            DrawerMenuItem(title = "Solar System", emoji = "🪐", subtitle = "Explore 8 Planets & the Sun") {
                onCloseDrawer(); onNavigate(ScreenRoute.SOLAR_SYSTEM)
            }
            DrawerMenuItem(title = "Physics For Kids", emoji = "⚛️", subtitle = "Forces, Motion & Gravity") {
                onCloseDrawer(); onNavigate(ScreenRoute.PHYSICS_FUN)
            }
            DrawerMenuItem(title = "AI Learning", emoji = "🧠", subtitle = "Pattern Recognition & Neural Nets") {
                onCloseDrawer(); onNavigate(ScreenRoute.AI_LEARNING)
            }
            DrawerMenuItem(title = "Technology Facts", emoji = "📱", subtitle = "Fascinating STEM Trivia") {
                onCloseDrawer(); onNavigate(ScreenRoute.TECH_FACTS)
            }
            DrawerMenuItem(title = "STEM Flashcards", emoji = "🎴", subtitle = "Study & Test Memory") {
                onCloseDrawer(); onNavigate(ScreenRoute.FLASHCARDS)
            }

            // SECTION: BUILD
            DrawerSectionHeader(title = "BUILD", icon = Icons.Default.Build, tint = STEMOrange)
            DrawerMenuItem(title = "Robot Builder", emoji = "🦾", subtitle = "Create custom robot friend") {
                onCloseDrawer(); onNavigate(ScreenRoute.ROBOT_BUILDER)
            }
            DrawerMenuItem(title = "Bridge Builder", emoji = "🌉", subtitle = "Engineering & Stress Simulation") {
                onCloseDrawer(); onNavigate(ScreenRoute.BRIDGE_BUILDER)
            }
            DrawerMenuItem(title = "Rocket Launch", emoji = "🚀", subtitle = "Assemble stages & launch") {
                onCloseDrawer(); onNavigate(ScreenRoute.ROCKET_LAUNCH)
            }
            DrawerMenuItem(title = "Machine Maker", emoji = "⚙️", subtitle = "Gears, Pulleys & Levers") {
                onCloseDrawer(); onNavigate(ScreenRoute.MACHINE_MAKER)
            }
            DrawerMenuItem(title = "Circuit Puzzle", emoji = "💡", subtitle = "Wire batteries & lights") {
                onCloseDrawer(); onNavigate(ScreenRoute.CIRCUIT_PUZZLE)
            }
            DrawerMenuItem(title = "Gravity Experiment", emoji = "🍎", subtitle = "Drop objects in vacuum & moon") {
                onCloseDrawer(); onNavigate(ScreenRoute.GRAVITY_EXP)
            }

            // SECTION: MISSIONS
            DrawerSectionHeader(title = "MISSIONS", icon = Icons.Default.AutoAwesome, tint = STEMYellow)
            DrawerMenuItem(title = "Daily STEM Missions", emoji = "🎯", subtitle = "Earn XP & Star rewards") {
                onCloseDrawer(); onNavigate(ScreenRoute.MISSIONS)
            }
            DrawerMenuItem(title = "Weekly Challenge", emoji = "🏆", subtitle = "Space Exploration Week") {
                onCloseDrawer(); onNavigate(ScreenRoute.MISSIONS)
            }
            DrawerMenuItem(title = "Discovery Collection", emoji = "🔍", subtitle = "42 items discovered") {
                onCloseDrawer(); onNavigate(ScreenRoute.DISCOVERIES)
            }

            // SECTION: PROGRESS
            DrawerSectionHeader(title = "PROGRESS", icon = Icons.Default.EmojiEvents, tint = STEMGreen)
            DrawerMenuItem(title = "My STEM Progress", emoji = "📊", subtitle = "Level, XP & Learning Streak") {
                onCloseDrawer(); onNavigate(ScreenRoute.PROGRESS)
            }
            DrawerMenuItem(title = "Achievements & Badges", emoji = "🎖️", subtitle = "13 collectible awards") {
                onCloseDrawer(); onNavigate(ScreenRoute.PROGRESS)
            }
            DrawerMenuItem(title = "Explorer Profile", emoji = "👤", subtitle = "Customize avatar & name") {
                onCloseDrawer(); onNavigate(ScreenRoute.PROFILE)
            }

            // SECTION: SETTINGS & PARENT AREA
            DrawerSectionHeader(title = "SETTINGS & PARENTS", icon = Icons.Default.Settings, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            DrawerMenuItem(title = "Settings", emoji = "⚙️", subtitle = "Sound, Haptics, Display") {
                onCloseDrawer(); onNavigate(ScreenRoute.SETTINGS)
            }
            DrawerMenuItem(
                title = "Parent Area",
                emoji = "🛡️",
                subtitle = "Protected learning dashboard",
                isProtected = true
            ) {
                onCloseDrawer()
                onOpenParentGate()
            }
            DrawerMenuItem(title = "About Us & Creator", emoji = "ℹ️", subtitle = "Safety, Mission & LinkedIn") {
                onCloseDrawer(); onNavigate(ScreenRoute.ABOUT_US)
            }
        }
    }
}

@Composable
fun DrawerSectionHeader(title: String, icon: ImageVector, tint: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 16.dp, bottom = 6.dp, end = 20.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                fontSize = 11.sp
            ),
            color = tint
        )
    }
}

@Composable
fun DrawerMenuItem(
    title: String,
    emoji: String,
    subtitle: String,
    isProtected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (isProtected) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Parent Gate Required",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
