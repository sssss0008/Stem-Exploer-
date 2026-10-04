package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizQuestion
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.CosmicPurpleContainer
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueContainer
import com.example.ui.theme.STEMGreen
import com.example.ui.theme.STEMOrange
import com.example.ui.theme.STEMYellow
import com.example.viewmodel.STEMViewModel

enum class QuizState {
    SELECT_MODE,
    PLAYING,
    COMPLETED
}

@Composable
fun QuizScreen(
    viewModel: STEMViewModel,
    modifier: Modifier = Modifier
) {
    val questions = viewModel.quizQuestions

    var quizState by remember { mutableStateOf(QuizState.SELECT_MODE) }
    var selectedQuizTitle by remember { mutableStateOf("Quick Quiz") }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    when (quizState) {
        QuizState.SELECT_MODE -> {
            QuizModeSelection(
                onSelectMode = { modeName ->
                    selectedQuizTitle = modeName
                    currentQuestionIndex = 0
                    score = 0
                    selectedAnswerIndex = null
                    isAnswerSubmitted = false
                    quizState = QuizState.PLAYING
                },
                modifier = modifier
            )
        }
        QuizState.PLAYING -> {
            val q = questions.getOrElse(currentQuestionIndex) { questions.first() }
            QuizGameplay(
                title = selectedQuizTitle,
                question = q,
                questionNumber = currentQuestionIndex + 1,
                totalQuestions = questions.size,
                selectedAnswer = selectedAnswerIndex,
                isSubmitted = isAnswerSubmitted,
                onSelectOption = { idx ->
                    if (!isAnswerSubmitted) {
                        selectedAnswerIndex = idx
                        isAnswerSubmitted = true
                        if (idx == q.correctIndex) {
                            score++
                            viewModel.playSuccessSound()
                        } else {
                            viewModel.playErrorSound()
                        }
                    }
                },
                onNextQuestion = {
                    if (currentQuestionIndex + 1 < questions.size) {
                        currentQuestionIndex++
                        selectedAnswerIndex = null
                        isAnswerSubmitted = false
                    } else {
                        // Complete quiz
                        val xpEarned = score * 10
                        val starsEarned = (score / 2).coerceAtLeast(2)
                        viewModel.addRewards("Quiz Completed!", xpEarned, starsEarned, "quiz_master")
                        quizState = QuizState.COMPLETED
                    }
                },
                modifier = modifier
            )
        }
        QuizState.COMPLETED -> {
            QuizCompleteScreen(
                score = score,
                totalQuestions = questions.size,
                xpEarned = score * 10,
                starsEarned = (score / 2).coerceAtLeast(2),
                onTryAgain = {
                    currentQuestionIndex = 0
                    score = 0
                    selectedAnswerIndex = null
                    isAnswerSubmitted = false
                    quizState = QuizState.PLAYING
                },
                onBackToQuiz = {
                    quizState = QuizState.SELECT_MODE
                },
                modifier = modifier
            )
        }
    }
}

