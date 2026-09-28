package com.example.quizwithtimer.data

import android.util.Log
import kotlinx.serialization.json.Json

interface QuizRepo {
    suspend fun getData(topic: String): List<Question>
}

val provider = QuestionProvider()

class Repository : QuizRepo {

    override suspend fun getData(topic: String): List<Question> {
        val response = provider.getQuestions(topic)
        val parsed = parse(response)
        val validated = validate(parsed)
        return validated.ifEmpty { provider.fallbackQuestions }
    }
}


fun parse(response: String?): List<Question> {
    if (response == null) {
        Log.e("QuizRepo", "Gemini returned null. Using fallback.")
        return provider.fallbackQuestions
    }

    Log.d("QuizRepo", "Raw Response: $response")

    val startIndex = response.indexOf("[")
    val endIndex = response.lastIndexOf("]")

    if (startIndex == -1 || endIndex == -1) {
        Log.e("QuizRepo", "Invalid JSON format. Using fallback.")
        return provider.fallbackQuestions
    }

    val cleanJson = response.substring(startIndex, endIndex + 1)
    val questions = Json.decodeFromString<List<Question>>(cleanJson)
    return questions
}

fun validate(questions: List<Question>): List<Question> {

    return questions.filter {
        val isValid =
            it.title.isNotBlank() &&
                    it.options.size == 4 &&
                    it.answerIndex in 0..3

        if (!isValid) {
            Log.e("Validation", "Invalid question: $it")
        }

        isValid
    }
}

