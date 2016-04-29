package com.example.muhammad.exquizme;

import java.util.Arrays;

/**
 * Class model for Question for the quiz
 * Created by Muhammad on 19/04/2016.
 */
class QuizQuestion {
    final String question;
    String[] choices = new String[3];
    final String answer;
    final String category;

    public QuizQuestion(String question, String[] choices, String answer, String category) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.category = category;
    }

    @Override
    public String toString() {
        return "QuizQuestion: " + question + ", " + Arrays.toString(choices) + ", " + answer + ", " + category;
    }
}
