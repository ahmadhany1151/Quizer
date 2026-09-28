package com.example.quizwithtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizwithtimer.Brain.Routes
import com.example.quizwithtimer.screens.HomeScreen
import com.example.quizwithtimer.screens.QuestionScreen
import com.example.quizwithtimer.ui.theme.QuizWithTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizWithTimerTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.Home.route
                ) {
                    composable(Routes.Home.route) {
                        HomeScreen(onClick = { name , topic ->
                            navController.navigate("questionScreen/$name/$topic")
                        })
                    }
                    composable(
                        route = Routes.Question.route,
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("topic") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val topic = backStackEntry.arguments?.getString("topic") ?: ""
                        QuestionScreen(name = name, topic = topic)
                    }
                }
            }
        }
    }
}
