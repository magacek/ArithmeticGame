package com.example.arithmeticgame

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

/**
 * A fragment representing the initial setup for the arithmetic quiz.
 * <p>
 * This fragment provides various user interface elements that allow the user to tailor their quiz experience:
 * - Selection of difficulty level (easy, medium, hard).
 * - Choice of arithmetic operation (addition, subtraction, multiplication, division).
 * - Setting the total number of questions via an input field, complemented by increment and decrement buttons.
 *
 * Upon defining their preferences, users can initiate the quiz by tapping the start button. If the quiz has
 * been previously attempted, the user's score is displayed prominently, providing feedback on their last performance.
 * This score display dynamically adjusts the layout, ensuring a cohesive and intuitive interface.
 * </p>
 *
 * @author [Matt Gacek]
 * @version 2.0
 */
class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val numberOfQuestionsInput = view.findViewById<EditText>(R.id.numberQuestions)
        val decrementButton = view.findViewById<Button>(R.id.decrement_button)
        val incrementButton = view.findViewById<Button>(R.id.increment_button)

        // Set "Easy" as the default difficulty
        val radioEasy = view.findViewById<RadioButton>(R.id.radioEasy)
        radioEasy.setChecked(true)

        // Set "Addition" as the default operation
        val radioAddition = view.findViewById<RadioButton>(R.id.radioAddition)
        radioAddition.setChecked(true)


        decrementButton.setOnClickListener {
            val currentNumber = numberOfQuestionsInput.text.toString().toIntOrNull() ?: 1
            if (currentNumber > 1) {
                numberOfQuestionsInput.setText((currentNumber - 1).toString())
            }
        }

        incrementButton.setOnClickListener {
            val currentNumber = numberOfQuestionsInput.text.toString().toIntOrNull() ?: 1
            numberOfQuestionsInput.setText((currentNumber + 1).toString())
        }

        view.findViewById<Button>(R.id.startButton).setOnClickListener {
            // Get user's choices
            val selectedDifficulty =
                when (view.findViewById<RadioGroup>(R.id.radioGroupDifficulty).checkedRadioButtonId) {
                    R.id.radioEasy -> "easy"
                    R.id.radioMedium -> "medium"
                    R.id.radioHard -> "hard"
                    else -> "easy" // Default
                }

            val selectedOperation =
                when (view.findViewById<RadioGroup>(R.id.radioGroupOperation).checkedRadioButtonId) {
                    R.id.radioAddition -> "addition"
                    R.id.radioSubtraction -> "subtraction"
                    R.id.radioMultiplication -> "multiplication"
                    R.id.radioDivision -> "division"
                    else -> "addition" // Default
                }

            val numberOfQuestions = numberOfQuestionsInput.text.toString().toIntOrNull() ?: 10

            // Navigate to QuizFragment
            val action = StartFragmentDirections.actionToQuiz(
                selectedDifficulty,
                selectedOperation,
                numberOfQuestions
            )
            findNavController().navigate(action)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("QUIZ_RESULT")?.observe(viewLifecycleOwner, Observer { result ->
            val correctAnswers = result.getInt("correctAnswers")
            val totalQuestions = result.getInt("totalQuestions")
            val selectedOperation = result.getString("selectedOperation") ?: "operation"

            val formattedOperation = when(selectedOperation) {
                "addition" -> "addition"
                "subtraction" -> "subtraction"
                "multiplication" -> "multiplication"
                "division" -> "division"
                else -> "operation"
            }

            val percentage = (correctAnswers.toDouble() / totalQuestions.toDouble()) * 100
            val resultTextView = view.findViewById<TextView>(R.id.resultTextView)

            if (percentage >= 80) {
                resultTextView.text = "You got $correctAnswers out of $totalQuestions correct in $formattedOperation. Good work!"

            } else {
                resultTextView.text = "You got $correctAnswers out of $totalQuestions correct in $formattedOperation. You need to practice more!"
                resultTextView.setTextColor(Color.RED)
            }

            resultTextView.visibility = View.VISIBLE
        })


    }

    }

