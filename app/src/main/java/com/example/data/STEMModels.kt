package com.example.data

enum class STEMCategory(val displayName: String, val emoji: String, val colorHex: Long) {
    SCIENCE("Science", "🔬", 0xFF10B981),
    TECHNOLOGY("Technology", "💻", 0xFF0284C7),
    ENGINEERING("Engineering", "🏗️", 0xFFF97316),
    MATHEMATICS("Math", "📐", 0xFF8B5CF6),
    CODING("Coding", "⚡", 0xFF06B6D4),
    ROBOTICS("Robotics", "🤖", 0xFFEC4899),
    SPACE("Space", "🚀", 0xFF6366F1),
    PHYSICS("Physics", "⚛️", 0xFFF59E0B),
    AI("AI", "🧠", 0xFF14B8A6)
}

enum class STEMRank(val title: String, val minXp: Int, val maxXp: Int, val badgeEmoji: String) {
    SCIENTIST("Scientist", 0, 100, "🔬"),
    ENGINEER("Engineer", 101, 300, "⚙️"),
    INVENTOR("Inventor", 301, 600, "💡"),
    STEM_MASTER("STEM Master", 601, 1000, "🏆"),
    INNOVATION_HERO("Innovation Hero", 1001, Int.MAX_VALUE, "🌟");

    companion object {
        fun fromXp(xp: Int): STEMRank {
            return entries.find { xp in it.minXp..it.maxXp } ?: entries.last()
        }

        fun nextRank(current: STEMRank): STEMRank? {
            val idx = entries.indexOf(current)
            return if (idx < entries.size - 1) entries[idx + 1] else null
        }
    }
}

data class UserProfile(
    val name: String = "Young Explorer",
    val avatarId: String = "robot_explorer",
    val xp: Int = 340,
    val stars: Int = 45,
    val streakDays: Int = 5,
    val streakDaysList: List<Boolean> = listOf(true, true, true, true, true, false, false), // Mon-Sun
    val completedActivities: Set<String> = setOf("robot_builder", "space_mission", "gravity_exp"),
    val discoveredIds: Set<String> = setOf("earth", "mars", "microscope", "steam_engine", "solar_cell", "satellite")
)

data class STEMGame(
    val id: String,
    val title: String,
    val category: STEMCategory,
    val description: String,
    val emoji: String,
    val durationMin: Int,
    val xpReward: Int,
    val starsReward: Int,
    val difficulty: String = "Easy"
)

data class LearningModule(
    val id: String,
    val title: String,
    val category: STEMCategory,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val cards: List<FactCard>,
    val difficulty: String = "Beginner"
)

data class FactCard(
    val title: String,
    val content: String,
    val funFact: String,
    val emoji: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val category: STEMCategory,
    val emoji: String = "❓"
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false
)

data class DailyMission(
    val id: String,
    val title: String,
    val description: String,
    val target: Int,
    val current: Int,
    val xpReward: Int,
    val starsReward: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false
)

data class WeeklyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val lessonsDone: Int,
    val lessonsTarget: Int,
    val quizzesDone: Int,
    val quizzesTarget: Int,
    val missionsDone: Int,
    val missionsTarget: Int,
    val badgeTitle: String,
    val badgeEmoji: String,
    val isCompleted: Boolean = false
)

data class Discovery(
    val id: String,
    val title: String,
    val category: STEMCategory,
    val description: String,
    val funFact: String,
    val emoji: String,
    val isDiscovered: Boolean = false
)

data class Planet(
    val name: String,
    val emoji: String,
    val position: Int,
    val type: String,
    val diameterKm: String,
    val tempDescription: String,
    val moons: Int,
    val facts: List<String>,
    val funFact: String,
    val colorHex: Long
)

data class AppSettings(
    val soundFxEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val hapticEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val reducedMotion: Boolean = false,
    val largeText: Boolean = false,
    val language: String = "English"
)

enum class ScreenRoute {
    HOME,
    PRACTICE,
    QUIZ,
    ABOUT_US,
    // Secondary game & feature screens opened from Home / Drawer / Practice:
    ROBOT_BUILDER,
    CODING_GAME,
    CIRCUIT_PUZZLE,
    SPACE_MISSION,
    ROCKET_LAUNCH,
    BRIDGE_BUILDER,
    MACHINE_MAKER,
    GRAVITY_EXP,
    SCIENCE_LAB,
    SOLAR_SYSTEM,
    AI_LEARNING,
    PHYSICS_FUN,
    FLASHCARDS,
    TECH_FACTS,
    MISSIONS,
    PROGRESS,
    DISCOVERIES,
    PROFILE,
    SETTINGS,
    PARENT_AREA,
    ONBOARDING
}
