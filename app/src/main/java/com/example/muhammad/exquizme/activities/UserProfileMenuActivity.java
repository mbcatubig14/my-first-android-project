package com.example.muhammad.exquizme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.muhammad.exquizme.R;

public class UserProfileMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_menu);

        TextView textView = (TextView) findViewById(R.id.textViewUserName);

        final Button playQuizBtn = (Button) findViewById(R.id.play_quiz_button),
                statsBtn = (Button) findViewById(R.id.stats_button),
                reqSubBtn = (Button) findViewById(R.id.reques_subject_button),
                rateAndFeedBtn = (Button) findViewById(R.id.rate_amp_feedback_button);

        SharedPreferences sharedPreferences = getSharedPreferences("userLogin", this.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username","");

        textView.setText("Welcome to MC's Quiz " + sharedPreferences.getString("username", username));

        playQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playQuizIntent = new Intent(getApplicationContext(), PlayQuizActivity.class);
                playQuizIntent.putExtra("username", username);
                startActivity(playQuizIntent);
            }
        });


        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statsIntent = new Intent(getApplicationContext(), StatisticsActivity.class);
                statsIntent.putExtra("username", username);
                startActivity(statsIntent);
            }
        });

        reqSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reqSubIntent = new Intent(getApplicationContext(), RequestSubjectActivity.class);
                startActivity(reqSubIntent);
            }
        });

        rateAndFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rateAndFeedIntent = new Intent(getApplicationContext(), RateAndFeedbackActivity.class);
                startActivity(rateAndFeedIntent);
            }
        });
    }
}
