package com.example.arithmeticgame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.math.RoundingMode

/**
 * A fragment that displays arithmetic questions to the user based on the selected difficulty and operation.
 * The user answers the questions one by one. After answering all the questions, the user is navigated to the score screen.
 * <p>
 * The arithmetic questions are generated dynamically based on the chosen difficulty ("easy", "medium", "hard")
 * and operation ("addition", "subtraction", "multiplication", "division").
 * </p>
 *
 * @author [Matt Gacek]
 * @version 1.0
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

        fun displayNextQuestion() {
            if (currentQuestionNumber <= numberOfQuestions) {
                currentCorrectAnswer = generateQuestion(difficulty, operation, num1TextView, arithmeticOperationTextView, num2TextView)
            } else {
                val action = QuizFragmentDirections.actionToScore(correctAnswers, numberOfQuestions)
                findNavController().navigate(action)
            }
        }

        view.findViewById<Button>(R.id.doneButton).setOnClickListener {
            val userAnswer = view.findViewById<EditText>(R.id.answerEditText).text.toString().toDoubleOrNull()
            if (userAnswer != null && currentCorrectAnswer != null && Math.abs(userAnswer - currentCorrectAnswer!!) < 0.01) { // Compare with a small threshold
                correctAnswers++
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
