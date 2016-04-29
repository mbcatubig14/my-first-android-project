package com.example.muhammad.exquizme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RequestSubjectActivity extends AppCompatActivity {

    private static final String AUTHOR_EMAIL_ADDRESS = "mbcatubig14@gmail.com";
    private EditText editTextSubject, editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_subject);

        editTextSubject = (EditText) findViewById(R.id.editTextReqSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextReason);

        Button submitReqButton = (Button) findViewById(R.id.submitBtn);

        submitReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(editTextSubject.getText().toString(), editTextMessage.getText().toString());
            }
        });
    }

    private void sendRequest(String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AUTHOR_EMAIL_ADDRESS});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Requesting for " + subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RequestSubjectActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
