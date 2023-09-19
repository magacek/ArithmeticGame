package com.example.arithmeticgame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
/**
 * A fragment that displays the user's score after completing the arithmetic quiz.
 * <p>
 * The score is calculated based on the number of correctly answered questions.
 * It provides an option for the user to try the quiz again, navigating back to the start.
 * </p>
 *
 * @author [Matt Gacek]
 * @version 1.0
 */
class ScoreFragment : Fragment(R.layout.fragment_score) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ScoreFragmentArgs by navArgs()
        val score = args.score

        val totalQuestions = args.numberOfQuestions

        view.findViewById<TextView>(R.id.scoreTextView).text = "Your score: $score out of $totalQuestions"

        view.findViewById<Button>(R.id.tryAgainButton).setOnClickListener {
            findNavController().navigate(R.id.action_global_startFragment)
        }
    }

}
