package com.example.quizwithtimer.ViewModel

import androidx.lifecycle.ViewModel
import com.example.quizwithtimer.Brain.QuizManager
import com.example.quizwithtimer.Brain.QuizState
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class QuizViewModel : ViewModel() {

    private val manager = QuizManager()
    val uiState: StateFlow<QuizState> = manager.state
    
    private var quizJob: Job? = null

    fun startQuiz(topic: String) {
        quizJob?.cancel()
        quizJob = viewModelScope.launch {
            manager.startQuiz(topic)
            manager.timer()
        }
    }

    fun onAnswerSelected(answer: Int) {
        manager.submitAnswer(answer)
    }


}