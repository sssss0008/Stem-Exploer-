package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppSettings
import com.example.data.DailyMission
import com.example.data.Discovery
import com.example.data.FactCard
import com.example.data.Planet
import com.example.data.QuizQuestion
import com.example.data.STEMGame
import com.example.data.STEMRank
import com.example.data.STEMRepository
import com.example.data.ScreenRoute
import com.example.data.SoundManager
import com.example.data.UserProfile
import com.example.data.WeeklyChallenge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CelebrationReward(
    val title: String,
    val xpEarned: Int,
    val starsEarned: Int,
    val badgeUnlocked: String? = null
)

class STEMViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = STEMRepository(application)
    private val soundManager = SoundManager(application)

    // Current navigation route
    private val _currentRoute = MutableStateFlow(ScreenRoute.HOME)
    val currentRoute: StateFlow<ScreenRoute> = _currentRoute.asStateFlow()

    // Navigation backstack for sub-screens
    private val navBackStack = mutableListOf<ScreenRoute>()

    // User profile state
    private val _userProfile = MutableStateFlow(repository.loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Settings state
    private val _settings = MutableStateFlow(repository.loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    // Daily missions
    private val _dailyMissions = MutableStateFlow(repository.getDailyMissions())
    val dailyMissions: StateFlow<List<DailyMission>> = _dailyMissions.asStateFlow()

    // Weekly Challenge
    private val _weeklyChallenge = MutableStateFlow(repository.getWeeklyChallenge())
    val weeklyChallenge: StateFlow<WeeklyChallenge> = _weeklyChallenge.asStateFlow()

    // Achievements
    private val _achievements = MutableStateFlow(repository.getAchievements())
    val achievements = _achievements.asStateFlow()

    // Discoveries
    private val _discoveries = MutableStateFlow(repository.getDiscoveries())
    val discoveries: StateFlow<List<Discovery>> = _discoveries.asStateFlow()

    // Planets
    val planets: List<Planet> = repository.getPlanets()

    // STEM Games
    val games: List<STEMGame> = repository.getSTEMGames()

    // Quiz questions
    val quizQuestions: List<QuizQuestion> = repository.getQuizQuestions()

    // Tech facts
    val techFacts: List<FactCard> = repository.getTechFacts()

    // Active celebration reward dialog
    private val _activeReward = MutableStateFlow<CelebrationReward?>(null)
    val activeReward: StateFlow<CelebrationReward?> = _activeReward.asStateFlow()

    fun navigateTo(route: ScreenRoute) {
        soundManager.playClickSound(_settings.value.soundFxEnabled)
        soundManager.triggerHaptic(_settings.value.hapticEnabled)
        if (_currentRoute.value != route) {
            navBackStack.add(_currentRoute.value)
            _currentRoute.value = route
        }
    }

    fun navigateBack(): Boolean {
        soundManager.playClickSound(_settings.value.soundFxEnabled)
        return if (navBackStack.isNotEmpty()) {
            _currentRoute.value = navBackStack.removeAt(navBackStack.size - 1)
            true
        } else if (_currentRoute.value != ScreenRoute.HOME) {
            _currentRoute.value = ScreenRoute.HOME
            true
        } else {
            false
        }
    }

    fun dismissReward() {
        _activeReward.value = null
    }

    fun addRewards(title: String, xp: Int, stars: Int, activityId: String? = null, discoveryId: String? = null) {
        soundManager.playCelebrationSound(_settings.value.soundFxEnabled)
        soundManager.triggerHaptic(_settings.value.hapticEnabled)

        val current = _userProfile.value
        val newXp = current.xp + xp
        val newStars = current.stars + stars
        val newActivities = if (activityId != null) current.completedActivities + activityId else current.completedActivities
        val newDiscoveries = if (discoveryId != null) current.discoveredIds + discoveryId else current.discoveredIds

        val updatedProfile = current.copy(
            xp = newXp,
            stars = newStars,
            completedActivities = newActivities,
            discoveredIds = newDiscoveries
        )

        _userProfile.value = updatedProfile
        repository.saveUserProfile(updatedProfile)

        // Check if level changed
        val oldRank = STEMRank.fromXp(current.xp)
        val newRank = STEMRank.fromXp(newXp)
        val rankUnlocked = if (newRank != oldRank) newRank.title else null

        _activeReward.value = CelebrationReward(
            title = title,
            xpEarned = xp,
            starsEarned = stars,
            badgeUnlocked = rankUnlocked
        )
    }

    fun claimMission(missionId: String) {
        val list = _dailyMissions.value.toMutableList()
        val idx = list.indexOfFirst { it.id == missionId }
        if (idx != -1) {
            val mission = list[idx]
            if (mission.isCompleted && !mission.isClaimed) {
                list[idx] = mission.copy(isClaimed = true)
                _dailyMissions.value = list
                addRewards("Mission Claimed!", mission.xpReward, mission.starsReward)
            }
        }
    }

    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
        repository.saveSettings(newSettings)
    }

    fun updateUserAvatar(newAvatarId: String) {
        val updated = _userProfile.value.copy(avatarId = newAvatarId)
        _userProfile.value = updated
        repository.saveUserProfile(updated)
    }

    fun updateUserName(newName: String) {
        val updated = _userProfile.value.copy(name = newName)
        _userProfile.value = updated
        repository.saveUserProfile(updated)
    }

    fun resetProgress() {
        val fresh = UserProfile()
        _userProfile.value = fresh
        repository.saveUserProfile(fresh)
        _dailyMissions.value = repository.getDailyMissions()
    }

    fun playClickSound() {
        soundManager.playClickSound(_settings.value.soundFxEnabled)
        soundManager.triggerHaptic(_settings.value.hapticEnabled)
    }

    fun playSuccessSound() {
        soundManager.playSuccessSound(_settings.value.soundFxEnabled)
        soundManager.triggerHaptic(_settings.value.hapticEnabled)
    }

    fun playErrorSound() {
        soundManager.playErrorSound(_settings.value.soundFxEnabled)
    }

    // Dynamic Home Personalization Message
    fun getDynamicHomePrompt(): String {
        val profile = _userProfile.value
        val rank = STEMRank.fromXp(profile.xp)
        val nextRank = STEMRank.nextRank(rank)
        val xpNeeded = if (nextRank != null) nextRank.minXp - profile.xp else 0

        return when {
            xpNeeded in 1..80 -> "Only $xpNeeded XP until you reach ${nextRank?.title}!"
            !_dailyMissions.value.all { it.isCompleted } -> "Your Daily STEM Mission is waiting!"
            !profile.completedActivities.contains("robot_builder") -> "Continue your Robot Mission!"
            !profile.discoveredIds.contains("mars") -> "Explore Mars in the Solar System!"
            else -> "Ready to discover something amazing today?"
        }
    }
}
