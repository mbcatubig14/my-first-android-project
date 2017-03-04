package com.example.muhammad.exquizme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.muhammad.exquizme.R;
import com.example.muhammad.exquizme.SQLiteDatabaseHelper;
import com.example.muhammad.exquizme.asynctasks.LoginUser;
import com.example.muhammad.exquizme.entities.QuizUser;

public class LoginUserActivity extends AppCompatActivity {


    private EditText editTextUserName;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = (EditText) findViewById(R.id.login_username);
        editTextPassword = (EditText) findViewById(R.id.login_password);

        Button buttonLogin = (Button) findViewById(R.id.login_button);

        TextView registerScreen = (TextView) findViewById(R.id.register_link);
        TextView guestPlay = (TextView) findViewById(R.id.guest_login_link);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to RegisterUserActivity screen
                Intent registerIntent = new Intent(LoginUserActivity.this, RegisterUserActivity.class);
                startActivity(registerIntent);
            }
        });

        guestPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playQuizIntent = new Intent(LoginUserActivity.this, PlayQuizActivity.class);
                startActivity(playQuizIntent);
            }
        });

    }

    private void login() {
        String quizUsername = editTextUserName.getText().toString().trim();
        String quizPassword = editTextPassword.getText().toString().trim();
        loginUser(new QuizUser(quizUsername, quizPassword));
    }

    private void loginUser(final QuizUser user) {
        String username = user.getUsername();
        String password = user.getPassword();

        Log.d("QuizUser","QuizUser: " + user.toString());

        LoginUser ulc = new LoginUser(LoginUserActivity.this);
        ulc.setUser(user);
        ulc.execute(user.getUsername(),  user.getPassword());
    }

}
