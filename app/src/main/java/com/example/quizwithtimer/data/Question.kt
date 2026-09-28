package com.example.quizwithtimer.data

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val title : String ,
    val options : List<String>,
    val answerIndex : Int
)
