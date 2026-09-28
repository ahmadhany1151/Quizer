package com.example.quizwithtimer.Brain

import com.example.quizwithtimer.data.Question
import com.example.quizwithtimer.data.QuestionProvider
import com.example.quizwithtimer.data.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizManager {

    var currentIndex = 0
    val provider = QuestionProvider()
    var score = 0
    var time = 30
    private val _state = MutableStateFlow<QuizState>(QuizState.Loading)
    val state = _state.asStateFlow()
    private var allQuestions: List<Question> = emptyList()
    private val userAnswers = mutableListOf<Int>()
    val repo: Repository = Repository()


    suspend fun startQuiz(topic: String) {
        _state.value = QuizState.Loading
        try {
            allQuestions = repo.getData(topic)
            currentIndex = 0
            score = 0
            userAnswers.clear()
            if (allQuestions.isNotEmpty()) {
                updateQuestion()
            } else {
                endQuiz()
            }
        } catch (e: Exception) {
            endQuiz()
        }
    }

    suspend fun timer() {
        while (_state.value is QuizState.Question) {
            delay(1000)
            time--

            val currentState = _state.value
            if (currentState is QuizState.Question) {
                _state.value = currentState.copy(remainingTime = time)

                if (time <= 0) {
                    submitAnswer(-1)
                }
            }
        }
    }

    fun submitAnswer(selectedAnswer: Int) {
        if (allQuestions.isEmpty()) return

        userAnswers.add(selectedAnswer)

        if (allQuestions[currentIndex].answerIndex == selectedAnswer) {
            score++
        }

        if (currentIndex < allQuestions.size - 1) {
            currentIndex++
            updateQuestion()
        } else {
            endQuiz()
        }
    }

    private fun updateQuestion() {
        time = 30
        _state.value = QuizState.Question(
            question = allQuestions[currentIndex],
            index = currentIndex + 1,
            totalQuestions = allQuestions.size,
            remainingTime = time
        )
    }

    fun endQuiz() {
        _state.value = QuizState.Result(score, allQuestions.size, allQuestions, userAnswers.toList())
    }

}