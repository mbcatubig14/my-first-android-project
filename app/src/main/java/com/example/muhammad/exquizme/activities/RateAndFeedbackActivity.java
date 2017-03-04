package com.example.muhammad.exquizme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.muhammad.exquizme.R;

public class RateAndFeedbackActivity extends AppCompatActivity {

    private static final String AUTHOR_EMAIL_ADDRESS = "mbcatubig14@gmail.com";
    private EditText editTextFeedback;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_feedback);

        editTextFeedback = (EditText) findViewById(R.id.editTextComment);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button submitFeedbackButton = (Button) findViewById(R.id.submitFeedbackButton);

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback(ratingBar.getRating() + "/" + ratingBar.getNumStars(), editTextFeedback.getText().toString());
            }
        });
    }

    private void sendFeedback(String overallRate, String feedback) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AUTHOR_EMAIL_ADDRESS});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback + "\nRating: " + overallRate);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Feedback..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RateAndFeedbackActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