@Composable
fun QuizModeSelection(
    onSelectMode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("quiz_screen_modes")
    ) {
        Text(
            text = "STEM Quiz",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
        Text(
            text = "Test what you've discovered across science, coding, and space!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quiz Modes
        val modes = listOf(
            QuizModeCardData(
                title = "Quick Quiz",
                subtitle = "10 Fun Questions across all STEM topics",
                emoji = "⚡",
                tag = "POPULAR",
                bg = ElectricBlueContainer,
                tint = ElectricBlue
            ),
            QuizModeCardData(
                title = "Daily Quiz",
                subtitle = "Fresh daily challenge questions to test your wits",
                emoji = "📅",
                tag = "TODAY",
                bg = Color(0xFFFEF3C7),
                tint = STEMOrange
            ),
            QuizModeCardData(
                title = "Speed Round",
                subtitle = "Answer rapid-fire questions against the clock!",
                emoji = "⏱️",
                tag = "FAST",
                bg = Color(0xFFCFFAFE),
                tint = CosmicPurple
            ),
            QuizModeCardData(
                title = "Challenge Quiz",
                subtitle = "Advanced questions for ambitious young scientists!",
                emoji = "🏆",
                tag = "HARD",
                bg = Color(0xFFEDE9FE),
                tint = CosmicPurple
            )
        )

        modes.forEach { mode ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onSelectMode(mode.title) }
                    .testTag("quiz_mode_${mode.title.replace(" ", "_")}"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(mode.bg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = mode.emoji, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(mode.tint.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = mode.tag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = mode.tint
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = mode.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

data class QuizModeCardData(
    val title: String,
    val subtitle: String,
    val emoji: String,
    val tag: String,
    val bg: Color,
    val tint: Color
)

@Composable
fun QuizGameplay(
    title: String,
    question: QuizQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    selectedAnswer: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = questionNumber.toFloat() / totalQuestions.toFloat()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("quiz_gameplay_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = ElectricBlue
            )
            Text(
                text = "Question $questionNumber / $totalQuestions",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ElectricBlue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(question.category.colorHex).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = question.emoji, fontSize = 26.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = question.category.displayName.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(question.category.colorHex)
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Options
        question.options.forEachIndexed { index, optionText ->
            val isSelected = selectedAnswer == index
            val isCorrect = index == question.correctIndex

            val borderColor = when {
                !isSubmitted -> if (isSelected) ElectricBlue else MaterialTheme.colorScheme.outlineVariant
                isCorrect -> STEMGreen
                isSelected -> STEMOrange
                else -> MaterialTheme.colorScheme.outlineVariant
            }

            val bgColor = when {
                !isSubmitted -> if (isSelected) ElectricBlueContainer else MaterialTheme.colorScheme.surface
                isCorrect -> Color(0xFFD1FAE5)
                isSelected -> Color(0xFFFFEDD5)
                else -> MaterialTheme.colorScheme.surface
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(bgColor)
                    .border(2.dp, borderColor, RoundedCornerShape(18.dp))
                    .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .testTag("quiz_option_$index"),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(borderColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = listOf("A", "B", "C", "D").getOrElse(index) { "?" },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (isSubmitted) {
                        if (isCorrect) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Correct",
                                tint = STEMGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Incorrect",
                                tint = STEMOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Child-friendly feedback and explanation
        AnimatedVisibility(
            visible = isSubmitted,
            enter = fadeIn() + slideInVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                val isAnswerCorrect = selectedAnswer == question.correctIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isAnswerCorrect) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = if (isAnswerCorrect) "🎉 Great thinking!" else "💡 Good try! Let's learn why:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isAnswerCorrect) Color(0xFF065F46) else Color(0xFF92400E)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isAnswerCorrect) Color(0xFF065F46) else Color(0xFF92400E)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNextQuestion,
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        text = if (questionNumber == totalQuestions) "FINISH QUIZ" else "NEXT QUESTION",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizCompleteScreen(
    score: Int,
    totalQuestions: Int,
    xpEarned: Int,
    starsEarned: Int,
    onTryAgain: () -> Unit,
    onBackToQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(CosmicPurpleContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🏆", fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "QUIZ COMPLETE!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Fantastic effort, Explorer! You answered $score out of $totalQuestions correctly.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Rewards Box
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ElectricBlueContainer),
                modifier = Modifier.weight(1f).padding(end = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "+$xpEarned XP",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricBlue
                        )
                    )
                    Text(text = "Experience", style = MaterialTheme.typography.labelSmall)
                }
            }

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicPurpleContainer),
                modifier = Modifier.weight(1f).padding(start = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "+$starsEarned Stars",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicPurple
                        )
                    )
                    Text(text = "STEM Stars", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onTryAgain,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quiz_try_again_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("TRY AGAIN", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackToQuiz,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quiz_back_button")
        ) {
            Text("BACK TO QUIZ MODES", fontWeight = FontWeight.Bold)
        }
    }
}
