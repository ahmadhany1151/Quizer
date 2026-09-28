package com.example.quizwithtimer.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.quizwithtimer.Brain.QuizState
import com.example.quizwithtimer.R
import com.example.quizwithtimer.ViewModel.QuizViewModel
import com.example.quizwithtimer.data.Question
import androidx.compose.foundation.lazy.grid.itemsIndexed as gridItemsIndexed

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier, viewModel: QuizViewModel = viewModel(),
    name: String = "name", topic: String = "General Knowledge"
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAnswers by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(
            false
        )
    }

    LaunchedEffect(key1 = topic) {
        viewModel.startQuiz(topic)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF121212),
                        Color(0xFF1A1A2E)
                    )
                )
            )
    ) {
        StarBackground()
        when (val currentState = state) {
            is QuizState.Loading -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.live_chatbot))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            is QuizState.Question -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Question Counter at the Top
                    Text(
                        text = "QUESTION ${currentState.index} / ${currentState.totalQuestions}",
                        color = Color(0xFFF9E400),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar for the whole quiz
                    LinearProgressIndicator(
                        progress = { currentState.index.toFloat() / currentState.totalQuestions },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        strokeCap = StrokeCap.Round,
                        trackColor = Color.White.copy(alpha = 0.1f),
                        color = Color(0xFFF9E400)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 2. Question Bubble
                    Card(
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentState.question.title,
                            modifier = Modifier.padding(24.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 3. Answer Options Grid
                    val borderColors = listOf(
                        Color(0xFF612ECC), // Purple
                        Color(0xFF0B7FB2), // Pink
                        Color(0xFF16946F), // Green
                        Color(0xFF982323)  // Yellow
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        gridItemsIndexed(currentState.question.options) { index, option ->
                            val borderColor = borderColors[index % borderColors.size]
                            Card(
                                onClick = { viewModel.onAnswerSelected(index) },
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.1f)
                                ),
                                border = BorderStroke(2.dp, borderColor),
                                modifier = Modifier.height(110.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = option,
                                        modifier = Modifier.padding(12.dp),
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // 4. Circular Timer at the Bottom
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { currentState.remainingTime / 30f },
                            modifier = Modifier.size(100.dp),
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round,
                            color = if (currentState.remainingTime <= 5)
                                Color(0xFFE587B1)
                            else
                                Color(0xFF69D4B2),
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )

                        Text(
                            text = currentState.remainingTime.toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = if (currentState.remainingTime <= 5)
                                Color(0xFFE587B1)
                            else
                                Color.White
                        )
                    }
                }
            }

            is QuizState.Result -> {
                if (showAnswers) {
                    Answers(
                        questions = currentState.questions,
                        selectedAnswers = currentState.userAnswers,
                        onBackClick = { showAnswers = false }
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "QUIZ OVER!",
                            color = Color(0xFFF9E400),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Black
                        )

                        Text(
                            text = "Great job, $name!",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Card(
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Your Score",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${currentState.score} / ${currentState.totalQuestions}",
                                    color = Color(0xFF69D4B2),
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = { viewModel.startQuiz(topic) },
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF69D4B2))
                        ) {
                            Text(
                                "PLAY AGAIN",
                                color = Color(0xFF121212),
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showAnswers = true },
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA584E8))
                        ) {
                            Text(
                                "SHOW ANSWERS",
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Answers(
    modifier: Modifier = Modifier,
    questions: List<Question>,
    selectedAnswers: List<Int>,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF121212),
                        Color(0xFF1A1A2E)
                    )
                )
            )
    ) {
        StarBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CORRECTIONS",
                color = Color(0xFFF9E400),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(questions) { qIndex, question ->
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${qIndex + 1}. ${question.title}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            question.options.forEachIndexed { oIndex, option ->
                                val isCorrect = (oIndex == question.answerIndex)
                                val isSelected = (oIndex == selectedAnswers.getOrNull(qIndex))

                                val cardColor = when {
                                    isCorrect -> Color(0xFF69D4B2) // Green
                                    isSelected && !isCorrect -> Color(0xFFE587B1) // Pink
                                    else -> Color.White.copy(alpha = 0.05f)
                                }

                                val textColor = if (isCorrect || isSelected) Color.Black else Color.White

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardColor),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${if (isCorrect) "✓ " else if (isSelected) "✗ " else "• "} $option",
                                        color = textColor,
                                        fontWeight = if (isCorrect || isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackClick,
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF69D4B2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    "BACK TO RESULTS",
                    color = Color(0xFF121212),
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
