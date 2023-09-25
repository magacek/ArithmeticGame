package com.example.arithmeticgame

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.math.RoundingMode
/**
 * A fragment representing the starting point of the arithmetic quiz. It allows users to select their preferred
 * difficulty level and arithmetic operation type for the quiz. Users also have the option to set the number
 * of questions for the quiz. Once set, they can initiate the quiz by tapping the start button.
 *
 * Upon completing the quiz, users will be navigated back to this screen, where they will be presented with
 * their score. The score will be displayed above the difficulty selection, pushing the difficulty and operation
 * selection downwards to accommodate the score display.
 *
 * The arithmetic questions in the quiz are dynamically generated based on the chosen difficulty
 * ("easy", "medium", "hard") and operation ("addition", "subtraction", "multiplication", "division").
 *
 * @author [Matt Gacek]
 * @version 2.0
 */

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in the view.
     * This method initializes the UI elements, sets up listeners, and displays the first question.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: QuizFragmentArgs by navArgs()
        val difficulty = args.difficulty
        val operation = args.operation
        val numberOfQuestions = args.numberOfQuestions

        val num1TextView = view.findViewById<TextView>(R.id.num1)
        val arithmeticOperationTextView = view.findViewById<TextView>(R.id.arithmeticOperation)
        val num2TextView = view.findViewById<TextView>(R.id.num2)

        var currentQuestionNumber = 1
        var correctAnswers = 0
        var currentCorrectAnswer: Double? = null


        val mediaPlayer = MediaPlayer.create(context, R.raw.correct_sound)
        mediaPlayer.start()

        fun displayNextQuestion() {
            if (currentQuestionNumber <= numberOfQuestions) {
                currentCorrectAnswer = generateQuestion(difficulty, operation, num1TextView, arithmeticOperationTextView, num2TextView)
            } else {
                val data = Bundle()
                data.putInt("correctAnswers", correctAnswers)
                data.putInt("totalQuestions", numberOfQuestions)
                data.putString("selectedOperation", operation)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("QUIZ_RESULT", data)
                findNavController().popBackStack()
                println("Setting QUIZ_RESULT: $data")

            }
        }

        view.findViewById<Button>(R.id.doneButton).setOnClickListener {
            val userAnswer = view.findViewById<EditText>(R.id.answerEditText).text.toString().toDoubleOrNull()
            if (userAnswer != null && currentCorrectAnswer != null && Math.abs(userAnswer - currentCorrectAnswer!!) < 0.01) {
                correctAnswers++
                Toast.makeText(context, "Correct. Good work!", Toast.LENGTH_SHORT).show()
                val mediaPlayer = MediaPlayer.create(context, R.raw.correct_sound)
                mediaPlayer.start()
            } else {
                Toast.makeText(context, "Wrong", Toast.LENGTH_SHORT).show()
                val mediaPlayer = MediaPlayer.create(context, R.raw.wrong_sound)
                mediaPlayer.start()
            }
            currentQuestionNumber++
            displayNextQuestion()
            view.findViewById<EditText>(R.id.answerEditText).text.clear()
        }


        displayNextQuestion()
    }

    /**
     * Generates a new arithmetic question based on the provided difficulty and operation.
     * Updates the UI with the generated numbers and arithmetic operation.
     *
     * @param difficulty The difficulty level of the question ("easy", "medium", "hard").
     * @param operation The arithmetic operation for the question ("addition", "subtraction", "multiplication", "division").
     * @param num1TextView The TextView to display the first number.
     * @param arithmeticOperationTextView The TextView to display the arithmetic operation.
     * @param num2TextView The TextView to display the second number.
     * @return The correct answer for the generated question.
     */

    private fun generateQuestion(difficulty: String, operation: String, num1TextView: TextView, arithmeticOperationTextView: TextView, num2TextView: TextView): Double? {
        val maxNumber = when (difficulty) {
            "easy" -> 10
            "medium" -> 25
            "hard" -> 50
            else -> 10
        }

        val num1 = (1..maxNumber).random()
        val num2 = (1..maxNumber).random()

        num1TextView.text = num1.toString()
        num2TextView.text = num2.toString()

        return when (operation) {
            "addition" -> {
                arithmeticOperationTextView.text = "+"
                (num1 + num2).toDouble()
            }
            "subtraction" -> {
                arithmeticOperationTextView.text = "-"
                (num1 - num2).toDouble()
            }
            "multiplication" -> {
                arithmeticOperationTextView.text = "x"
                (num1 * num2).toDouble()
            }
            "division" -> {
                if (num2 == 0) {
                    // Recursive call if divisor is zero and directly return its value
                    return generateQuestion(difficulty, operation, num1TextView, arithmeticOperationTextView, num2TextView)
                }
                arithmeticOperationTextView.text = "÷"
                (num1.toDouble() / num2).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
            }
            else -> {
                arithmeticOperationTextView.text = "+"
                (num1 + num2).toDouble()
            }
        }
    }
}
