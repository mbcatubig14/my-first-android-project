package com.example.muhammad.exquizme.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammad.exquizme.PostRequestHandler;
import com.example.muhammad.exquizme.R;
import com.example.muhammad.exquizme.WebRequestURLConstants;
import com.example.muhammad.exquizme.entities.QuizUser;
import com.example.muhammad.exquizme.asynctasks.RegisterUser;

import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.reg_username);
        editTextPassword = (EditText) findViewById(R.id.reg_password);
        editTextEmail = (EditText) findViewById(R.id.reg_email);

        Button buttonRegister = (Button) findViewById(R.id.btnRegister);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        //Listening to Register Button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Listening to LoginUserActivity Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to LoginUserActivity Screen/closing register screen
                finish();
            }
        });
    }

    private void registerUser() {
        String quizUsername = editTextUsername.getText().toString().trim().toLowerCase();
        String quizPassword = editTextPassword.getText().toString().trim().toLowerCase();
        String quizEmail = editTextEmail.getText().toString().trim().toLowerCase();

        register(new QuizUser(quizUsername, quizPassword, quizEmail));
    }

    private void register(QuizUser user) {
        String quizUsername = user.getUsername(), quizPassword = user.getPassword(), quizEmail = user.getEmail();


        RegisterUser ru = new RegisterUser(RegisterUserActivity.this);
        ru.execute(quizUsername, quizPassword, quizEmail);
    }


}
