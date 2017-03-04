package com.example.muhammad.exquizme.entities;

import java.util.Arrays;

/**
 * Class model for Question for the quiz
 * Created by Muhammad on 19/04/2016.
 */
public class QuizQuestion {
    private String question;
    private String answer;
    private String category;
    private String[] choices = new String[3];

    public QuizQuestion(String question, String[] choices, String answer, String category) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getAnswer() {
        return answer;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return "QuizQuestion: " + question + ", " + Arrays.toString(choices) + ", " + answer + ", " + category;
    }
}
