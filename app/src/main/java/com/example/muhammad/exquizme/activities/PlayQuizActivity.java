package com.example.muhammad.exquizme.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammad.exquizme.R;
import com.example.muhammad.exquizme.SQLiteDatabaseHelper;
import com.example.muhammad.exquizme.WebRequestURLConstants;
import com.example.muhammad.exquizme.asynctasks.QuestionExtractor;
import com.example.muhammad.exquizme.entities.QuizQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayQuizActivity extends AppCompatActivity {

    private final List<String> categories = new ArrayList<>(10);
    private ArrayList<Integer> randomNumberList;
    private int randomIndex, questionIndex = 1;
    private SQLiteDatabaseHelper questionDatabase;
    private RadioButton choiceBtnA, choiceBtnB, choiceBtnC, choiceBtnD;
    private TextView questionTextView, qIndexView;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        randomNumberList = new ArrayList<>();//Initialise randomNumberList
        questionDatabase = new SQLiteDatabaseHelper(this); //Initialise questionDatabase

        qIndexView = (TextView) findViewById(R.id.currentNumber);

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        choiceBtnA = (RadioButton) findViewById(R.id.choiceA);
        choiceBtnB = (RadioButton) findViewById(R.id.choiceB);
        choiceBtnC = (RadioButton) findViewById(R.id.choiceC);
        choiceBtnD = (RadioButton) findViewById(R.id.choiceD);

        continuousCheck();

        choiceBtnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers(0);
            }
        });

        choiceBtnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers(1);
            }
        });

        choiceBtnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers(2);
            }
        });

        choiceBtnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers(3);
            }
        });
    }

    private void continuousCheck() {
        //Get Network State
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                isConnected = true;
            }
        }
        if (isConnected) {
            getJSONFromURL();
        } else {
            displayQuestion();
        }
    }

    public void displayQuestion() {
        qIndexView.setText("QUESTION " + questionIndex + "/10");
        //questionDatabase = new SQLiteDatabaseHelper(this);

        for (int index = 1; index < questionDatabase.getQuestions().size(); index++) {
            randomNumberList.add(index);
        }
        Collections.shuffle(randomNumberList);
        if (randomNumberList.size() != 0)
            randomIndex = randomNumberList.get(questionIndex - 1);
        Log.d("randomIndex", randomIndex + "");

        choiceBtnA.setChecked(false);
        choiceBtnB.setChecked(false);
        choiceBtnC.setChecked(false);
        choiceBtnD.setChecked(false);

        if (questionDatabase.getQuestions().size() != 0) {//If database is not empty
            //Play the questions until the index of 10
            if (questionIndex <= 10) {
                String question = questionDatabase.getQuestions().get(randomIndex).getQuestion(); //this causes the error
                String[] choices = new String[4];
                choices[0] = questionDatabase.getQuestions().get(randomIndex).getChoices()[0];
                choices[1] = questionDatabase.getQuestions().get(randomIndex).getChoices()[1];
                choices[2] = questionDatabase.getQuestions().get(randomIndex).getChoices()[2];
                choices[3] = questionDatabase.getQuestions().get(randomIndex).getChoices()[3];

                questionTextView.setText(question);
                choiceBtnA.setText(choices[0]);
                choiceBtnB.setText(choices[1]);
                choiceBtnC.setText(choices[2]);
                choiceBtnD.setText(choices[3]);
                questionIndex++;
            } else if (questionIndex > 10) { //Show Statistics activity after 10
                questionIndex = 1;
                Intent intent;
                intent = getIntent();
                String username = intent.getStringExtra("username");
                //If user is not logged in
                if (username == null || username.equals("") || username.isEmpty()) {
                    qIndexView.setText("You scored " + score + "%/100%!!");
                    questionTextView.setText("Thank you for playing. Login or register first to see your stats.");
                    choiceBtnA.setText("Click here to go back to login");
                    choiceBtnA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent loginIntent = new Intent(PlayQuizActivity.this, LoginUserActivity.class);
                            startActivity(loginIntent);
                        }
                    });
                    choiceBtnB.setVisibility(View.GONE);
                    choiceBtnC.setVisibility(View.GONE);
                    choiceBtnD.setVisibility(View.GONE);
                } else { //Otherwise start StatisticsActivity
                    intent = new Intent(this, StatisticsActivity.class);
                    intent.putExtra("score", "You scored " + score + "%/100!!");
                    intent.putExtra("username", username);
                    intent.putExtra("best_score", score + "");
                    String bestCategory = findBestCategory();

                    intent.putExtra("best_category", bestCategory); //It's from here.
                    startActivity(intent);
                }
            }
        }
    }

    private String findBestCategory() {
        //Finding best category
        int count = 1, tempCount;
        String bestCategory = categories.get(0);
        String tempCategory;
        for (int i = 0; i < (categories.size() - 1); i++) {
            tempCategory = categories.get(i);
            tempCount = 0;
            for (int j = 1; j < categories.size(); j++) {
                if (tempCategory.equals(categories.get(j)))
                    tempCount++;
            }
            if (tempCount > count) {
                bestCategory = tempCategory;
                count = tempCount;
            }
        }
        return bestCategory;
    }

    private void checkAnswers(int index) {
        String answer = questionDatabase.getQuestions().get(randomIndex).getChoices()[index],
                correctAnswer = questionDatabase.getQuestions().get(randomIndex).getAnswer();
        if (answer.equals(correctAnswer)) {
            score += 10;
            //Start adding categories here
            categories.add(questionDatabase.getQuestions().get(randomIndex).getCategory());
            //Show message
            Toast.makeText(PlayQuizActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayQuestion();
                }
            }, 1000);
        } else {
            Toast.makeText(PlayQuizActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayQuestion();
                }
            }, 1000);
        }
    }

    private void getJSONFromURL() {

        QuestionExtractor questionExtractorTask = new QuestionExtractor(PlayQuizActivity.this);
        questionExtractorTask.execute();
    }
}



