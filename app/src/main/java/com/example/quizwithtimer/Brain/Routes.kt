package com.example.quizwithtimer.Brain

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Question : Routes("questionScreen/{name}/{topic}")
}