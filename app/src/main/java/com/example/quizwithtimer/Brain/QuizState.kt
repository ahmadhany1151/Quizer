package com.example.quizwithtimer.Brain


sealed class QuizState {
    data object Loading : QuizState()
    data class Question(
        val question: com.example.quizwithtimer.data.Question,
        val index: Int,
        val totalQuestions: Int,
        var remainingTime: Int
    ) : QuizState()

    data class Result(
        val score: Int,
        val totalQuestions: Int,
        val questions: List<com.example.quizwithtimer.data.Question>,
        val userAnswers: List<Int>
    ) : QuizState()
}