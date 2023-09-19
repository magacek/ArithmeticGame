package com.example.arithmeticgame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * A fragment that serves as the starting point for the arithmetic quiz.
 * <p>
 * It provides user interface elements for the user to select a difficulty level, arithmetic operation,
 * and specify the number of questions for the quiz. It also features increment and decrement buttons
 * to adjust the number of questions.
 * Once the user is ready, they can initiate the quiz by clicking on the start button.
 * </p>
 *
 * @author [Matt Gacek] //
 * @version 1.0
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
            val selectedDifficulty = when (view.findViewById<RadioGroup>(R.id.radioGroupDifficulty).checkedRadioButtonId) {
                R.id.radioEasy -> "easy"
                R.id.radioMedium -> "medium"
                R.id.radioHard -> "hard"
                else -> "easy" // Default
            }

            val selectedOperation = when (view.findViewById<RadioGroup>(R.id.radioGroupOperation).checkedRadioButtonId) {
                R.id.radioAddition -> "addition"
                R.id.radioSubtraction -> "subtraction"
                R.id.radioMultiplication -> "multiplication"
                R.id.radioDivision -> "division"
                else -> "addition" // Default
            }

            val numberOfQuestions = numberOfQuestionsInput.text.toString().toIntOrNull() ?: 10

            // Navigate to QuizFragment
            val action = StartFragmentDirections.actionToQuiz(selectedDifficulty, selectedOperation, numberOfQuestions)
            findNavController().navigate(action)
        }
    }
}
