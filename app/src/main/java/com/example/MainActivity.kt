package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ScreenRoute
import com.example.ui.components.CelebrationRewardDialog
import com.example.ui.components.ParentalGateDialog
import com.example.ui.components.STEMBottomBar
import com.example.ui.components.STEMDrawerContent
import com.example.ui.components.STEMTopBar
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.DiscoveriesScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MissionsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ParentAreaScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TechFactsScreen
import com.example.ui.screens.games.AILearningGame
import com.example.ui.screens.games.BridgeBuilderGame
import com.example.ui.screens.games.CircuitPuzzleGame
import com.example.ui.screens.games.CodingKidsGame
import com.example.ui.screens.games.GravityExperimentGame
import com.example.ui.screens.games.MachineMakerGame
import com.example.ui.screens.games.PhysicsFunGame
import com.example.ui.screens.games.RobotBuilderGame
import com.example.ui.screens.games.RocketLaunchGame
import com.example.ui.screens.games.ScienceLabGame
import com.example.ui.screens.games.SolarSystemGame
import com.example.ui.screens.games.SpaceMissionGame
import com.example.ui.theme.STEMExplorerTheme
import com.example.viewmodel.STEMViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            STEMExplorerTheme {
                STEMApp()
            }
        }
    }
}

@Composable
fun STEMApp(viewModel: STEMViewModel = viewModel()) {
    val currentRoute by viewModel.currentRoute.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val activeReward by viewModel.activeReward.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var showParentGateForArea by remember { mutableStateOf(false) }

    // Intercept hardware Back button for nested screens
    BackHandler(enabled = currentRoute != ScreenRoute.HOME) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            viewModel.navigateBack()
        }
    }

    // Parental Gate verification for Parent Area
    if (showParentGateForArea) {
        ParentalGateDialog(
            onDismiss = { showParentGateForArea = false },
            onSuccess = {
                showParentGateForArea = false
                viewModel.navigateTo(ScreenRoute.PARENT_AREA)
            }
        )
    }

    // Celebration Reward Modal
    if (activeReward != null) {
        CelebrationRewardDialog(
            title = activeReward!!.title,
            xpEarned = activeReward!!.xpEarned,
            starsEarned = activeReward!!.starsEarned,
            badgeUnlocked = activeReward!!.badgeUnlocked,
            onDismiss = { viewModel.dismissReward() }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                STEMDrawerContent(
                    userProfile = userProfile,
                    onNavigate = { route -> viewModel.navigateTo(route) },
                    onCloseDrawer = { coroutineScope.launch { drawerState.close() } },
                    onOpenParentGate = { showParentGateForArea = true }
                )
            }
        }
    ) {
        // Main scaffold
        val isBottomNavScreen = currentRoute in listOf(
            ScreenRoute.HOME,
            ScreenRoute.PRACTICE,
            ScreenRoute.QUIZ,
            ScreenRoute.ABOUT_US
        )

        Scaffold(
            topBar = {
                if (isBottomNavScreen) {
                    STEMTopBar(
                        userProfile = userProfile,
                        onMenuClick = { coroutineScope.launch { drawerState.open() } },
                        onProfileClick = { viewModel.navigateTo(ScreenRoute.PROFILE) }
                    )
                }
            },
            bottomBar = {
                // CRITICAL: Exactly FOUR bottom navigation items
                AnimatedVisibility(
                    visible = isBottomNavScreen,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    STEMBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { route -> viewModel.navigateTo(route) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentRoute) {
                    // Core 4 Bottom Nav destinations
                    ScreenRoute.HOME -> HomeScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    ScreenRoute.PRACTICE -> PracticeScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    ScreenRoute.QUIZ -> QuizScreen(viewModel = viewModel)
                    ScreenRoute.ABOUT_US -> AboutUsScreen()

                    // Playable Games & Simulations
                    ScreenRoute.ROBOT_BUILDER -> RobotBuilderGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.CODING_GAME -> CodingKidsGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.CIRCUIT_PUZZLE -> CircuitPuzzleGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.SPACE_MISSION -> SpaceMissionGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.ROCKET_LAUNCH -> RocketLaunchGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.BRIDGE_BUILDER -> BridgeBuilderGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.MACHINE_MAKER -> MachineMakerGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.GRAVITY_EXP -> GravityExperimentGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.SCIENCE_LAB -> ScienceLabGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.SOLAR_SYSTEM -> SolarSystemGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.AI_LEARNING -> AILearningGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.PHYSICS_FUN -> PhysicsFunGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })

                    // Educational Content & Features
                    ScreenRoute.FLASHCARDS -> FlashcardsScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.TECH_FACTS -> TechFactsScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.MISSIONS -> MissionsScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.PROGRESS -> ProgressScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.DISCOVERIES -> DiscoveriesScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.PROFILE -> ProfileScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.SETTINGS -> SettingsScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.PARENT_AREA -> ParentAreaScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
                    ScreenRoute.ONBOARDING -> OnboardingScreen(onFinish = { viewModel.navigateTo(ScreenRoute.HOME) })
                }
            }
        }
    }
}
