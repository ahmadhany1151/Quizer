package com.example.quizwithtimer.data

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel

class QuestionProvider {

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = "AIzaSyDdi3NF4DXFkNfJ94Zw6_F-hSIUCT6_Sjc"
    )

    // Fallback questions in case the API fails
    val fallbackQuestions = listOf(
        Question("a?", listOf("London", "Berlin", "Paris", "Madrid"), 2),
        Question("a?", listOf("Earth", "Mars", "Jupiter", "Saturn"), 1),
        Question("a?", listOf("10", "11", "12", "13"), 2),
        Question(
            "a?",
            listOf("Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"),
            1
        ),
        Question("a?", listOf("Atlantic", "Indian", "Arctic", "Pacific"), 3)
    )

    suspend fun getQuestions(topic: String = "General knowledge"): String? {
        val prompt = """
            Generate 5 multiple-choice questions about $topic.
            Return the response ONLY as a JSON array. 
            Each object in the array MUST have exactly these keys:
            "title": string (the question)
            "options": array of 4 strings (the choices)
            "answerIndex": integer (0 to 3, the index of the correct answer)
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            val responseText = response.text
            responseText
//
//            if (responseText == null) {
//                Log.e("QuestionProvider", "Gemini returned null. Using fallback.")
//                return fallbackQuestions
//            }
//
//            Log.d("QuestionProvider", "Raw Response: $responseText")
//
//            val startIndex = responseText.indexOf("[")
//            val endIndex = responseText.lastIndexOf("]")
//
//            if (startIndex == -1 || endIndex == -1) {
//                Log.e("QuestionProvider", "Invalid JSON format. Using fallback.")
//                return fallbackQuestions
//            }
//
//            val cleanJson = responseText.substring(startIndex, endIndex + 1)
//            val questions = Json.decodeFromString<List<Question>>(cleanJson)

// old method in deserialization
//            val jsonArray = JSONArray(cleanJson)
//
//            for (i in 0 until jsonArray.length()) {
//                val jsonObject = jsonArray.getJSONObject(i)
//                val optionsArray = jsonObject.getJSONArray("options")
//                val optionsList = List(optionsArray.length()) { j -> optionsArray.getString(j) }
//                questions.add(
//                    Question(
//                        title = jsonObject.getString("title"),
//                        options = optionsList,
//                        answerIndex = jsonObject.getInt("answerIndex")
//                    )
//                )
//            }

        } catch (e: Exception) {
            Log.e("QuestionProvider", "API Error: ${e.message}. Using fallback.", e)
            null
        }
    }
}